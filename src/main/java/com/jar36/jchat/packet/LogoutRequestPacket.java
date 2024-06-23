package com.jar36.jchat.packet;

@Deprecated
public class LogoutRequestPacket extends Packet{
    private long sessionToken;
    @Override
    public Short getCommand() {
        return Command.LOGOUT_REQUEST;
    }
    public long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(long sessionToken) {
        this.sessionToken = sessionToken;
    }
}
