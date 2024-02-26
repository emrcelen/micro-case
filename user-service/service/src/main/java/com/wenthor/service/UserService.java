package com.wenthor.service;

import com.wenthor.IndustryServiceClient;
import com.wenthor.bo.LoginResponseBO;
import com.wenthor.bo.UserBO;
import com.wenthor.configuration.FeignClientConfiguration;
import com.wenthor.enumeration.Status;
import com.wenthor.exception.UserNotFoundException;
import com.wenthor.mapper.UserServiceMapper;
import com.wenthor.model.User;
import com.wenthor.repository.UserRepository;
import com.wenthor.response.IndustryResponse;
import com.wenthor.response.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IServiceMain<UserBO> {
    private final UserRepository repository;
    private final IndustryServiceClient industryServiceClient;
    private final VerificationCodeService verificationCodeService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public UserService(UserRepository repository,
                       IndustryServiceClient industryServiceClient,
                       VerificationCodeService verificationCodeService,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.industryServiceClient = industryServiceClient;
        this.verificationCodeService = verificationCodeService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public UserBO create(UserBO userBO) {
        logger.debug("Create User Request Data: {}", userBO);
        if (isUserEmailUnique(userBO.email()) && isFullNameValid(userBO.fullName())) {
            User po = new User.Builder()
                    .id(userBO.id())
                    .createdBy(userBO.createdBy())
                    .updatedBy(userBO.updatedBy())
                    .fullName(userBO.fullName())
                    .normalizedName(normalizeFullName(userBO.fullName()))
                    .status(userBO.status())
                    .email(userBO.email().toLowerCase())
                    .password(passwordEncoder.encode(userBO.password()))
                    .role(userBO.role())
                    .build();
            User userSave = this.repository.save(po);
            logger.debug("Create User Register Data: {}", userSave);
            this.verificationCodeService.create(UserServiceMapper.convertToBO(userSave));
            UserBO response = UserServiceMapper.convertToBO(userSave);
            logger.debug("Create User Response: {}", response);
            return response;
        } else if (!isFullNameValid(userBO.email()))
            throw new IllegalArgumentException("This email address is already in use, please try another email address.");
        throw new IllegalArgumentException("Unexpected values were sent during registration. Your request has been rejected.");
    }
    public LoginResponseBO login(String email, String password){
        UserBO bo = this.findByEmail(email);
        if(this.passwordEncoder.matches(password, bo.password())){
            final String jwt = this.jwtService.generateToken(bo.email());
            return UserServiceMapper.convertToLoginBO(
                    email,
                    jwt,
                    this.jwtService.tokenIssuedAt(jwt).getTime(),
                    this.jwtService.tokenExpirationTime(jwt).getTime());
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    public UserBO findByAttribute(UUID id, String email){
        if(id != null && email == null)
            return this.findByID(id);
        else if(email != null && !email.isEmpty() && id == null)
            return this.findByEmail(email);
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    @Override
    public UserBO findByID(UUID id) {
        logger.debug("User findByID request: {} ", id);
        if (id != null) {
            User po = this.repository.findById(id)
                    .orElseThrow(
                            () -> new UserNotFoundException(
                                    String.format(
                                            "The user (id: %s) you are looking for could not be found in the system."
                                            , id.toString()
                                    )
                            )
                    );
            logger.debug("User findByID data: {}", po);
            UserBO response = UserServiceMapper.convertToBO(po);
            logger.debug("User findByID response: {}", response);
            return response;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    public UserBO findByEmail(String email) {
        logger.debug("User findByEmail request: {} ", email);
        if (email != null && !email.isEmpty()) {
            User po = this.repository.findByEmail(email)
                    .orElseThrow(
                            () -> new UserNotFoundException(
                                    String.format(
                                            "The user (mail: %s) you are looking for could not be found in the system."
                                            , email
                                    )
                            )
                    );
            logger.debug("User findByEmail data: {}", po);
            UserBO response = UserServiceMapper.convertToBO(po);
            logger.debug("User findByEmail response: {}", response);
            return response;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    public List<UserBO> findAll(String normalizedName, int page, int size) {
        logger.debug("User findAll search: {} | page: {} | size: {}", normalizedName, page, size);
        Pageable pageable = PageRequest.of(page - 1, size);
        List<User> poList = this.repository.findByNormalizedNameContainingIgnoreCase(normalizedName, pageable).toList();
        if (!poList.isEmpty()) {
            List<UserBO> boList = UserServiceMapper.convertToBO(poList);
            return boList;
        }
        throw new UserNotFoundException("No users found matching the search values.");
    }

    public UserBO updateByAttribute(String token, UUID id, String email, UserBO userBO){
        if(id != null && email == null)
            return this.updateByID(token,id,userBO);
        else if(email != null && !email.isEmpty() && id == null)
            return this.updateByEmail(token,email,userBO);
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    @Override
    @Transactional
    public UserBO updateByID(String token, UUID id, UserBO updateBO) {
        if (id != null && updateBO != null) {
            logger.debug("User updateByID request id: {} ", id);
            logger.debug("User updateByID request body: {} ", updateBO);
            UserBO orginalBO = this.findByID(id);
            logger.debug("User updateByID orginal data: {}", orginalBO);
            if (updateUserControl(orginalBO, updateBO)) {
                User po = updateUser(token,orginalBO, updateBO);
                if (po.getEnabled())
                    this.verificationCodeService.create(UserServiceMapper.convertToBO(po));
                User updatePO = this.repository.save(po);
                logger.debug("User updateByID update data: {}", po);
                UserBO response = UserServiceMapper.convertToBO(updatePO);
                logger.debug("User updateByID response data: {}", response);
                return response;
            }
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    @Transactional
    public UserBO updateByEmail(String token, String email, UserBO updateBO) {
        if (email != null && !email.isEmpty() && updateBO != null) {
            logger.debug("User updateByEmail request email: {} ", email);
            logger.debug("User updateByEmail request body: {} ", updateBO);
            UserBO orginalBO = this.findByEmail(email);
            logger.debug("User updateByEmail orginal data: {}", orginalBO);
            if (updateUserControl(orginalBO, updateBO)) {
                User po = updateUser(token,orginalBO, updateBO);
                if (po.getEnabled())
                    this.verificationCodeService.create(UserServiceMapper.convertToBO(po));
                User updatePO = this.repository.save(po);
                logger.debug("User updateByEmail update data: {}", po);
                UserBO response = UserServiceMapper.convertToBO(updatePO);
                logger.debug("User updateByEmail response data: {}", response);
                return response;
            }
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    protected void update(UserBO userBO){
        logger.debug("User update request body: {} ", userBO);
        User po = UserServiceMapper.convertToPO(userBO);
        this.repository.save(po);
        logger.debug("User update data: {}", po);
    }

    public UserBO deleteByAttribute(UUID id, String email){
        if(id != null && email == null)
            return this.deleteByID(id);
        else if(email != null && !email.isEmpty() && id == null)
            return this.deleteByEmail(email);
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    @Override
    public UserBO deleteByID(UUID id) {
        logger.debug("User deleteByID request: {} ", id);
        if(id != null){
            UserBO bo = this.findByID(id);
            User po = UserServiceMapper.convertToPO(bo);
            logger.debug("User deleteByID delete data: {}", po);
            this.repository.delete(po);
            logger.debug("User deleteByID response: {}", bo);
            return bo;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    public UserBO deleteByEmail(String email) {
        logger.debug("User deleteByID request: {} ", email);
        if(email != null){
            UserBO bo = this.findByEmail(email);
            User po = UserServiceMapper.convertToPO(bo);
            logger.debug("User deleteByID delete data: {}", po);
            this.repository.delete(po);
            logger.debug("User deleteByID response: {}", bo);
            return bo;
        }
        throw new IllegalArgumentException("Your request has been rejected.");
    }
    public UserBO subscribeIndustry(String token, UUID user, UUID industry, boolean client){
        String byAccountEmail = this.jwtService.findByAccountEmail(token);
        UserBO tokenUser = this.findByEmail(byAccountEmail);
        User subscribeUser = this.repository.findById(user)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format(
                                        "The user (id: %s) you are looking for could not be found in the system."
                                        , user.toString()
                                )
                        )
                );
        FeignClientConfiguration.updateJwtSecret(token);
        final IndustryResponse response = industryServiceClient.findByID(industry).getBody().payload();
        if(!industry.equals(response.id()))
            throw new IllegalArgumentException("Your request has been rejected.");
        subscribeUser.getBaseEntity().setUpdated(LocalDateTime.now());
        subscribeUser.getBaseEntity().setUpdatedBy(tokenUser.id());
        subscribeUser.getIndustries().add(response.id());
        User saveUser = this.repository.save(subscribeUser);
        if(!client){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer ".concat(token));
            industryServiceClient.subsribeIndustry(headers,industry,user);
        }
        UserBO bo = UserServiceMapper.convertToBO(saveUser);
        return bo;
    }
    public UserBO unSubscribeIndustry(String token, UUID user, UUID industry, boolean client){
        String byAccountEmail = this.jwtService.findByAccountEmail(token);
        UserBO tokenUser = this.findByEmail(byAccountEmail);
        User unSubscribeUser = this.repository.findById(user)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format(
                                        "The user (id: %s) you are looking for could not be found in the system."
                                        , user.toString()
                                )
                        )
                );
        UUID control = unSubscribeUser.getIndustries().stream().filter(k -> k.equals(industry)).findFirst().orElse(null);
        if(control == null)
            throw new IllegalArgumentException("Your request has been rejected.");
        unSubscribeUser.getBaseEntity().setUpdated(LocalDateTime.now());
        unSubscribeUser.getBaseEntity().setUpdatedBy(tokenUser.id());
        unSubscribeUser.getIndustries().remove(industry);
        User saveUser = this.repository.save(unSubscribeUser);
        if(!client){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer ".concat(token));
            industryServiceClient.unSubsribeIndustry(headers,industry,user);
        }
        UserBO bo = UserServiceMapper.convertToBO(saveUser);
        return bo;
    }

    private User updateUser(String token, UserBO orginalBO, UserBO updateBO){
        final String byAccountEmail = this.jwtService.findByAccountEmail(token);
        final UserBO bo = this.findByEmail(byAccountEmail);
        User po = new User.Builder()
                .id(orginalBO.id())
                .createdBy(orginalBO.createdBy())
                .updatedBy(bo.id())
                .fullName(updateBO.fullName())
                .normalizedName(normalizeFullName(updateBO.fullName()))
                .status(orginalBO.email().equals(updateBO.email()) ? updateBO.status() : Status.PENDING)
                .email(updateBO.email().toLowerCase())
                .password(passwordEncoder.encode(updateBO.password()))
                .role(orginalBO.role())
                .industries(updateBO.industries())
                .accountLock(updateBO.accountLock())
                .accountExpired(updateBO.accountExpired())
                .credentialSlock(updateBO.credentialSlock())
                .enabled(!orginalBO.email().equalsIgnoreCase(updateBO.email()) && !orginalBO.enabled() ? false :
                        orginalBO.email().equalsIgnoreCase(updateBO.email()) ? orginalBO.enabled() : false)
                .build();
        po.getBaseEntity().setUpdated(LocalDateTime.now());
        return po;
    }
    private boolean updateUserControl(UserBO orginal, UserBO update) {
        if (orginal.email().equalsIgnoreCase(update.email()) || isUserEmailUnique(update.email()))
            return true;
        return false;
    }
    private boolean isUserEmailUnique(String email) {
        boolean present = this.repository.findByEmail(email).isPresent();
        return !present;
    }
    private boolean isFullNameValid(String fullName) {
        return fullName.matches("^[a-zA-Z ]+$");
    }
    private String normalizeFullName(String fullName) {
        final String normalized = fullName.trim().toLowerCase()
                .replace("ı", "i")
                .replace("ö", "o")
                .replace("ü", "u")
                .replace("ş", "s")
                .replace("ğ", "g")
                .replace("ç", "c")
                .replace(" ", "")
                .replaceAll("[^a-z]", ""); // Özel karakterleri kaldır
        return normalized;
    }

}
