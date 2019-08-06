package com.pavelprymak.propodcast.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.utils.otto.player.EventPlayerError;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateDurationAndCurrentPos;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateLoading;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayPauseBtn;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerView;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateTrackImageAndTitle;
import com.pavelprymak.propodcast.utils.player.PlayerErrorsListener;
import com.pavelprymak.propodcast.utils.player.PlayerHelper;
import com.pavelprymak.propodcast.utils.player.PlayerStateListener;
import com.pavelprymak.propodcast.utils.player.UpdateByTimerHandler;
import com.pavelprymak.propodcast.utils.widget.LastTrackPreferenceManager;
import com.pavelprymak.propodcast.utils.widget.WidgetUpdateManager;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import timber.log.Timber;

import static com.pavelprymak.propodcast.App.CHANNEL_ID;

public class PlayerService extends Service implements PlayerStateListener, PlayerErrorsListener {
    public static final String EXTRA_COMMAND_PLAYER = "extraCommandToPlayerService";
    private static final int NOTIFICATION_FOREGROUND_ID = 123;

    public static final String COMMAND_START_TRACK = "commandStartTrack";
    public static final String COMMAND_CONTINUE_LAST_TRACK = "commandContinueLastTrack";
    public static final String COMMAND_STOP_SERVICE = "commandStopService";
    public static final String EXTRA_TRACK_URL = "extraTrackUrl";
    public static final String EXTRA_TRACK_TITLE = "extraTrackTitle";
    public static final String EXTRA_TRACK_AUTHOR = "extraTrackAuthor";
    public static final String EXTRA_TRACK_IMAGE_URL = "extraTrackImageUrl";

    public static final String COMMAND_PAUSE = "commandPause";

    public static final String COMMAND_PLAY = "commandPlay";
    public static final String COMMAND_SEEK_TO_POSITION = "commandSeekToPosition";
    public static final String EXTRA_TRACK_SEEK_PROGRESS_IN_PERSENTS = "extraTrackSeekPosition";

    public static final String COMMAND_UPDATE_UI = "commandUpdateUI";

    public static boolean isStartService = false;
    public static boolean isTrackPlayNow = false;

    private PlayerHelper mPlayerHelper;
    private PowerManager.WakeLock wakeLock;

    private UpdateByTimerHandler mUpdateUIPositionHandler;
    private final Bus eventBus = App.eventBus;
    private final LastTrackPreferenceManager mLastTrackPreferenceManager = App.mLastTrackSettings;


    @Override
    public void onCreate() {
        super.onCreate();
        isStartService = true;
        Timber.d("onCreate");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "ProPodcastApp:Wakelock");
        wakeLock.acquire();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Running...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(NOTIFICATION_FOREGROUND_ID, notification);
        }
        Timber.d("Wakelock acquired");
        mPlayerHelper = new PlayerHelper(getApplicationContext(), this, this);
        mUpdateUIPositionHandler = new UpdateByTimerHandler() {
            @Override
            public void doOperation() {
                if (mPlayerHelper != null) {
                    //Update current position and duration
                    if (eventBus != null)
                        eventBus.post(new EventUpdateDurationAndCurrentPos(mPlayerHelper.getCurrentResumePosition(), mPlayerHelper.getCurrentTrackDuration()));
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayerHelper != null) {
            if (mLastTrackPreferenceManager != null) {
                mLastTrackPreferenceManager.saveTrackCurrentPosition(mPlayerHelper.getCurrentResumePosition());
            }
            mPlayerHelper.releasePlayer();
            mPlayerHelper = null;
        }
        if (mUpdateUIPositionHandler != null) {
            mUpdateUIPositionHandler.stopHandler();
            mUpdateUIPositionHandler = null;
        }
        isStartService = false;
        isTrackPlayNow = false;
        WidgetUpdateManager.updateWidget(getApplicationContext());
        Timber.d("onDestroy");
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
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
                String trackAuthor = intent.getStringExtra(EXTRA_TRACK_AUTHOR);
                String trackImage = intent.getStringExtra(EXTRA_TRACK_IMAGE_URL);
                Uri trackUri = Uri.parse(trackUrlStr);
                startTrack(trackTitle, trackAuthor, trackImage, trackUri, 0);
                mLastTrackPreferenceManager.saveTrackInfo(trackUrlStr, trackTitle, trackAuthor, trackImage);
                break;
            }
            case COMMAND_CONTINUE_LAST_TRACK: {
                if (mLastTrackPreferenceManager != null && mLastTrackPreferenceManager.getTrackAudioUrl() != null) {
                    Uri trackUri = Uri.parse(mLastTrackPreferenceManager.getTrackAudioUrl());
                    startTrack(mLastTrackPreferenceManager.getTrackTitle(),
                            mLastTrackPreferenceManager.getTrackAuthor(),
                            mLastTrackPreferenceManager.getTrackLogo(),
                            trackUri,
                            mLastTrackPreferenceManager.getTrackCurrentPosition());
                }
                break;
            }
            case COMMAND_PAUSE: {
                if (mPlayerHelper != null) {
                    mPlayerHelper.pauseTrack();
                    if (mLastTrackPreferenceManager != null)
                        mLastTrackPreferenceManager.saveTrackCurrentPosition(mPlayerHelper.getCurrentResumePosition());
                }
                if (mUpdateUIPositionHandler != null) {
                    mUpdateUIPositionHandler.stopHandler();
                }

                break;
            }
            case COMMAND_PLAY: {
                if (mPlayerHelper != null) {
                    mPlayerHelper.playTrack();
                }
                if (mUpdateUIPositionHandler != null) {
                    mUpdateUIPositionHandler.startHandler();
                }
                break;
            }
            case COMMAND_SEEK_TO_POSITION: {
                float progressInPercents = intent.getFloatExtra(EXTRA_TRACK_SEEK_PROGRESS_IN_PERSENTS, 0f);
                if (mPlayerHelper != null) {
                    mPlayerHelper.seekToPosition(progressInPercents);
                }
                break;
            }
            case COMMAND_UPDATE_UI: {
                if (mPlayerHelper != null && eventBus != null) {
                    eventBus.post(getDataAboutPlayer());
                }
                break;
            }
            case COMMAND_STOP_SERVICE: {
                stopSelf();
                break;
            }
        }
        return START_STICKY;
    }

    private void startTrack(String trackTitle, String trackAuthor, String trackImage, Uri trackUri, long oldPosition) {
        if (mPlayerHelper != null) {
            //update UI loading flag true
            if (eventBus != null)
                eventBus.post(new EventUpdateLoading(true));
            if (mUpdateUIPositionHandler != null) {
                mUpdateUIPositionHandler.startHandler();
            }
            //clear position data and stop
            mPlayerHelper.stopCurrentTrack();
            mPlayerHelper.clearResumePosition();
            //init new track data
            if (oldPosition == 0L) {
                mPlayerHelper.initializePlayer(trackUri);
            } else {
                mPlayerHelper.initializePlayer(trackUri, oldPosition);
            }
            mPlayerHelper.setTrackTitle(trackTitle);
            mPlayerHelper.setTrackImageUrl(trackImage);
            mPlayerHelper.setTrackAuthor(trackAuthor);
            //update UI track image and title
            if (eventBus != null)
                eventBus.post(new EventUpdateTrackImageAndTitle(trackTitle, trackImage));
        }
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

    @Override
    public void stateChanged() {
        // Show foreground notification
        if (mPlayerHelper != null && mPlayerHelper.getMediaSessionHelper() != null) {
            Picasso.get().load(mPlayerHelper.getTrackImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Notification notification = mPlayerHelper.getMediaSessionHelper().getNotification(mPlayerHelper.getTrackTitle(), mPlayerHelper.getTrackAuthor(), bitmap);
                    if (notification != null) {
                        startForeground(NOTIFICATION_FOREGROUND_ID, notification);
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Notification notification = mPlayerHelper.getMediaSessionHelper().getNotification(mPlayerHelper.getTrackTitle(), mPlayerHelper.getTrackAuthor(), BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher));
                    if (notification != null) {
                        startForeground(NOTIFICATION_FOREGROUND_ID, notification);
                    }
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }

    @Override
    public void isBuffed() {
        if (eventBus != null) {
            eventBus.post(new EventUpdateLoading(true));
        }
    }

    @Override
    public void isReadyAndPlay() {
        if (eventBus != null) {
            eventBus.post(new EventUpdateLoading(false));
            eventBus.post(new EventUpdatePlayPauseBtn(true));
        }
        if (mUpdateUIPositionHandler != null && mUpdateUIPositionHandler.isStopHandler()) {
            mUpdateUIPositionHandler.startHandler();
        }
        isTrackPlayNow = true;
        WidgetUpdateManager.updateWidget(getApplicationContext());
    }

    @Override
    public void isReadyAndPause() {
        if (eventBus != null) {
            eventBus.post(new EventUpdateLoading(false));
            eventBus.post(new EventUpdatePlayPauseBtn(false));
        }
        isTrackPlayNow = false;
        WidgetUpdateManager.updateWidget(getApplicationContext());
    }

    @Override
    public void isEnded() {
        if (eventBus != null) {
            eventBus.post(new EventUpdateLoading(false));
        }
        isTrackPlayNow = false;
        WidgetUpdateManager.updateWidget(getApplicationContext());
    }

    @Override
    public void onPlayerError(int errorCode) {
        if (eventBus != null) {
            eventBus.post(new EventPlayerError(errorCode));
        }
    }
}
