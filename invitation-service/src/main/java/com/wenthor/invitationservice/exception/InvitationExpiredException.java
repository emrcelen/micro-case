package com.wenthor.invitationservice.exception;

public class InvitationExpiredException extends RuntimeException{
    public InvitationExpiredException() {
        super("Event invitation has expired. If you would like to request a new invitation, please contact us.");
    }
}
