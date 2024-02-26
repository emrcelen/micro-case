package com.wenthor.industryservice.service;

import com.wenthor.industryservice.client.UserServiceClient;
import com.wenthor.industryservice.client.configuration.FeignClientConfiguration;
import com.wenthor.industryservice.controller.dto.response.rest.RestResponse;
import com.wenthor.industryservice.client.response.user.ResponseUser;
import com.wenthor.industryservice.exception.IndustryNameNotAlphaNumericException;
import com.wenthor.industryservice.exception.IndustryNameNotUniqueException;
import com.wenthor.industryservice.exception.IndustryNotFoundException;
import com.wenthor.industryservice.exception.UserAlreadySubscribedException;
import com.wenthor.industryservice.model.Industry;
import com.wenthor.industryservice.repository.IndustryRepository;
import com.wenthor.industryservice.service.bo.BasicIndustryBO;
import com.wenthor.industryservice.service.bo.IndustryBO;
import com.wenthor.industryservice.service.bo.IndustryCreateUpdateBO;
import com.wenthor.industryservice.service.mapper.IndustryServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class IndustryService implements IServiceMain<IndustryBO, IndustryCreateUpdateBO> {
    private final IndustryRepository repository;
    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public IndustryService(IndustryRepository repository,
                           JwtService jwtService,
                           UserServiceClient userServiceClient) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
    }


    @Override
    public IndustryBO create(HttpHeaders headers, IndustryCreateUpdateBO bo) {
        if (isIndustryNameUnique(bo.name()) && isIndustryNameAlphanumeric(bo.name())) {
            logger.debug("Create Request: {}", bo);
            final ResponseUser user = getResponseUserRestResponse(headers);
            logger.debug("Owner: {}", user);
            Industry industry = new Industry.Builder()
                    .updatedBy(user.id())
                    .createdBy(user.id())
                    .name(bo.name())
                    .normalizedName(normalizedName(bo.name()))
                    .parentIndustry(null)
                    .subIndustries(new ArrayList<>())
                    .build();
            Industry po = this.repository.save(industry);
            IndustryBO response = IndustryServiceMapper.convertToBO(po);
            logger.debug("Response: {}", response);
            return response;
        } else if (!isIndustryNameUnique(bo.name()))
            throw new IndustryNameNotUniqueException(bo.name());
        else if (!isIndustryNameAlphanumeric(bo.name()))
            throw new IndustryNameNotAlphaNumericException();
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    @Override
    public IndustryBO findByID(UUID id) {
        if (id != null) {
            Industry po = this.repository.findById(id)
                    .orElseThrow(
                            () -> new IndustryNotFoundException(
                                    String.format("The industry (id: %s) you are looking for could not be found in the system."
                                            , id.toString())
                            )
                    );
            IndustryBO bo = IndustryServiceMapper.convertToBO(po);
            return bo;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    private List<Industry> findAllByID(Set<UUID> industries) {
        return this.repository.findAllByIdIn(industries).orElse(new ArrayList<>());
    }

    @Override
    public IndustryBO updateByID(HttpHeaders headers, UUID id, IndustryCreateUpdateBO bo) {
        if (id != null) {
            IndustryBO orginal = this.findByID(id);
            if ((orginal.name().equalsIgnoreCase(bo.name())) ||
                    (isIndustryNameUnique(bo.name()) && isIndustryNameAlphanumeric(bo.name()))) {
                logger.debug("Update Request ID: {} Body: {}", id, bo);
                final ResponseUser user = getResponseUserRestResponse(headers);
                logger.debug("Requester: {}", user.id());
                Industry convertUpdate = this.updateIndustry(orginal, bo, user.id());
                convertUpdate.getSubIndustries().forEach(k -> k.setParentIndustry(convertUpdate));
                Industry updatePO = this.repository.save(convertUpdate);
                IndustryBO response = IndustryServiceMapper.convertToBO(updatePO);
                logger.info("Response: {}", response);
                return response;
            } else if (!isIndustryNameAlphanumeric(bo.name()))
                throw new IndustryNameNotAlphaNumericException();
            else if (!orginal.name().equalsIgnoreCase(bo.name()) && !isIndustryNameUnique(bo.name()))
                throw new IndustryNameNotUniqueException(bo.name());
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    public IndustryBO updateIndustryHierarchy(HttpHeaders headers, UUID main, UUID parent, UUID sub) {
        if (main != null) {
            Industry orginal = this.repository.findById(main)
                    .orElseThrow(
                            () -> new IndustryNotFoundException(
                                    String.format("The industry (id: %s) you are looking for could not be found in the system."
                                            , main.toString())
                            )
                    );
            final ResponseUser user = getResponseUserRestResponse(headers);
            if (parent != null && sub == null)
                return this.updateParentIndustry(orginal, parent, user.id());
            else if (sub != null && parent == null)
                return this.addSubIndustry(orginal, sub, user.id());
            else
                throw new IllegalArgumentException("Your request has been rejected.");
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    private IndustryBO updateParentIndustry(Industry industry, UUID id, UUID user) {
        Industry parent = this.repository.findById(id)
                .orElseThrow(
                        () -> new IndustryNotFoundException(
                                String.format("The parent industry (id: %s) you are looking for could not be found in the system.",
                                        id.toString())
                        )
                );
        industry.setParentIndustry(parent);
        industry.setUpdated(LocalDateTime.now());
        industry.setUpdatedBy(user);
        Industry po = this.repository.save(industry);
        IndustryBO response = IndustryServiceMapper.convertToBO(po);
        return response;
    }

    @Transactional
    private IndustryBO addSubIndustry(Industry industry, UUID id, UUID user) {
        Industry sub = this.repository.findById(id)
                .orElseThrow(
                        () -> new IndustryNotFoundException(
                                String.format("The sub industry (id: %s) you are looking for could not be found in the system.",
                                        id.toString())
                        )
                );
        final boolean isAlreadyAdded = industry.getSubIndustries().stream().anyMatch(i -> i.getId().equals(sub.getId()));
        final boolean checkRelotionship = industry.getSubIndustries().stream().anyMatch(i -> i.getParentIndustry().getId().equals(sub.getId()));
        if (isAlreadyAdded)
            throw new IllegalArgumentException("The sub-industry already exists.");
        if (checkRelotionship)
            throw new IllegalArgumentException("You cannot add your parent industry as a sub-industry.");
        industry.getSubIndustries().add(sub);
        industry.setUpdatedBy(user);
        industry.setUpdated(LocalDateTime.now());
        Industry po = this.repository.save(industry);
        sub.setParentIndustry(industry);
        sub.setUpdated(LocalDateTime.now());
        sub.setUpdatedBy(user);
        this.repository.save(sub);
        IndustryBO response = IndustryServiceMapper.convertToBO(po);
        return response;
    }

    @Override
    @Transactional
    public void deleteByID(UUID id) {
        if (id != null) {
            IndustryBO bo = this.findByID(id);
            if (bo.parentIndustry() != null) {
                Industry parent = this.repository.findById(bo.parentIndustry().id()).get();
                Industry sub = parent.getSubIndustries().stream()
                        .filter(k -> k.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                parent.getSubIndustries().remove(sub);
                this.repository.save(parent);
            }
            if (!bo.subIndustries().isEmpty()) {
                List<UUID> industries = bo.subIndustries().stream().map(BasicIndustryBO::id).toList();
                this.repository.deleteAllById(industries);
              /*  List<Industry> subIndustries = this.repository.findAllById(industries);
                subIndustries.forEach(k-> k.setParentIndustry(null));
                this.repository.saveAll(subIndustries);*/
            }
            this.repository.deleteById(id);
            return;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }

    public IndustryBO subscribeIndustry(HttpHeaders headers, UUID industryID, UUID userID) {
        final String token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        FeignClientConfiguration.updateJwtSecret(token);
        final ResponseUser requestUser = getResponseUserRestResponse(headers);
        final ResponseUser subscribeUser = userServiceClient.findByAttribute(userID, null).getBody().payload();
        if(subscribeUser.id().equals(userID)){
            Industry industry = this.repository.findById(industryID)
                    .orElseThrow(
                            () -> new IndustryNotFoundException(
                                    String.format("The industry (id: %s) you are looking for could not be found in the system."
                                            , industryID.toString())
                            )
                    );
            boolean control = industry.getUsers().stream().anyMatch(k -> k.equals(subscribeUser.id()));
            if(control)
                throw new UserAlreadySubscribedException(subscribeUser.id());
            industry.getUsers().add(subscribeUser.id());
            industry.setUpdatedBy(requestUser.id());
            industry.setUpdated(LocalDateTime.now());
            userServiceClient.subscribeIndustry(headers,userID,industryID,true);
            Industry save = this.repository.save(industry);
            return IndustryServiceMapper.convertToBO(save);
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    public IndustryBO unSubscribeIndustry(HttpHeaders headers, UUID industryID, UUID userID){
        ResponseUser user = getResponseUserRestResponse(headers);
        Industry industry = this.repository.findById(industryID)
                .orElseThrow(
                        () -> new IndustryNotFoundException(
                                String.format("The industry (id: %s) you are looking for could not be found in the system."
                                        , industryID.toString())
                        )
                );
        UUID uuid = industry.getUsers().stream().filter(k -> k.equals(userID)).findFirst().orElse(null);
        if(uuid == null)
            throw new IllegalArgumentException("Your request has been rejected.");
        industry.getUsers().remove(uuid);
        industry.setUpdatedBy(user.id());
        industry.setUpdated(LocalDateTime.now());
        Industry save = this.repository.save(industry);
        userServiceClient.unSubscribeIndustry(headers,userID,industryID,true);
        return IndustryServiceMapper.convertToBO(save);
    }

    private ResponseUser getResponseUserRestResponse(HttpHeaders headers) {
        final String token = headers.get(headers.AUTHORIZATION).get(0).substring(7);
        FeignClientConfiguration.updateJwtSecret(token);
        final String email = jwtService.findByAccountEmail(token);
        final RestResponse<ResponseUser> response = userServiceClient.findByAttribute(null, email).getBody();
        return response.payload();
    }

    private Industry updateIndustry(IndustryBO orginal, IndustryCreateUpdateBO update, UUID user) {
        final String name = update.name() != null ? update.name() : orginal.name();
        Industry parentIndustry = update.parentIndustry() != null ?
                this.repository.findById(update.parentIndustry())
                        .orElseThrow(
                                () -> new IndustryNotFoundException(
                                        String.format("The sub industry (id: %s) you are looking for could not be found in the system.",
                                                update.parentIndustry().toString())
                                )
                        ) : orginal.parentIndustry() != null ? this.repository.findById(orginal.parentIndustry().id())
                .orElseThrow(
                        () -> new IndustryNotFoundException(
                                String.format("The sub industry (id: %s) you are looking for could not be found in the system.",
                                        orginal.parentIndustry().id().toString())
                        )
                ) : null;

        List<Industry> subIndustries = !update.subIndustries().isEmpty() ? this.findAllByID(update.subIndustries()) : this.findAllByID(orginal.subIndustries()
                .stream().map(BasicIndustryBO::id).collect(Collectors.toSet()));
        System.err.println(subIndustries);
        return new Industry.Builder()
                .id(orginal.id())
                .createdBy(orginal.createdBy())
                .updatedBy(user)
                .updatedDate(LocalDateTime.now())
                .name(name)
                .normalizedName(normalizedName(name))
                .subIndustries(subIndustries)
                .parentIndustry(parentIndustry)
                .build();
    }

    private boolean isIndustryNameUnique(String name) {
        boolean present = this.repository.findByName(name).isPresent();
        return !present;
    }

    private boolean isIndustryNameAlphanumeric(String name) {
        return name.matches("[a-zA-Z0-9]+$");
    }

    private String normalizedName(String name) {
        final String normalized = name.trim().toLowerCase()
                .replace("ı", "i")
                .replace("ö", "o")
                .replace("ü", "u")
                .replace("ş", "s")
                .replace("ğ", "g")
                .replace("ç", "c")
                .replace(" ", "")
                .replaceAll("[^a-z0-9]", ""); // Özel karakterleri kaldır
        return normalized;
    }
}
