package com.pavelprymak.propodcast.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteEpisodeDao {
    @Query("SELECT * FROM favorite_episodes")
    LiveData<List<FavoriteEpisodeEntity>> loadAllEpisodes();

    @Query("SELECT* FROM favorite_episodes WHERE id = :episodeId")
    LiveData<FavoriteEpisodeEntity> loadEpisodeById(String episodeId);

    @Query("SELECT COUNT(id) FROM favorite_episodes")
    LiveData<Integer> getRowCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEpisode(FavoriteEpisodeEntity episodeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllEpisodes(List<FavoriteEpisodeEntity> episodes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEpisode(FavoriteEpisodeEntity episodeEntity);

    @Query("DELETE FROM favorite_episodes WHERE id = :episodeId")
    void deleteEpisodeById(String episodeId);
}
