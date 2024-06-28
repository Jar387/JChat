package com.jar36.jchat.packet;

public abstract class Packet {
    private static final Short version = 1; // ver 0.1

    public abstract Short getCommand();

    public Short getVersion() {
        return version;
    }
}
