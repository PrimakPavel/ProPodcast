package com.pavelprymak.propodcast.utils.otto.player;

public class EventPlayerError {
    private int errorCode;

    public EventPlayerError(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
