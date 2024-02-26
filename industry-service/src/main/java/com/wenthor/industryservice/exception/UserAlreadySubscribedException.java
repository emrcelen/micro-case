package com.wenthor.industryservice.exception;

import java.util.UUID;

public class UserAlreadySubscribedException extends RuntimeException{
    public UserAlreadySubscribedException(UUID user) {
        super(String.format("This user (%s) is already subscribed to the relevant industry. Therefore, the request was rejected.", user));
    }
}
