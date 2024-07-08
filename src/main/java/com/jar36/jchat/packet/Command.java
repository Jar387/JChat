package com.jar36.jchat.packet;

public interface Command {
    // packet head command
    Short LOGIN_REQUEST = 0x1;
    Short LOGIN_RESPONSE = 0x2;
    Short DATA_GET = 0x3;
    Short DATA_POST = 0x4;

    // in-packet function select
    char LOGIN_REQUEST_SUBFUNCTION_LOGIN = 0x1;
    char LOGIN_REQUEST_SUBFUNCTION_CREATE_USER = 0x2;

    char LOGIN_REQUEST_SUBFUNCTION_RESPONSE_ERROR = 0x0;
    char LOGIN_REQUEST_SUBFUNCTION_RESPONSE_JWT_EXPIRED = 0xfe;
    char LOGIN_REQUEST_SUBFUNCTION_RESPONSE_SUCCESS = 0xff;

    char DATA_GET_SESSION_MESSAGE = 0x1;
    char DATA_GET_SESSION_LIST = 0x2;

    char DATA_POST_CREATE_SESSION = 0x1;
    char DATA_POST_SESSION_MESSAGE = 0x2;
}
