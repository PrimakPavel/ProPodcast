package com.pavelprymak.propodcast.services;

public interface PlayerUI {
    // User Actions
    void playAction();

    void pauseAction();

    void stopAction();

    void startTrackAction(String trackTitle, String imageUrl, String trackLink);

    void updateUiAction();

    void seekToPositionAction(long seekPosition);


    // UI preparing
    void setPlayStatus(boolean isPlay);

    void setLoadingStatus(boolean isLoading);

    void setPlayerErrors(String errorMessage);

    void setPlaybackDuration(long duration);

    void setPlaybackCurrentPosition(long currentPosition, long duration);

    void setTrackTitle(String trackTitle);

    void setTrackImage(String imageUrl);
}
