package com.pavelprymak.propodcast.utils.otto;

public class EventUpdateLoading {
    boolean isLoading;

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
