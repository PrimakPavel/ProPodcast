package com.pavelprymak.propodcast.utils.otto;

public class EventUpdatePlayerVisibility {
    private boolean isVisible;

    public EventUpdatePlayerVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
