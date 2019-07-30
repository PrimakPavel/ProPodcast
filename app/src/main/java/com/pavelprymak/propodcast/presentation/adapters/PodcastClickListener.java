package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;

public interface PodcastClickListener {
    void onPodcastItemClick(String podcastId);

    void onPodcastMoreOptionsClick(PodcastItem podcastItem, View v);

}
