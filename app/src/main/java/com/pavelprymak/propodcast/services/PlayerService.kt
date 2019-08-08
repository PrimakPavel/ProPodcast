package com.pavelprymak.propodcast.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.utils.otto.player.*
import com.pavelprymak.propodcast.utils.player.PlayerErrorsListener
import com.pavelprymak.propodcast.utils.player.PlayerHelper
import com.pavelprymak.propodcast.utils.player.PlayerStateListener
import com.pavelprymak.propodcast.utils.player.UpdateByTimerHandler
import com.pavelprymak.propodcast.utils.widget.LastTrackPreferenceManager
import com.pavelprymak.propodcast.utils.widget.WidgetUpdateManager
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.lang.Exception


const val EXTRA_COMMAND_PLAYER = "extraCommandToPlayerService"
private const val NOTIFICATION_FOREGROUND_ID = 123

const val COMMAND_START_TRACK = "commandStartTrack"
const val COMMAND_CONTINUE_LAST_TRACK = "commandContinueLastTrack"
const val COMMAND_STOP_SERVICE = "commandStopService"
const val EXTRA_TRACK_URL = "extraTrackUrl"
const val EXTRA_TRACK_TITLE = "extraTrackTitle"
const val EXTRA_TRACK_AUTHOR = "extraTrackAuthor"
const val EXTRA_TRACK_IMAGE_URL = "extraTrackImageUrl"
const val COMMAND_PAUSE = "commandPause"
const val COMMAND_PLAY = "commandPlay"
const val COMMAND_SEEK_TO_POSITION = "commandSeekToPosition"
const val EXTRA_TRACK_SEEK_PROGRESS_IN_PERSENTS = "extraTrackSeekPosition"
const val COMMAND_UPDATE_UI = "commandUpdateUI"

class PlayerService : Service(), PlayerStateListener, PlayerErrorsListener {

    private var mPlayerHelper: PlayerHelper? = null
    private var wakeLock: PowerManager.WakeLock? = null

    private var mUpdateUIPositionHandler: UpdateByTimerHandler? = null
    private val mEventBus = App.eventBus
    private val mLastTrackPref: LastTrackPreferenceManager by inject()

    private val dataAboutPlayer: EventUpdatePlayerView
        get() {
            val event = EventUpdatePlayerView()
            mPlayerHelper?.apply {
                event.isPlay = isPlayerReadyAndPlay
                event.isLoad = isPlayerLoading
                event.trackTitle = trackTitle
                event.trackImageUrl = trackImageUrl
                event.trackDuration = currentTrackDuration
                event.trackCurrentPosition = currentResumePosition
            }
            return event
        }


    override fun onCreate() {
        super.onCreate()
        isStartService = true
        Timber.d("onCreate")
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            getString(R.string.player_service_wake_lock_tag)
        )
        wakeLock?.acquire()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = NotificationCompat.Builder(this, getString(R.string.player_notification_channel_id))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.player_service_running))
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()

            startForeground(NOTIFICATION_FOREGROUND_ID, notification)
        }
        Timber.d("Wakelock acquired")
        mPlayerHelper = PlayerHelper(applicationContext, this, this)
        mUpdateUIPositionHandler = object : UpdateByTimerHandler() {
            public override fun doOperation() {
                mPlayerHelper?.apply {
                    //Update current position and duration
                    mEventBus.post(EventUpdateDurationAndCurrentPos(currentResumePosition, currentTrackDuration))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayerHelper?.apply {
            mLastTrackPref.saveTrackCurrentPosition(currentResumePosition)
            releasePlayer()
        }
        mPlayerHelper = null
        mUpdateUIPositionHandler?.stopHandler()
        mUpdateUIPositionHandler = null

        isStartService = false
        isTrackPlayNow = false
        WidgetUpdateManager.updateWidget(applicationContext)
        Timber.d("onDestroy")
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
        Timber.d("Wakelock released")
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val input = intent.getStringExtra(EXTRA_COMMAND_PLAYER)
        Timber.d("onStartCommand")
        Timber.d(input)
        when (input) {
            COMMAND_START_TRACK -> {
                val trackTitle = intent.getStringExtra(EXTRA_TRACK_TITLE)
                val trackUrlStr = intent.getStringExtra(EXTRA_TRACK_URL)
                val trackAuthor = intent.getStringExtra(EXTRA_TRACK_AUTHOR)
                val trackImage = intent.getStringExtra(EXTRA_TRACK_IMAGE_URL)
                val trackUri = Uri.parse(trackUrlStr)
                startTrack(trackTitle, trackAuthor, trackImage, trackUri, 0)
                mLastTrackPref.saveTrackInfo(trackUrlStr, trackTitle, trackAuthor, trackImage)
            }
            COMMAND_CONTINUE_LAST_TRACK -> {
                if (mLastTrackPref.trackAudioUrl != null) {
                    val trackUri = Uri.parse(mLastTrackPref.trackAudioUrl)
                    startTrack(
                        mLastTrackPref.trackTitle,
                        mLastTrackPref.trackAuthor,
                        mLastTrackPref.trackLogo,
                        trackUri,
                        mLastTrackPref.trackCurrentPosition
                    )
                }
            }
            COMMAND_PAUSE -> {
                mPlayerHelper?.apply {
                    pauseTrack()
                    mLastTrackPref.saveTrackCurrentPosition(currentResumePosition)
                }
                mUpdateUIPositionHandler?.stopHandler()

            }
            COMMAND_PLAY -> {
                mPlayerHelper?.playTrack()
                mUpdateUIPositionHandler?.startHandler()
            }
            COMMAND_SEEK_TO_POSITION -> {
                val progressInPercents = intent.getFloatExtra(EXTRA_TRACK_SEEK_PROGRESS_IN_PERSENTS, 0f)
                mPlayerHelper?.seekToPosition(progressInPercents)

            }
            COMMAND_UPDATE_UI -> {
                if (mPlayerHelper != null) {
                    mEventBus.post(dataAboutPlayer)
                }
            }
            COMMAND_STOP_SERVICE -> {
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startTrack(
        trackTitle: String?,
        trackAuthor: String?,
        trackImage: String?,
        trackUri: Uri,
        oldPosition: Long
    ) {
        mPlayerHelper?.apply {
            this.trackTitle = trackTitle
            this.trackImageUrl = trackImage
            this.trackAuthor = trackAuthor
            //update UI loading flag true
            mEventBus.post(EventUpdateLoading(true))
            mUpdateUIPositionHandler?.startHandler()
            //clear position data and stop
            stopCurrentTrack()
            clearResumePosition()
            //init new track data
            if (oldPosition == 0L) {
                initializePlayer(trackUri)
            } else {
                initializePlayer(trackUri, oldPosition)
            }
            //update UI track image and title
            mEventBus.post(EventUpdateTrackImageAndTitle(trackTitle, trackImage))
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun stateChanged() {
        // Show foreground notification
        mPlayerHelper?.let { playerHelper ->
            if (playerHelper.mediaSessionHelper != null && playerHelper.trackImageUrl != null) {
                Picasso.get().load(playerHelper.trackImageUrl).into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        val notification = playerHelper.mediaSessionHelper.getNotification(
                            playerHelper.trackTitle,
                            playerHelper.trackAuthor,
                            BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher)
                        )
                        if (notification != null) {
                            startForeground(NOTIFICATION_FOREGROUND_ID, notification)
                        }
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        val notification = playerHelper.mediaSessionHelper.getNotification(
                            playerHelper.trackTitle,
                            playerHelper.trackAuthor,
                            bitmap
                        )
                        if (notification != null) {
                            startForeground(NOTIFICATION_FOREGROUND_ID, notification)
                        }
                    }
                })
            }
        }
    }

    override fun isBuffed() {
        mEventBus.post(EventUpdateLoading(true))
    }

    override fun isReadyAndPlay() {
        mEventBus.post(EventUpdateLoading(false))
        mEventBus.post(EventUpdatePlayPauseBtn(true))
        if (mUpdateUIPositionHandler?.isStopHandler == true) {
            mUpdateUIPositionHandler?.startHandler()
        }
        isTrackPlayNow = true
        WidgetUpdateManager.updateWidget(applicationContext)
    }

    override fun isReadyAndPause() {
        mEventBus.post(EventUpdateLoading(false))
        mEventBus.post(EventUpdatePlayPauseBtn(false))
        isTrackPlayNow = false
        WidgetUpdateManager.updateWidget(applicationContext)
    }

    override fun isEnded() {
        mEventBus.post(EventUpdateLoading(false))
        isTrackPlayNow = false
        WidgetUpdateManager.updateWidget(applicationContext)
    }

    override fun onPlayerError(errorCode: Int) {
        mEventBus.post(EventPlayerError(errorCode))
    }

    companion object {
        var isStartService = false
        var isTrackPlayNow = false
    }
}
