package com.pavelprymak.propodcast.utils.player.mediaSession.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MediaSessionNotificationsManager {
    private final Context mContext;
    private NotificationManager mNotificationManager;

    public MediaSessionNotificationsManager(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    public void showNotification(PlaybackStateCompat state, MediaSessionCompat.Token mediaSessionToken, String recipeDescription) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.player_notification_channel_id));

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = mContext.getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = mContext.getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(mContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new NotificationCompat
                .Action(R.drawable.exo_controls_previous, mContext.getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (mContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (mContext, 0, new Intent(mContext, MainActivity.class), 0);

        builder.setContentTitle(mContext.getString(R.string.app_name))
                .setContentText(recipeDescription)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_STATUS)
                .setSound(null)
                .addAction(playPauseAction)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionToken)
                        .setShowActionsInCompactView(0, 1));


        mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public Notification getNotification(PlaybackStateCompat state, MediaSessionCompat.Token mediaSessionToken, String trackTitle, String trackAuthor, Bitmap logoBitmap) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.player_notification_channel_id));

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = mContext.getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = mContext.getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(mContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new NotificationCompat
                .Action(R.drawable.exo_controls_previous, mContext.getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (mContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (mContext, 0, new Intent(mContext, MainActivity.class), 0);

        builder.setContentTitle(trackAuthor)
                .setContentText(trackTitle)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(logoBitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionToken)
                        .setShowActionsInCompactView(0, 1));

        return builder.build();
    }

    public void release() {
        if (mNotificationManager != null)
            mNotificationManager.cancelAll();
    }
}
