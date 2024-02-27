package com.wenthor.invitationservice.service;

import com.wenthor.invitationservice.client.UserServiceClient;
import com.wenthor.invitationservice.client.configuration.FeignClientConfiguration;
import com.wenthor.invitationservice.client.response.user.ResponseUser;
import com.wenthor.invitationservice.controller.dto.response.rest.RestResponse;
import com.wenthor.invitationservice.enumeration.Status;
import com.wenthor.invitationservice.exception.InvitationExpiredException;
import com.wenthor.invitationservice.exception.InvitationNotFoundException;
import com.wenthor.invitationservice.model.Invitation;
import com.wenthor.invitationservice.repository.InvitationRepository;
import com.wenthor.invitationservice.service.bo.InvitationBO;
import com.wenthor.invitationservice.service.mapper.InvitationServiceMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

@Service
public class InvitationService implements IServiceMain<InvitationBO> {
    private final InvitationRepository repository;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;

    public InvitationService(InvitationRepository repository,
                             JwtService jwtService,
                             UserServiceClient userServiceClient) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public InvitationBO create(HttpHeaders headers, InvitationBO invitationBO) {
        final ResponseUser requestUser = getResponseUserRestResponse(headers);
        final ResponseUser invitationUser = getResponseUserRestResponse(headers, invitationBO.user());
        if(invitationPendingControl(invitationBO.user()))
            throw new IllegalArgumentException("Only one pending invitation can exist per user. Please confirm or cancel the existing pending invitation. For further assistance, feel free to reach out to our support team.");
        Invitation invitation = new Invitation.Builder()
                .message(invitationBO.message())
                .status(Status.PENDING)
                .code(codeGenarator(invitationUser.email()))
                .user(invitationBO.user())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .createdBy(requestUser.id())
                .updatedBy(requestUser.id())
                .build();
        Invitation control = this.repository.findByCode(invitation.getCode()).orElse(null);
        if (control != null) {
            Stream.generate(() -> {
                        String newCode = codeGenarator(invitationUser.email());
                        return this.repository.findByCode(newCode).isPresent() ? null : newCode;
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(invitation::setCode);
        }
        Invitation po = this.repository.save(invitation);
        InvitationBO response = InvitationServiceMapper.convertToBO(po);
        return response;
    }

    @Override
    public InvitationBO findByID(UUID id) {
        if (id != null) {
            Invitation invitation = this.repository.findById(id).orElseThrow(
                    () -> new InvitationNotFoundException(id)
            );
            return InvitationServiceMapper.convertToBO(invitation);
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    @Override
    public InvitationBO updateByID(HttpHeaders headers, UUID id, InvitationBO invitationBO) {
        ResponseUser requestUser = getResponseUserRestResponse(headers);
        Invitation orginal = this.repository.findById(id).orElseThrow(
                () -> new InvitationNotFoundException(id)
        );
        if(invitationPendingControl(invitationBO.user()))
            throw new IllegalArgumentException("Only one pending invitation can exist per user. Please confirm or cancel the existing pending invitation. For further assistance, feel free to reach out to our support team.");
        Invitation updateInvitation = updateInvitation(orginal, invitationBO, requestUser.id());
        Invitation save = this.repository.save(updateInvitation);
        return InvitationServiceMapper.convertToBO(save);
    }

    @Override
    public InvitationBO deleteByID(UUID id) {
        if (id != null) {
            InvitationBO bo = this.findByID(id);
            this.repository.deleteById(bo.id());
            return bo;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    public List<InvitationBO> findAllByUserID(UUID user) {
        if (user != null) {
            List<Invitation> allByUserId = this.repository.findByUser(user);
            if (allByUserId.isEmpty())
                throw new InvitationNotFoundException("We couldn't find any invitations for the user ID you searched for.");
            return InvitationServiceMapper.convertToBO(allByUserId);
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    public InvitationBO acceptInvitation(HttpHeaders headers, String invitationCode) {
        ResponseUser requestUser = getResponseUserRestResponse(headers);
        if (invitationCode != null && !invitationCode.isEmpty()) {
            Invitation po = this.repository.findByCode(invitationCode).orElseThrow(
                    () -> new InvitationNotFoundException(
                            String.format("The invitation (code: %s) you are looking for could not be found in the system."
                                    ,invitationCode)
                    )
            );
            if(po.getExpiredDate().isBefore(LocalDateTime.now())){
                po.setStatus(Status.EXPIRED);
                po.setUpdated(LocalDateTime.now());
                po.setUpdatedBy(requestUser.id());
                this.repository.save(po);
                throw new InvitationExpiredException();
            }
            po.setStatus(Status.ACCEPTED);
            po.setUpdated(LocalDateTime.now());
            po.setUpdatedBy(requestUser.id());
            Invitation update = this.repository.save(po);
            return InvitationServiceMapper.convertToBO(update);
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    public InvitationBO refreshInvitation(HttpHeaders headers, UUID invitation){
        ResponseUser requestUser = getResponseUserRestResponse(headers);
        if (invitation != null) {
            Invitation po = this.repository.findById(invitation).orElseThrow(
                    () -> new InvitationNotFoundException(invitation)
            );
            if(invitationPendingControl(po.getUser()))
                throw new IllegalArgumentException("Only one pending invitation can exist per user. Please confirm or cancel the existing pending invitation. For further assistance, feel free to reach out to our support team.");
            po.setStatus(Status.PENDING);
            po.setUpdatedBy(requestUser.id());
            po.setUpdated(LocalDateTime.now());
            po.setExpiredDate(LocalDateTime.now().plusWeeks(1));
            Invitation save = this.repository.save(po);
            return InvitationServiceMapper.convertToBO(save);
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void expireInvitations() {
        List<Invitation> allByExpiredDateBefore = this.repository.findAllByExpiredDateBeforeAndStatus(LocalDateTime.now(), Status.PENDING);
        if(!allByExpiredDateBefore.isEmpty()){
            allByExpiredDateBefore
                    .forEach(k-> k.setStatus(Status.EXPIRED));
            this.repository.saveAll(allByExpiredDateBefore);
        }
    }
    private Invitation updateInvitation(Invitation orginal, InvitationBO updateData, UUID user) {
        return new Invitation.Builder()
                .id(orginal.getId())
                .message(updateData.message()) // update dto
                .code(orginal.getCode())
                .status(updateData.status()) // update dto
                .user(updateData.user()) // update dto
                .createdDate(orginal.getCreated())
                .updatedDate(LocalDateTime.now())
                .createdBy(orginal.getCreatedBy())
                .updatedBy(user)
                .build();
    }
    private boolean invitationPendingControl(UUID user){
        List<InvitationBO> allByUserID = this.findAllByUserID(user);
        boolean control = allByUserID.stream().anyMatch(k -> k.status().equals(Status.PENDING));
        return control;
    }
    private ResponseUser getResponseUserRestResponse(HttpHeaders headers) {
        final String token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        FeignClientConfiguration.updateJwtSecret(token);
        final String email = jwtService.findByAccountEmail(token);
        final RestResponse<ResponseUser> response = userServiceClient.findByAttribute(null, email).getBody();
        return response.payload();
    }

    private ResponseUser getResponseUserRestResponse(HttpHeaders headers, UUID id) {
        final String token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        FeignClientConfiguration.updateJwtSecret(token);
        final RestResponse<ResponseUser> response = userServiceClient.findByAttribute(id, null).getBody();
        return response.payload();
    }

    private String codeGenarator(String userMail) {
        final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        final String hash = userMail.concat("_").concat(date);

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(hash.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            return UUID.randomUUID().toString();
        }
    }
}
