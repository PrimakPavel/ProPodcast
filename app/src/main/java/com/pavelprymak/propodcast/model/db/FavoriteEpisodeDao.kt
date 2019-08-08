package com.pavelprymak.propodcast.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteEpisodeDao {

    @Query("SELECT COUNT(id) FROM favorite_episodes")
    fun getRowCount(): LiveData<Int>

    @Query("SELECT * FROM favorite_episodes")
    fun loadAllEpisodes(): LiveData<List<FavoriteEpisodeEntity>>

    @Query("SELECT* FROM favorite_episodes WHERE id = :episodeId")
    fun loadEpisodeById(episodeId: String): LiveData<FavoriteEpisodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisode(episodeEntity: FavoriteEpisodeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEpisodes(episodes: List<FavoriteEpisodeEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateEpisode(episodeEntity: FavoriteEpisodeEntity)

    @Query("DELETE FROM favorite_episodes WHERE id = :episodeId")
    fun deleteEpisodeById(episodeId: String)
}
