package com.example.fixup.models;

public class UserDetailsModel {
    public String userName;
    public String userEmail;
    public String userContactNo;
    public String userState;
    public String userCity;
    public String userPassword;

    public UserDetailsModel(String userName, String userEmail, String userContactNo,
                            String userState, String userCity, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userContactNo = userContactNo;
        this.userState = userState;
        this.userCity = userCity;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserContactNo() {
        return userContactNo;
    }

    public String getUserState() {
        return userState;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserPassword() {
        return userPassword;
    }
}