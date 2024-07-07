package com.jar36.jchat.client;

public class UserEvent {
    // this event is used to communicate between UI and netty handlers
    public static char LOGIN_TRIGGERED = 0x1;
    private char eventID;

    public UserEvent(char eventID) {
        this.eventID = eventID;
    }

    public char getEventID() {
        return eventID;
    }

    public void setEventID(char eventID) {
        this.eventID = eventID;
    }
}
