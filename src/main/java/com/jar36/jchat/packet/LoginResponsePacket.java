package com.jar36.jchat.packet;

public class LoginResponsePacket extends Packet{
    private long sessionToken;
    @Override
    public Short getCommand() {
        return Command.LOGIN_RESPONSE;
    }
    public long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(long sessionToken) {
        this.sessionToken = sessionToken;
    }
}