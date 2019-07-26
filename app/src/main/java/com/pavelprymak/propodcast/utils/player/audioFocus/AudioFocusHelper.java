package com.pavelprymak.propodcast.utils.player.audioFocus;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AudioFocusHelper {
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener;
    private AudioFocusRequest mAudioFocusRequest;

    public AudioFocusHelper(Context context, AudioManager.OnAudioFocusChangeListener audioFocusChangeListener) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioFocusListener = audioFocusChangeListener;
    }

    public boolean requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return requestAudioFocusO();
        } else {
            return requestAudioFocusBase();
        }
    }

    public void abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            abandonAudioFocusO();
        } else {
            abandonAudioFocusBase();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean requestAudioFocusO() {
        if (mAudioManager != null && mAudioFocusListener != null) {
            AudioAttributes mAudioAttributes =
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
            mAudioFocusRequest =
                    new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                            .setAudioAttributes(mAudioAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(mAudioFocusListener) // Need to implement instance
                            .build();

            int focusRequest = mAudioManager.requestAudioFocus(mAudioFocusRequest);
            switch (focusRequest) {
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    // don’t start playback
                    return false;
                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                    return true;
                // actually start playback
                default:
                    return false;
            }
        } else return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void abandonAudioFocusO() {
        if (mAudioManager != null && mAudioFocusRequest != null) {
            mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
        }
    }

    private boolean requestAudioFocusBase() {
        if (mAudioManager != null && mAudioFocusListener != null) {
            int focusRequest = mAudioManager.requestAudioFocus(mAudioFocusListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            switch (focusRequest) {
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    return false;
                // don't start playback
                case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                    return true;
                // actually start playback
                default:
                    return false;
            }
        } else return false;
    }

    private void abandonAudioFocusBase() {
        if (mAudioManager != null && mAudioFocusListener != null) {
            mAudioManager.abandonAudioFocus(mAudioFocusListener);
        }
    }
}
