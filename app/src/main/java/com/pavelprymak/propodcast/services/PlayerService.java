package com.pavelprymak.propodcast.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.utils.otto.EventUpdatePlayPauseBtn;
import com.pavelprymak.propodcast.utils.otto.EventUpdatePlayerView;
import com.pavelprymak.propodcast.utils.player.PlayerHelper;

import timber.log.Timber;

import static com.pavelprymak.propodcast.App.CHANNEL_ID;

public class PlayerService extends Service {
    private PowerManager.WakeLock wakeLock;
    public static final String EXTRA_COMMAND_PLAYER = "extraCommandToPlayerService";

    public static final String COMMAND_START_TRACK = "commandStartTrack";
    public static final String EXTRA_TRACK_URL = "extraTrackUrl";
    public static final String EXTRA_TRACK_TITLE = "extraTrackTitle";
    public static final String EXTRA_TRACK_IMAGE_URL = "extraTrackImageUrl";

    public static final String COMMAND_PAUSE = "commandPause";

    public static final String COMMAND_PLAY = "commandPlay";
    public static final String COMMAND_SEEK_TO_POSITION = "commandSeekToPosition";
    public static final String EXTRA_TRACK_SEEK_POSITION = "extraTrackSeekPosition";

    public static final String COMMAND_UPDATE_UI = "commandUpdateUI";

    private PlayerHelper mPlayerHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "ExampleApp:Wakelock");
        wakeLock.acquire();
        Timber.d("Wakelock acquired");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example PlayerService")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_baseline_play)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        mPlayerHelper = new PlayerHelper(getApplicationContext(), null, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayerHelper != null) {
            mPlayerHelper.releasePlayer();
            mPlayerHelper = null;
        }
        Timber.d("onDestroy");
        wakeLock.release();
        Timber.d("Wakelock released");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra(EXTRA_COMMAND_PLAYER);
        Timber.d("onStartCommand");
        Timber.d(input);

        switch (input) {
            case COMMAND_START_TRACK: {
                String trackTitle = intent.getStringExtra(EXTRA_TRACK_TITLE);
                String trackUrlStr = intent.getStringExtra(EXTRA_TRACK_URL);
                String trackImage = intent.getStringExtra(EXTRA_TRACK_IMAGE_URL);
                Uri trackUri = Uri.parse(trackUrlStr);
                if (mPlayerHelper != null) {
                    //clear position data and stop
                    mPlayerHelper.stopCurrentTrack();
                    mPlayerHelper.clearResumePosition();
                    //init new track data
                    mPlayerHelper.initializePlayer(trackUri);
                    mPlayerHelper.setTrackTitle(trackTitle);
                    mPlayerHelper.setTrackImageUrl(trackImage);
                }
                break;
            }
            case COMMAND_PAUSE: {
                if (mPlayerHelper != null) {
                    mPlayerHelper.pauseTrack();
                }
                App.eventBus.post(new EventUpdatePlayPauseBtn(false));
                break;
            }
            case COMMAND_PLAY: {
                if (mPlayerHelper != null) {
                    mPlayerHelper.playTrack();
                    App.eventBus.post(new EventUpdatePlayPauseBtn(true));
                }
                break;
            }
            case COMMAND_SEEK_TO_POSITION: {
                long trackSeekPosition = intent.getLongExtra(EXTRA_TRACK_SEEK_POSITION, 0L);
                if (mPlayerHelper != null) {
                    mPlayerHelper.seekToPosition(trackSeekPosition);
                }
                break;
            }
            case COMMAND_UPDATE_UI: {
                if (mPlayerHelper != null) {
                    App.eventBus.post(getDataAboutPlayer());
                }
                break;
            }
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private EventUpdatePlayerView getDataAboutPlayer() {
        EventUpdatePlayerView event = new EventUpdatePlayerView();
        if (mPlayerHelper != null) {
            event.setPlay(mPlayerHelper.isPlayerReadyAndPlay());
            event.setLoad(mPlayerHelper.isPlayerLoading());
            event.setTrackTitle(mPlayerHelper.getTrackTitle());
            event.setTrackImageUrl(mPlayerHelper.getTrackImageUrl());
            event.setTrackDuration(mPlayerHelper.getCurrentTrackDuration());
            event.setTrackCurrentPosition(mPlayerHelper.getCurrentResumePosition());
        }
        return event;
    }

}
