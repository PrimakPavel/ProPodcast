package com.pavelprymak.propodcast.presentation.adapters;

import android.view.View;

import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;

public interface FavoriteEpisodeClickListener {
    void onEpisodeItemClick(FavoriteEpisodeEntity episodesItem);

    void onEpisodeMoreOptionsClick(String episodeId, String link, View v);
}
