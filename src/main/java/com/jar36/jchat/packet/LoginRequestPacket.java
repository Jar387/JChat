package com.jar36.jchat.packet;

public class LoginRequestPacket extends Packet{
    private String username;
    private String passwdHash;
    private String oldPasswdHash;
    private char subfunction;

    public String getPasswdHash() {
        return passwdHash;
    }

    public void setPasswdHash(String passwdHash) {
        this.passwdHash = passwdHash;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
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

    public String getOldPasswdHash() {
        return oldPasswdHash;
    }

    public void setOldPasswdHash(String oldPasswdHash) {
        this.oldPasswdHash = oldPasswdHash;
    }
}
