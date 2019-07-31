package com.pavelprymak.propodcast.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;

import java.util.List;

@Dao
public interface FavoritePodcastDao {
    @Query("SELECT * FROM favorite_podcasts")
    LiveData<List<FavoritePodcastEntity>> loadAllPodcasts();

    @Query("SELECT* FROM favorite_podcasts WHERE id = :podcastId")
    LiveData<FavoritePodcastEntity> loadPodcastById(String podcastId);

    @Query("SELECT COUNT(id) FROM favorite_podcasts")
    LiveData<Integer> getRowCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPodcast(FavoritePodcastEntity podcastEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPodcasts(List<FavoritePodcastEntity> podcastEntitys);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePodcast(FavoritePodcastEntity podcastEntity);

    @Query("DELETE FROM favorite_podcasts WHERE id = :podcastId")
    void deletePodcast(String podcastId);
}
