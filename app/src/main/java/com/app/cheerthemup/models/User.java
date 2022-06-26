package com.app.cheerthemup.models;

public class User {

    private String userName;
    private String anonymousUser;
    private String email;
    private String phone;
    private boolean isProfileSetupDone;
    private String uid;
    private boolean isPaidUser;
    private boolean isIdentityHidden;


    public String getAnonymousUser() {
        return anonymousUser;
    }

    public void setAnonymousUser(String anonymousUser) {
        this.anonymousUser = anonymousUser;
    }

    public User(String userName, String anonymousUser, String email, String phone, boolean isProfileSetupDone, String uid, boolean isPaidUser, boolean isIdentityHidden) {
        this.userName = userName;
        this.anonymousUser = anonymousUser;
        this.email = email;
        this.phone = phone;
        this.isProfileSetupDone = isProfileSetupDone;
        this.uid = uid;
        this.isPaidUser = isPaidUser;

        this.isIdentityHidden = isIdentityHidden;
    }

    public boolean isIdentityHidden() {
        return isIdentityHidden;
    }

    public void setIdentityHidden(boolean identityHidden) {
        isIdentityHidden = identityHidden;
    }

    public boolean isPaidUser() {
        return isPaidUser;
    }

    public void setPaidUser(boolean paidUser) {
        isPaidUser = paidUser;
    }

//    public User(String userName, String email, String phone, boolean isProfileSetupDone, String uid, boolean isPaidUser, String password) {
//        this.userName = userName;
//        this.email = email;
//        this.phone = phone;
//        this.isProfileSetupDone = isProfileSetupDone;
//        this.uid = uid;
//        this.isPaidUser = isPaidUser;
//        this.password = password;
//    }

    private String password;

//    public User(String userName, String email, String phone, boolean isProfileSetupDone, String uid) {
//        this.userName = userName;
//        this.email = email;
//        this.phone = phone;
//        this.isProfileSetupDone = isProfileSetupDone;
//        this.uid = uid;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isProfileSetupDone() {
        return isProfileSetupDone;
    }

    public void setProfileSetupDone(boolean profileSetupDone) {
        isProfileSetupDone = profileSetupDone;
    }

//    public User(String userName, String email, String phone, boolean isProfileSetupDone) {
//        this.userName = userName;
//        this.email = email;
//        this.phone = phone;
//        this.isProfileSetupDone = isProfileSetupDone;
//    }

//    public User(String userName, String email, String phone) {
//        this.userName = userName;
//        this.email = email;
//        this.phone = phone;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


//    public User(String email, String password, String phone, String userName) {
//        this.email = email;
//        this.password = password;
//        this.phone = phone;
//        this.userName = userName;
//    }

//    public User(String userName, String password, String phone) {
//        this.userName = userName;
//        this.password = password;
//        this.phone = phone;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public User(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }


    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


