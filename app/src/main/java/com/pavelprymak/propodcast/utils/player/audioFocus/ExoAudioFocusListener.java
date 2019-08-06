package com.pavelprymak.propodcast.utils.player.audioFocus;

import android.media.AudioManager;

import com.google.android.exoplayer2.SimpleExoPlayer;

import timber.log.Timber;

public class ExoAudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
    private static final float MEDIA_VOLUME_DEFAULT = 1.0f;
    private static final float MEDIA_VOLUME_DUCK = 0.2f;
    private final SimpleExoPlayer player;

    public ExoAudioFocusListener(SimpleExoPlayer player) {
        this.player = player;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (player != null) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (!player.getPlayWhenReady()) {
                        player.setPlayWhenReady(true);
                    }
                    player.setVolume(MEDIA_VOLUME_DEFAULT);
                    Timber.d("Player = " + player.toString() + "AudioManager.AUDIOFOCUS_GAIN");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    player.setVolume(MEDIA_VOLUME_DUCK);
                    Timber.d("Player = " + player.toString() + "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //pause player
                    player.setPlayWhenReady(false);
                    Timber.d("Player = " + player.toString() + "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //stop
                    player.setPlayWhenReady(false);
                    Timber.d("Player = " + player.toString() + "AudioManager.AUDIOFOCUS_LOSS");
                    break;
            }
        }
    }
}
