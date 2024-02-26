package com.wenthor.mapper;

import com.wenthor.bo.LoginResponseBO;
import com.wenthor.bo.UserBO;
import com.wenthor.dto.request.user.RegisterUser;
import com.wenthor.dto.request.user.UpdateUser;
import com.wenthor.dto.response.user.ResponseLoginUser;
import com.wenthor.dto.response.user.ResponseUser;
import com.wenthor.enumeration.Role;
import com.wenthor.enumeration.Status;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

public class UserControllerMapper {
    public static UserBO convertRegisterUserToBO(RegisterUser registerUser) {
        final UUID userID = UUID.randomUUID();
        return new UserBO(
                userID,
                registerUser.fullName(),
                null,
                Status.PENDING,
                registerUser.email(),
                registerUser.password(),
                Role.USER,
                new HashSet<UUID>(),
                false,
                false,
                false,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                userID,
                userID
        );
    }
    public static UserBO convertUpdateUserToBO(UpdateUser updateUser){
        return new UserBO(
                null,
                updateUser.fullName(),
                null,
                updateUser.status(),
                updateUser.email(),
                updateUser.password(),
                null,
                updateUser.industries(),
                updateUser.accountExpired(),
                updateUser.accountLock(),
                updateUser.credentialSlock(),
                false,
                null,
                LocalDateTime.now(),
                null,
                null
        );
    }
    public static ResponseUser convertToResponseUser(UserBO bo) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return new ResponseUser(
                bo.id(),
                bo.fullName(),
                bo.normalizedName(),
                bo.email(),
                bo.status(),
                dtf.format(bo.created()),
                dtf.format(bo.updated()),
                bo.createdBy(),
                bo.updatedBy()
        );
    }
    public static List<ResponseUser> convertToResponseUsers(List<UserBO> bos) {
        return bos.stream()
                .map(UserControllerMapper::convertToResponseUser)
                .collect(Collectors.toList());
    }
    public static ResponseLoginUser convertToResponseLoginUser(LoginResponseBO response){
        return new ResponseLoginUser(
                response.email(),
                response.token(),
                response.tokenIssuedTime(),
                response.tokenExpirationTime()
        );
    }
}
