package com.pavelprymak.propodcast.utils.otto;

public class EventUpdateDurationAndCurrentPos {
    private long trackCurrentPosition;
    private long trackDuration;

    public EventUpdateDurationAndCurrentPos(long trackCurrentPosition, long trackDuration) {
        this.trackCurrentPosition = trackCurrentPosition;
        this.trackDuration = trackDuration;
    }

    public long getTrackCurrentPosition() {
        return trackCurrentPosition;
    }

    public void setTrackCurrentPosition(long trackCurrentPosition) {
        this.trackCurrentPosition = trackCurrentPosition;
    }

    public long getTrackDuration() {
        return trackDuration;
    }

    public void setTrackDuration(long trackDuration) {
        this.trackDuration = trackDuration;
    }
}
