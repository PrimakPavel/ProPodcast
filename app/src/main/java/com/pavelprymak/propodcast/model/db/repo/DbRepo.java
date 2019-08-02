package com.pavelprymak.propodcast.model.db.repo;

import androidx.lifecycle.LiveData;

import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;

import java.util.List;

public interface DbRepo {
    //Podcasts
    LiveData<List<FavoritePodcastEntity>> getFavoritePodcasts();

    LiveData<FavoritePodcastEntity> getFavoritePodcastById(String id);

    void insertPodcast(FavoritePodcastEntity podcastEntity);

    void updatePodcast(FavoritePodcastEntity podcastEntity);

    void deletePodcastById(String podcastId);

    LiveData<Integer> getPodcastsCount();


    //Episodes
    LiveData<List<FavoriteEpisodeEntity>> getFavoriteEpisodes();

    LiveData<FavoriteEpisodeEntity> getFavoriteEpisodeById(String id);

    void insertEpisode(FavoriteEpisodeEntity episodeEntity);

    void updateEpisode(FavoriteEpisodeEntity episodeEntity);

    void deleteEpisodeById(String episodeId);

    LiveData<Integer> getEpisodesCount();

}
