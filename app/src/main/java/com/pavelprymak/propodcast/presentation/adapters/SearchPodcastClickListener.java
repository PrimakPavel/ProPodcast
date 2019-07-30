package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;

public interface SearchPodcastClickListener {
    void onPodcastItemClick(String podcastId);

    void onPodcastMoreOptionsClick(ResultsItem podcastItem, View v);
}
