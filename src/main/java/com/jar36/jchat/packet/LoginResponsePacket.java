package com.jar36.jchat.packet;

public class LoginResponsePacket extends Packet{
    private long sessionToken;
    private String reason;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
