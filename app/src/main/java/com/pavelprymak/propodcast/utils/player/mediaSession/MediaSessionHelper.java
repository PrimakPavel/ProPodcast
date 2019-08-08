package com.pavelprymak.propodcast.utils.player.mediaSession;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.session.MediaButtonReceiver;

import com.pavelprymak.propodcast.utils.player.PlayerHelper;
import com.pavelprymak.propodcast.utils.player.mediaSession.notifications.MediaSessionNotificationsManager;

public class MediaSessionHelper {
    private static final String TAG = MediaSessionHelper.class.getSimpleName();
    //MediaSession
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private MediaReceiver mMediaReceiver;
    private Context mContext;
    //Notification
    private MediaSessionNotificationsManager mMediaSessionNotificationsManager;

    public MediaSessionHelper(@NonNull Context context, PlayerHelper.MySessionCallback mySessionCallback) {
        initializeMediaSession(context, mySessionCallback);
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession(@NonNull Context context, PlayerHelper.MySessionCallback mySessionCallback) {

        // Create a MediaSessionCompat.
        mContext = context;
        mMediaSession = new MediaSessionCompat(context, TAG);
        if (mMediaSessionNotificationsManager == null) {
            mMediaSessionNotificationsManager = new MediaSessionNotificationsManager(mContext);
        }
        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(mySessionCallback);

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

        //register broadcast receiver MediaReceiver
        if (mMediaReceiver == null) {
            mMediaReceiver = new MediaReceiver();
        }
        IntentFilter filter1 = new IntentFilter("android.intent.action.MEDIA_BUTTON");
        context.registerReceiver(mMediaReceiver, filter1);
    }

    public void showNotification(String trackTitle) {
        //SHOW NOTIFICATION
        if (mMediaSessionNotificationsManager != null && mMediaSession != null && mStateBuilder != null) {
            mMediaSessionNotificationsManager.showNotification(mStateBuilder.build(), mMediaSession.getSessionToken(), trackTitle);
        }
    }

    public Notification getNotification(String trackTitle, String trackAuthor, @Nullable Bitmap logoBitmap) {
        //SHOW NOTIFICATION
        if (mMediaSessionNotificationsManager != null && mMediaSession != null && mStateBuilder != null) {
            return mMediaSessionNotificationsManager.getNotification(mStateBuilder.build(), mMediaSession.getSessionToken(), trackTitle, trackAuthor, logoBitmap);
        } else return null;
    }

    public MediaSessionCompat getMediaSession() {
        return mMediaSession;
    }

    public PlaybackStateCompat.Builder getStateBuilder() {
        return mStateBuilder;
    }

    public void release() {
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
        //unregister broadcast receiver MediaReceiver
        if (mContext != null && mMediaReceiver != null) {
            mContext.unregisterReceiver(mMediaReceiver);
        }
        if (mMediaSessionNotificationsManager != null) {
            mMediaSessionNotificationsManager.release();
            mMediaSessionNotificationsManager = null;
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

}
