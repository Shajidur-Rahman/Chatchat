package com.example.whatsapp.Models;

public class Users {
    private String uid, name, phone_number, profileImage;

    public Users() {
    }

    public Users(String uid, String name, String phone_number, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.phone_number = phone_number;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
