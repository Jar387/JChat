package com.jar36.jchat.packet;

public class LoginRequestPacket extends Packet{
    private String username;
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    @Override
    public Short getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
