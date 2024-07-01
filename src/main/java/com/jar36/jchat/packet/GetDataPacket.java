package com.jar36.jchat.packet;

public class GetDataPacket extends Packet {
    private char subfunction;
    private Message[] messages;
    private Session[] sessions;
    private long sessionToken;
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
}
