package com.jar36.jchat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

public class User {
    public static ArrayList<User> users = new ArrayList<>();
    private String name;
    private long sessionToken;
    private String ip;
    private Channel channel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
