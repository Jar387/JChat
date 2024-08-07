package com.jar36.jchat.packet;

public class LoginRequestPacket extends Packet {
    private String JWTCode;
    private String username;
    private String passwdHash;
    private char subfunction;

    public String getPasswdHash() {
        return passwdHash;
    }

    public void setPasswdHash(String passwdHash) {
        this.passwdHash = passwdHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char getSubfunction() {
        return subfunction;
    }

    public void setSubfunction(char subfunction) {
        this.subfunction = subfunction;
    }

    @Override
    public Short getCommand() {
        return Command.LOGIN_REQUEST;
    }

    public String getJWTCode() {
        return JWTCode;
    }

    public void setJWTCode(String JWTCode) {
        this.JWTCode = JWTCode;
    }
}
