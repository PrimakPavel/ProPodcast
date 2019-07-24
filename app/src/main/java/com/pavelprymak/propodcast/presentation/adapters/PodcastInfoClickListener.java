package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;

public interface PodcastInfoClickListener {
    void onEpisodeItemClick(String episodeId, String mediaUrl);

    void onMoreEpisodeClick();

    void onRecommendationItemClick(String podcastId);

    void onPodcastMoreOptionsClick(PodcastItem podcastItem, View v);
}
