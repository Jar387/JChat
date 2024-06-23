package com.jar36.jchat.server;

import com.jar36.jchat.server.data.UserData;
import io.netty.channel.Channel;

import java.util.ArrayList;

public class User {
    public static final ArrayList<User> users = new ArrayList<>();
    private UserData userData;
    private long sessionToken;
    private String ip;
    private Channel channel;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(long sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
