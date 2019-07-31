package com.pavelprymak.propodcast.presentation.screens;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentPlayerBinding;
import com.pavelprymak.propodcast.services.PlayerService;
import com.pavelprymak.propodcast.services.PlayerUI;
import com.pavelprymak.propodcast.utils.otto.player.EventPlayerError;
import com.pavelprymak.propodcast.utils.otto.player.EventStartTack;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateDurationAndCurrentPos;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateLoading;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayPauseBtn;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerView;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateTrackImageAndTitle;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_COMMAND_PLAYER;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_AUTHOR;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_IMAGE_URL;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_SEEK_PROGRESS_IN_PERSENTS;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_TITLE;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_URL;
import static com.pavelprymak.propodcast.utils.DateFormatUtil.MS_AT_SEC;
import static com.pavelprymak.propodcast.utils.DateFormatUtil.formatTimeHHmmss;

public class PlayerFragment extends Fragment implements PlayerUI {
    private FragmentPlayerBinding mBinding;
    private static final String TAG_PAUSE = "tagPause";
    private static final String TAG_PLAY = "tagPlay";
    private static final String EMPTY_TITLE = "";
    private static int SEEK_BAR_MAX_PROGRESS = 10000;
    private static float MAX_PERCENT = 100f;
    private boolean mIsSeekBarTouch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        App.eventBus.register(this);
        updateUiAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.eventBus.unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.playPauseBtn.setOnClickListener(v -> {
            if (v.getTag() == TAG_PAUSE) {
                pauseAction();
            } else if (v.getTag() == TAG_PLAY) {
                playAction();
            }
        });
        mBinding.stopBtn.setOnClickListener(v -> stopAction());
        mBinding.playerSeekBar.setMax(SEEK_BAR_MAX_PROGRESS);
        mBinding.playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float progressInPercents = progress * MAX_PERCENT / SEEK_BAR_MAX_PROGRESS;
                    seekToPositionAction(progressInPercents);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsSeekBarTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsSeekBarTouch = false;

            }
        });
    }

    @Subscribe
    public void onUpdatePlayerView(EventUpdatePlayerView playerUpdateView) {
        setPlayStatus(playerUpdateView.isPlay());
        setLoadingStatus(playerUpdateView.isLoad());
        setPlaybackDuration(playerUpdateView.getTrackDuration());
        setPlaybackCurrentPosition(playerUpdateView.getTrackCurrentPosition(), playerUpdateView.getTrackDuration());
        setTrackTitle(playerUpdateView.getTrackTitle());
        setTrackImage(playerUpdateView.getTrackImageUrl());
    }

    @Subscribe
    public void onUpdatePlayPauseBtn(EventUpdatePlayPauseBtn playPauseBtnUpdate) {
        setPlayStatus(playPauseBtnUpdate.isPlay());
    }

    @Subscribe
    public void onUpdateLoading(EventUpdateLoading eventUpdateLoading) {
        setLoadingStatus(eventUpdateLoading.isLoading());
    }

    @Subscribe
    public void onUpdateTrackImageAndTitle(EventUpdateTrackImageAndTitle eventUpdateTrackImageAndTitle) {
        setTrackImage(eventUpdateTrackImageAndTitle.getImageUrl());
        setTrackTitle(eventUpdateTrackImageAndTitle.getTitle());
    }

    @Subscribe
    public void onUpdateDurationAndCurrentPosition(EventUpdateDurationAndCurrentPos eventUpdateDurationAndCurrentPos) {
        setPlaybackDuration(eventUpdateDurationAndCurrentPos.getTrackDuration());
        setPlaybackCurrentPosition(eventUpdateDurationAndCurrentPos.getTrackCurrentPosition(), eventUpdateDurationAndCurrentPos.getTrackDuration());
    }

    @Subscribe
    public void onStartTrack(EventStartTack eventStartTack) {
        mBinding.playerErrorLayout.setVisibility(View.GONE);
        startTrackAction(eventStartTack.getTrackLink(), eventStartTack.getTrackTitle(), eventStartTack.getImageUrl(), eventStartTack.getTrackAuthor());
    }

    @Subscribe
    public void onPlayerError(EventPlayerError eventPlayerError) {
        mBinding.playerErrorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void playAction() {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_PLAY);
            ContextCompat.startForegroundService(getContext(), serviceIntent);
        }
    }

    @Override
    public void pauseAction() {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_PAUSE);
            ContextCompat.startForegroundService(getContext(), serviceIntent);
        }
    }

    @Override
    public void stopAction() {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            getContext().stopService(serviceIntent);
        }
        App.eventBus.post(new EventUpdatePlayerVisibility(false));
    }

    @Override
    public void startTrackAction(String trackLink, String trackTitle, String imageUrl, String trackAuthor) {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_START_TRACK);
            serviceIntent.putExtra(EXTRA_TRACK_URL, trackLink);
            serviceIntent.putExtra(EXTRA_TRACK_TITLE, trackTitle);
            serviceIntent.putExtra(EXTRA_TRACK_AUTHOR, trackAuthor);
            serviceIntent.putExtra(EXTRA_TRACK_IMAGE_URL, imageUrl);
            ContextCompat.startForegroundService(getContext(), serviceIntent);
        }
    }

    @Override
    public void updateUiAction() {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_UPDATE_UI);
            ContextCompat.startForegroundService(getContext(), serviceIntent);
        }
    }

    @Override
    public void seekToPositionAction(float progressInPercents) {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_SEEK_TO_POSITION);
            serviceIntent.putExtra(EXTRA_TRACK_SEEK_PROGRESS_IN_PERSENTS, progressInPercents);
            ContextCompat.startForegroundService(getContext(), serviceIntent);
        }
    }

    @Override
    public void setPlayStatus(boolean isPlay) {
        if (isPlay) {
            mBinding.playPauseBtn.setImageResource(R.drawable.ic_baseline_pause);
            mBinding.playPauseBtn.setTag(TAG_PAUSE);
        } else {
            mBinding.playPauseBtn.setImageResource(R.drawable.ic_baseline_play);
            mBinding.playPauseBtn.setTag(TAG_PLAY);
        }
    }

    @Override
    public void setLoadingStatus(boolean isLoading) {
        if (isLoading) {
            mBinding.progressBarPlayer.setVisibility(View.VISIBLE);
        } else {
            mBinding.progressBarPlayer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPlayerErrors(String errorMessage) {

    }

    @Override
    public void setPlaybackDuration(long duration) {
        if (duration > MS_AT_SEC)
            mBinding.tvEpisodeDuration.setText(formatTimeHHmmss((int) (duration / MS_AT_SEC)));
    }

    @Override
    public void setPlaybackCurrentPosition(long currentPosition, long duration) {
        if (!mIsSeekBarTouch) {
            if (currentPosition > 0 && duration > 0) {
                int progress = (int) (currentPosition * SEEK_BAR_MAX_PROGRESS / duration);
                mBinding.playerSeekBar.setProgress(progress);
            } else {
                mBinding.playerSeekBar.setProgress(0);
            }
        }
        mBinding.tvCurrentPosition.setText(formatTimeHHmmss((int) (currentPosition / MS_AT_SEC)));
    }

    @Override
    public void setTrackTitle(String trackTitle) {
        if (!TextUtils.isEmpty(trackTitle))
            mBinding.tvTitle.setText(trackTitle);
        else {
            mBinding.tvTitle.setText(EMPTY_TITLE);
        }
    }

    @Override
    public void setTrackImage(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(mBinding.ivPodcastLogo);
        }
    }
}
