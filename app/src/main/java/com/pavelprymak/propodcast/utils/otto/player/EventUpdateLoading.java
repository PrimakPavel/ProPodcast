package com.pavelprymak.propodcast.utils.otto.player;

public class EventUpdateLoading {
    private boolean isLoading;

    public EventUpdateLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
