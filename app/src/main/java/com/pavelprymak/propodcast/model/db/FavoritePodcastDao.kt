package com.pavelprymak.propodcast.model.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoritePodcastDao {

    @Query("SELECT COUNT(id) FROM favorite_podcasts")
    fun getRowCount(): LiveData<Int>

    @Query("SELECT * FROM favorite_podcasts")
    fun loadAllPodcasts(): LiveData<List<FavoritePodcastEntity>>

    @Query("SELECT* FROM favorite_podcasts WHERE id = :podcastId")
    fun loadPodcastById(podcastId: String): LiveData<FavoritePodcastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPodcast(podcastEntity: FavoritePodcastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPodcasts(podcastEntitys: List<FavoritePodcastEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePodcast(podcastEntity: FavoritePodcastEntity)

    @Query("DELETE FROM favorite_podcasts WHERE id = :podcastId")
    fun deletePodcast(podcastId: String)
}
