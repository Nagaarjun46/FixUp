package com.example.fixup.apicalls.models;

public class LoginRequestModel {
    private String email;
    private String city;
    private String sessionId;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
