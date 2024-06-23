package com.jar36.jchat.packet;

public class LoginResponsePacket extends Packet{
    private long sessionToken;
    private String reason;
    private char subfunction;
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

    public char getSubfunction() {
        return subfunction;
    }

    public void setSubfunction(char subfunction) {
        this.subfunction = subfunction;
    }
}
