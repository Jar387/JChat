package com.jar36.jchat.packet;

public class Message {
    private String time; // java.lang.Date().toString();
    private String message; // message body
    private int uid; // user id

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
