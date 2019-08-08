package com.pavelprymak.propodcast.model.db.repo

import androidx.lifecycle.LiveData

import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity

interface DbRepo {
    //Podcasts
    fun getFavoritePodcasts(): LiveData<List<FavoritePodcastEntity>>

    fun getPodcastsCount(): LiveData<Int>


    //Episodes
    fun getFavoriteEpisodes(): LiveData<List<FavoriteEpisodeEntity>>

    fun getEpisodesCount(): LiveData<Int>

    fun getFavoritePodcastById(id: String): LiveData<FavoritePodcastEntity>

    fun insertPodcast(podcastEntity: FavoritePodcastEntity)

    fun updatePodcast(podcastEntity: FavoritePodcastEntity)

    fun deletePodcastById(podcastId: String)

    fun getFavoriteEpisodeById(id: String): LiveData<FavoriteEpisodeEntity>

    fun insertEpisode(episodeEntity: FavoriteEpisodeEntity)

    fun updateEpisode(episodeEntity: FavoriteEpisodeEntity)

    fun deleteEpisodeById(episodeId: String)

}
