package com.pavelprymak.propodcast.services

interface PlayerUI {
    // User Actions
    fun playAction()

    fun pauseAction()

    fun stopAction()

    fun startTrackAction(trackLink: String?, trackTitle: String?, imageUrl: String?, trackAuthor: String?)

    fun updateUiAction()

    fun seekToPositionAction(percentSeekPosition: Float)


    // UI preparing
    fun setPlayStatus(isPlay: Boolean)

    fun setLoadingStatus(isLoading: Boolean)

    fun setPlayerErrors(errorMessage: String)

    fun setPlaybackDuration(duration: Long)

    fun setPlaybackCurrentPosition(currentPosition: Long, duration: Long)

    fun setTrackTitle(trackTitle: String?)

    fun setTrackImage(imageUrl: String?)
}
