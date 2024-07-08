package com.jar36.jchat.packet;

public class LoginResponsePacket extends Packet {
    private String reason;
    private char subfunction;
    private String JWTCode;

    @Override
    public Short getCommand() {
        return Command.LOGIN_RESPONSE;
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

    public String getJWTCode() {
        return JWTCode;
    }

    public void setJWTCode(String JWTCode) {
        this.JWTCode = JWTCode;
    }
}
