package com.pavelprymak.propodcast.presentation.screens;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.pavelprymak.propodcast.utils.otto.EventUpdatePlayerView;
import com.pavelprymak.propodcast.utils.otto.EventUpdatePlayPauseBtn;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_COMMAND_PLAYER;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_IMAGE_URL;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_SEEK_POSITION;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_TITLE;
import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_TRACK_URL;
import static com.pavelprymak.propodcast.utils.DateFormatUtil.MS_AT_SEC;
import static com.pavelprymak.propodcast.utils.DateFormatUtil.formatTimeHHmmss;

public class PlayerFragment extends Fragment implements PlayerUI {
    private FragmentPlayerBinding mBinding;
    private static final String TAG_PAUSE = "tagPause";
    private static final String TAG_PLAY = "tagPlay";
    private static final String EMPTY_TITLE = "";

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

        //TODO start track other way
        startTrackAction("The Monday show",
                "https://cdn-images-1.listennotes.com/podcasts/my-life-in-the-mosh-of-ghosts-1Bk3nRY9Dgp.300x300.jpg",
                "https://www.listennotes.com/e/p/11b34041e804491b9704d11f283c74de/");
        new Handler().postDelayed(this::updateUiAction, 5000);
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
    }

    @Override
    public void startTrackAction(String trackTitle, String imageUrl, String trackLink) {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_START_TRACK);
            serviceIntent.putExtra(EXTRA_TRACK_URL, trackLink);
            serviceIntent.putExtra(EXTRA_TRACK_TITLE, trackTitle);
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
    public void seekToPositionAction(long seekPosition) {
        if (getContext() != null) {
            Intent serviceIntent = new Intent(getContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_SEEK_TO_POSITION);
            serviceIntent.putExtra(EXTRA_TRACK_SEEK_POSITION, seekPosition);
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
        if (currentPosition > 0 && duration > 0) {
            int percent = (int) (currentPosition * 100 / duration);
            mBinding.playerSeekBar.setProgress(percent);
            mBinding.tvCurrentPosition.setText(formatTimeHHmmss((int) (currentPosition / MS_AT_SEC)));
        } else {
            mBinding.playerSeekBar.setProgress(0);
        }
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
