package com.wenthor.mapper;

import com.wenthor.bo.LoginResponseBO;
import com.wenthor.bo.UserBO;
import com.wenthor.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceMapper{

    public static UserBO convertToBO(User user) {
        return new UserBO(
                user.getId(),
                user.getFullName(),
                user.getNormalizedName(),
                user.getStatus(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getIndustries(),
                user.getAccountExpired(),
                user.getAccountLock(),
                user.getCredentialSlock(),
                user.getEnabled(),
                user.getBaseEntity().getCreated(),
                user.getBaseEntity().getUpdated(),
                user.getBaseEntity().getCreatedBy(),
                user.getBaseEntity().getUpdatedBy()
        );
    }
    public static User convertToPO(UserBO userBO) {
        return new User.Builder()
                .id(userBO.id())
                .fullName(userBO.fullName())
                .normalizedName(userBO.normalizedName())
                .status(userBO.status())
                .email(userBO.email())
                .password(userBO.password())
                .role(userBO.role())
                .industries(userBO.industries())
                .accountExpired(userBO.accountExpired())
                .accountLock(userBO.accountLock())
                .credentialSlock(userBO.credentialSlock())
                .enabled(userBO.enabled())
                .createdBy(userBO.createdBy())
                .updatedBy(userBO.updatedBy())
                .build();
    }

    public static List<UserBO> convertToBO(List<User> users){
        return users.stream()
                .map(UserServiceMapper::convertToBO)
                .collect(Collectors.toList());
    }

    public static LoginResponseBO convertToLoginBO(String mail,
                                                   String token,
                                                   long tokenIssuedTime,
                                                   long tokenExpirationTime){
        return new LoginResponseBO(mail,token,tokenIssuedTime,tokenExpirationTime);
    }
}
