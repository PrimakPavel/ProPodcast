package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;

public interface PodcastClickListener {
    void onPodcastItemClick(String podcastId);

    void onPodcastMoreOptionsClick(PodcastItem podcastItem, View v);
}
