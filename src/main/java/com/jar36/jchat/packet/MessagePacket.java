package com.jar36.jchat.packet;

public class MessagePacket extends Packet{
    private String msg;
    private String username;
    @Override
    public Short getCommand() {
        return Command.MESSAGE_REQUEST;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
