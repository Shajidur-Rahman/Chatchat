package com.example.whatsapp.Models;

import java.util.ArrayList;

public class UserStatus {
    private String name, profileImage;
    private long lastUploaded;
    private ArrayList<Status> statuses;

    public UserStatus() {

    }

    public UserStatus(String name, String profileImage, long lastUploaded, ArrayList<Status> statuses) {
        this.name = name;
        this.profileImage = profileImage;
        this.lastUploaded = lastUploaded;
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastUploaded() {
        return lastUploaded;
    }

    public void setLastUploaded(long lastUploaded) {
        this.lastUploaded = lastUploaded;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
