package com.wenthor.model;

import java.io.Serializable;

public class Notification implements Serializable {
    private final String userFullName;
    private final String userMail;
    private final String verificationCode;
    private final String verificationURL;

    public Notification(){
        this.userFullName = null;
        this.userMail = null;
        this.verificationURL = null;
        this.verificationCode = null;
    }
    public Notification(Builder builder){
        this.userFullName = builder.fullName;
        this.userMail = builder.userMail;
        this.verificationCode = builder.verificationCode;
        this.verificationURL = builder.verificationURL;
    }

    public String getUserFullName() {
        return userFullName;
    }
    public String getUserMail() {
        return userMail;
    }
    public String getVerificationCode() {
        return verificationCode;
    }
    public String getVerificationURL() {
        return verificationURL;
    }

    public static class Builder {
        private String fullName;
        private String userMail;
        private String verificationCode;
        private String verificationURL;

        public Builder fullName(String fullName){
            this.fullName = fullName;
            return this;
        }
        public Builder mail(String userMail){
            this.userMail = userMail;
            return this;
        }
        public Builder code(String verificationCode){
            this.verificationCode = verificationCode;
            return this;
        }
        public Builder url(String verificationURL){
            this.verificationURL = verificationURL;
            return this;
        }
        public Notification build(){
            return new Notification(this);
        }
    }
}
