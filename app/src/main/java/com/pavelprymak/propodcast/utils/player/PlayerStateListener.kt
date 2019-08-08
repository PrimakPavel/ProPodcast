package com.pavelprymak.propodcast.utils.player

interface PlayerStateListener {
    fun stateChanged()
    fun isBuffed()
    fun isReadyAndPlay()
    fun isReadyAndPause()
    fun isEnded()
}
