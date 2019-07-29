package com.pavelprymak.propodcast.utils.player;

public interface PlayerStateListener {
    void stateChanged();
    void isBuffed();
    void isReadyAndPlay();
    void isReadyAndPause();
    void isEnded();
}
