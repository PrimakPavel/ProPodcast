package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;

public interface PodcastInfoClickListener {
    void onEpisodeItemClick(EpisodesItem episodesItem);

    void onMoreEpisodeClick();

    void onEpisodeMoreOptionClick(EpisodesItem episodesItem, View view);

    void onRecommendationItemClick(String podcastId);

    void onPodcastMoreOptionsClick(PodcastItem podcastItem, View v);
}
