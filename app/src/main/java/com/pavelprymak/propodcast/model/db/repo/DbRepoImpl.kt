package com.pavelprymak.propodcast.model.db.repo

import androidx.lifecycle.LiveData

import com.pavelprymak.propodcast.model.db.AppDatabase
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.utils.AppExecutors
import com.pavelprymak.propodcast.utils.firebase.FirebaseAnalyticsHelper
import java.util.concurrent.Executor

class DbRepoImpl(db: AppDatabase, executors: AppExecutors) : DbRepo {
    private val mDb: AppDatabase = db
    private val diskIO: Executor = executors.diskIO()

    //PODCASTS

    override fun getFavoritePodcasts(): LiveData<List<FavoritePodcastEntity>> {
        return mDb.favoritePodcastDao().loadAllPodcasts()
    }

    override fun getFavoritePodcastById(id: String): LiveData<FavoritePodcastEntity> {
        return mDb.favoritePodcastDao().loadPodcastById(id)
    }

    override fun insertPodcast(podcastEntity: FavoritePodcastEntity) {
        diskIO.execute { mDb.favoritePodcastDao().insertPodcast(podcastEntity) }
        FirebaseAnalyticsHelper.sentAnalyticAddToFavorite(podcastEntity)
    }

    override fun updatePodcast(podcastEntity: FavoritePodcastEntity) {
        diskIO.execute { mDb.favoritePodcastDao().updatePodcast(podcastEntity) }
    }

    override fun deletePodcastById(podcastId: String) {
        diskIO.execute { mDb.favoritePodcastDao().deletePodcast(podcastId) }
        FirebaseAnalyticsHelper.sentAnalyticRemoveFromFavorite(podcastId)
    }

    override fun getPodcastsCount(): LiveData<Int> {
        return mDb.favoritePodcastDao().getRowCount()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////


    //EPISODES


    override fun getFavoriteEpisodes(): LiveData<List<FavoriteEpisodeEntity>> {
        return mDb.favoriteEpisodeDao().loadAllEpisodes()
    }

    override fun getFavoriteEpisodeById(id: String): LiveData<FavoriteEpisodeEntity> {
        return mDb.favoriteEpisodeDao().loadEpisodeById(id)
    }

    override fun insertEpisode(episodeEntity: FavoriteEpisodeEntity) {
        diskIO.execute { mDb.favoriteEpisodeDao().insertEpisode(episodeEntity) }
    }

    override fun updateEpisode(episodeEntity: FavoriteEpisodeEntity) {
        diskIO.execute { mDb.favoriteEpisodeDao().updateEpisode(episodeEntity) }
    }

    override fun deleteEpisodeById(episodeId: String) {
        diskIO.execute { mDb.favoriteEpisodeDao().deleteEpisodeById(episodeId) }
    }

    override fun getEpisodesCount(): LiveData<Int> {
        return mDb.favoriteEpisodeDao().getRowCount()
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
}
