package com.example.bargainbuddy;

public class User {

    private String name;
    private String email;
    private String uid;
    private boolean isBussiness; // for businesses True, for users False;
    private String website;

    public User() {
    }

    public User(String name, String email, String uid, boolean role, String website) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.isBussiness = role;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isBussiness() {
        return isBussiness;
    }

    public void setRole(boolean isBussiness) {
        this.isBussiness = isBussiness;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
