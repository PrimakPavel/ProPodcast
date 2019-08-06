package com.pavelprymak.propodcast.utils.otto.player;

public class EventUpdatePlayPauseBtn {

    public EventUpdatePlayPauseBtn(boolean isPlay) {
        this.isPlay = isPlay;
    }

    private boolean isPlay;



    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
