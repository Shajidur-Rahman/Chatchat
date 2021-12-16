package com.example.whatsapp.Models;

public class Status {
    private String imgUrl;
    private long timeStamp;

    public Status() {

    }

    public Status(String imgUrl, long timeStamp) {
        this.imgUrl = imgUrl;
        this.timeStamp = timeStamp;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
