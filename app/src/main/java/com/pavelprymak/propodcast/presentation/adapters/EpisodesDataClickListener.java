package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

public interface EpisodesDataClickListener {
    void onEpisodeItemClick(String episodeId, String mediaUrl);
    void onMoreEpisodeClick();
    void onRecommendationItemClick(String podcastId);
    void onPodcastMoreOptionsClick(String podcastId, String link, View v);
}
