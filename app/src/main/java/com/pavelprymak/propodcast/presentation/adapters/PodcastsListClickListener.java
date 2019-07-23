package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

public interface PodcastsListClickListener {
    void onPodcastItemClick(String podcastId);

    void onPodcastMoreOptionsClick(String podcastId, String link, View v);
}
