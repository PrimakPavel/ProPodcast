package com.pavelprymak.propodcast.presentation.screens


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.services.*
import com.pavelprymak.propodcast.services.PlayerService.Companion.isStartService
import com.pavelprymak.propodcast.utils.DateFormatUtil.MS_AT_SEC
import com.pavelprymak.propodcast.utils.DateFormatUtil.formatTimeHHmmss
import com.pavelprymak.propodcast.utils.otto.player.*
import com.squareup.otto.Subscribe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_player.*

private const val TAG_PAUSE = "tagPause"
private const val TAG_PLAY = "tagPlay"
private const val SEEK_BAR_MAX_PROGRESS = 10000
private const val MAX_PERCENT = 100f

class PlayerFragment : Fragment(), PlayerUI {
    private var mIsSeekBarTouch = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onResume() {
        super.onResume()
        App.eventBus.register(this)
        updateUiAction()
    }

    override fun onPause() {
        super.onPause()
        App.eventBus.unregister(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playPauseBtn.setOnClickListener { v ->
            if (v.tag === TAG_PAUSE) {
                pauseAction()
            } else if (v.tag === TAG_PLAY) {
                playAction()
            }
        }
        stopBtn.setOnClickListener { v -> stopAction() }
        playerSeekBar.max = SEEK_BAR_MAX_PROGRESS
        playerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val progressInPercents = progress * MAX_PERCENT / SEEK_BAR_MAX_PROGRESS
                    seekToPositionAction(progressInPercents)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mIsSeekBarTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mIsSeekBarTouch = false

            }
        })
    }

    @Subscribe
    fun onUpdatePlayerView(playerUpdateView: EventUpdatePlayerView) {
        setPlayStatus(playerUpdateView.isPlay)
        setLoadingStatus(playerUpdateView.isLoad)
        setPlaybackDuration(playerUpdateView.trackDuration)
        setPlaybackCurrentPosition(playerUpdateView.trackCurrentPosition, playerUpdateView.trackDuration)
        setTrackTitle(playerUpdateView.trackTitle)
        setTrackImage(playerUpdateView.trackImageUrl)
    }

    @Subscribe
    fun onUpdatePlayPauseBtn(playPauseBtnUpdate: EventUpdatePlayPauseBtn) {
        setPlayStatus(playPauseBtnUpdate.isPlay)
    }

    @Subscribe
    fun onUpdateLoading(eventUpdateLoading: EventUpdateLoading) {
        setLoadingStatus(eventUpdateLoading.isLoading)
    }

    @Subscribe
    fun onUpdateTrackImageAndTitle(eventUpdateTrackImageAndTitle: EventUpdateTrackImageAndTitle) {
        setTrackImage(eventUpdateTrackImageAndTitle.imageUrl)
        setTrackTitle(eventUpdateTrackImageAndTitle.title)
    }

    @Subscribe
    fun onUpdateDurationAndCurrentPosition(eventUpdateDurationAndCurrentPos: EventUpdateDurationAndCurrentPos) {
        setPlaybackDuration(eventUpdateDurationAndCurrentPos.trackDuration)
        setPlaybackCurrentPosition(
            eventUpdateDurationAndCurrentPos.trackCurrentPosition,
            eventUpdateDurationAndCurrentPos.trackDuration
        )
    }

    @Subscribe
    fun onStartTrack(eventStartTack: EventStartTack) {
        playerErrorLayout.visibility = View.GONE
        startTrackAction(
            eventStartTack.trackLink,
            eventStartTack.trackTitle,
            eventStartTack.imageUrl,
            eventStartTack.trackAuthor
        )
    }

    @Subscribe
    fun onPlayerError(eventPlayerError: EventPlayerError) {
        playerErrorLayout.visibility = View.VISIBLE
    }

    override fun playAction() {
        if (context != null && isStartService) {
            val serviceIntent = Intent(context, PlayerService::class.java)
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, COMMAND_PLAY)
            ContextCompat.startForegroundService(context!!, serviceIntent)
        }
    }

    override fun pauseAction() {
        if (context != null && isStartService) {
            val serviceIntent = Intent(context, PlayerService::class.java)
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, COMMAND_PAUSE)
            ContextCompat.startForegroundService(context!!, serviceIntent)
        }
    }

    override fun stopAction() {
        context?.let { context ->
            val serviceIntent = Intent(context, PlayerService::class.java)
            context.stopService(serviceIntent)
        }
        App.eventBus.post(EventUpdatePlayerVisibility(false))
    }

    override fun startTrackAction(trackLink: String?, trackTitle: String?, imageUrl: String?, trackAuthor: String?) {
        if (context != null && trackLink != null) {
            val serviceIntent = Intent(context, PlayerService::class.java)
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, COMMAND_START_TRACK)
            serviceIntent.putExtra(EXTRA_TRACK_URL, trackLink)
            serviceIntent.putExtra(EXTRA_TRACK_TITLE, trackTitle)
            serviceIntent.putExtra(EXTRA_TRACK_AUTHOR, trackAuthor)
            serviceIntent.putExtra(EXTRA_TRACK_IMAGE_URL, imageUrl)
            ContextCompat.startForegroundService(context!!, serviceIntent)
        }
    }

    override fun updateUiAction() {
        if (context != null && isStartService) {
            val serviceIntent = Intent(context, PlayerService::class.java)
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, COMMAND_UPDATE_UI)
            ContextCompat.startForegroundService(context!!, serviceIntent)
        }
    }

    override fun seekToPositionAction(percentSeekPosition: Float) {
        if (context != null && isStartService) {
            val serviceIntent = Intent(context, PlayerService::class.java)
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, COMMAND_SEEK_TO_POSITION)
            serviceIntent.putExtra(EXTRA_TRACK_SEEK_PROGRESS_IN_PERSENTS, percentSeekPosition)
            ContextCompat.startForegroundService(context!!, serviceIntent)
        }
    }

    override fun setPlayStatus(isPlay: Boolean) {
        if (isPlay) {
            playPauseBtn.setImageResource(R.drawable.ic_baseline_pause)
            playPauseBtn.contentDescription = getString(R.string.content_description_pause)
            playPauseBtn.tag = TAG_PAUSE
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_baseline_play)
            playPauseBtn.contentDescription = getString(R.string.content_description_play)
            playPauseBtn.tag = TAG_PLAY
        }
    }

    override fun setLoadingStatus(isLoading: Boolean) {
        if (isLoading) progressBarPlayer.visibility = View.VISIBLE else progressBarPlayer.visibility = View.GONE
    }

    override fun setPlayerErrors(errorMessage: String) {

    }

    override fun setPlaybackDuration(duration: Long) {
        if (duration > MS_AT_SEC) {
            tvEpisodeDuration.text = formatTimeHHmmss((duration / MS_AT_SEC).toInt())
            tvEpisodeDuration.visibility = View.VISIBLE
        } else {
            tvEpisodeDuration.visibility = View.GONE
        }
    }

    override fun setPlaybackCurrentPosition(currentPosition: Long, duration: Long) {
        if (!mIsSeekBarTouch) {
            if (currentPosition > 0 && duration > 0) {
                val progress = (currentPosition * SEEK_BAR_MAX_PROGRESS / duration).toInt()
                playerSeekBar.progress = progress
            } else {
                playerSeekBar.progress = 0
            }
        }
        tvCurrentPosition.text = formatTimeHHmmss((currentPosition / MS_AT_SEC).toInt())
    }

    override fun setTrackTitle(trackTitle: String?) {
        if (!trackTitle.isNullOrEmpty()) tvTitle.text = trackTitle else tvTitle.setText(R.string.adapter_empty_string)
    }

    override fun setTrackImage(imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(ivPodcastLogo)
        }
    }
}
