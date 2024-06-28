package com.jar36.jchat.packet;

public class SessionPacket extends Packet {
    @Override
    public Short getCommand() {
        return Command.SESSION_REQUEST;
    }
}
