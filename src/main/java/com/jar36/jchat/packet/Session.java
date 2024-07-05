package com.jar36.jchat.packet;

public class Session {
    // TODO: replace this with ArrayList after groupchat function is implemented
    String name; // session title name
    private int uid1; // one of session users
    private int uid2; // another session user

    public int getUid1() {
        return uid1;
    }

    public void setUid1(int uid1) {
        this.uid1 = uid1;
    }

    public int getUid2() {
        return uid2;
    }

    public void setUid2(int uid2) {
        this.uid2 = uid2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
