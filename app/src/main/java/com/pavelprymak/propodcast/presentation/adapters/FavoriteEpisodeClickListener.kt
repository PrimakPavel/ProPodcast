package com.pavelprymak.propodcast.presentation.adapters

import android.view.View

import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity

interface FavoriteEpisodeClickListener {
    fun onEpisodeItemClick(episodesItem: FavoriteEpisodeEntity)

    fun onEpisodeMoreOptionsClick(episodeId: String, link: String?, v: View)
}
