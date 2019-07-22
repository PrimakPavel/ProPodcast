package com.pavelprymak.propodcast.model.db.repo;

import androidx.lifecycle.LiveData;

import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;

import java.util.List;

public interface DbRepo {
    LiveData<List<FavoritePodcastEntity>> getFavoritePodcasts();

    LiveData<FavoritePodcastEntity> getFavoritePodcastById(String id);

    void insertPodcast(FavoritePodcastEntity podcastEntity);

    void updatePodcast(FavoritePodcastEntity podcastEntity);

    void deletePodcastById(String podcastId);

    LiveData<Integer> getPodcastsCount();

}
