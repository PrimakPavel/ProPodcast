package com.pavelprymak.propodcast.utils.otto;

public class EventUpdatePlayerView {
    private String trackTitle;
    private String trackImageUrl;
    private long trackDuration;
    private long trackCurrentPosition;
    private boolean isPlay;
    private boolean isLoad;

    public EventUpdatePlayerView() {
    }

    public EventUpdatePlayerView(String trackTitle, String trackImageUrl, long trackDuration, long trackCurrentPosition, boolean isPlay, boolean isLoad) {
        this.trackTitle = trackTitle;
        this.trackImageUrl = trackImageUrl;
        this.trackDuration = trackDuration;
        this.trackCurrentPosition = trackCurrentPosition;
        this.isPlay = isPlay;
        this.isLoad = isLoad;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getTrackImageUrl() {
        return trackImageUrl;
    }

    public void setTrackImageUrl(String trackImageUrl) {
        this.trackImageUrl = trackImageUrl;
    }

    public long getTrackDuration() {
        return trackDuration;
    }

    public void setTrackDuration(long trackDuration) {
        this.trackDuration = trackDuration;
    }

    public long getTrackCurrentPosition() {
        return trackCurrentPosition;
    }

    public void setTrackCurrentPosition(long trackCurrentPosition) {
        this.trackCurrentPosition = trackCurrentPosition;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }
}
