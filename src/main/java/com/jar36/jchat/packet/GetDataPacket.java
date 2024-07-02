package com.jar36.jchat.packet;

public class GetDataPacket extends Packet { // um? this is like RPC?
    private char subfunction; // function select
    // data body start
    private int id; // parameter used to pass parameter of ids (like sid, uid, gid ...)
    private Message[] messages;
    private Session[] sessions;
    // data body ends
    private long sessionToken; // use as return value
    @Override
    public Short getCommand() {
        return Command.DATA_POST;
    }

    public char getSubfunction() {
        return subfunction;
    }

    public void setSubfunction(char subfunction) {
        this.subfunction = subfunction;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public Session[] getSessions() {
        return sessions;
    }

    public void setSessions(Session[] sessions) {
        this.sessions = sessions;
    }

    public long getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(long sessionToken) {
        this.sessionToken = sessionToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
