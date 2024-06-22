package com.jar36.jchat.packet;

public interface Command {
    Short LOGIN_REQUEST = 0x1;
    Short LOGIN_RESPONSE = 0x2;
    Short LOGOUT_REQUEST = 0x3;
}
