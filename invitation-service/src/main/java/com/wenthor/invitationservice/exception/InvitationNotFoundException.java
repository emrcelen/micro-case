package com.wenthor.invitationservice.exception;

import java.util.UUID;

public class InvitationNotFoundException extends RuntimeException{
    public InvitationNotFoundException(UUID invitation) {
        super(String.format("The invitation (id: %s) you are looking for could not be found in the system.",invitation.toString()));
    }
    public InvitationNotFoundException(String message){
        super(message);
    }
}
