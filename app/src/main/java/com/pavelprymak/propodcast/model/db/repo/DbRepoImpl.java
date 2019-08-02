package com.pavelprymak.propodcast.model.db.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.pavelprymak.propodcast.model.db.AppDatabase;
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.utils.firebase.AnalyticsHelper;

import java.util.List;
import java.util.concurrent.Executor;

public class DbRepoImpl implements DbRepo {

    private final AppDatabase mDb;
    private final Executor diskIO;

    public DbRepoImpl(@NonNull AppDatabase appDatabase, @NonNull Executor discIOExecutor) {
        mDb = appDatabase;
        diskIO = discIOExecutor;
    }

    //PODCASTS

    @Override
    public LiveData<List<FavoritePodcastEntity>> getFavoritePodcasts() {
        return mDb.favoritePodcastDao().loadAllPodcasts();
    }

    @Override
    public LiveData<FavoritePodcastEntity> getFavoritePodcastById(String id) {
        return mDb.favoritePodcastDao().loadPodcastById(id);
    }

    @Override
    public void insertPodcast(FavoritePodcastEntity podcastEntity) {
        diskIO.execute(() -> mDb.favoritePodcastDao().insertPodcast(podcastEntity));
        AnalyticsHelper.sentFirebaseAnalyticAddToFavorite(podcastEntity);
    }

    @Override
    public void updatePodcast(FavoritePodcastEntity podcastEntity) {
        diskIO.execute(() -> mDb.favoritePodcastDao().updatePodcast(podcastEntity));
    }

    @Override
    public void deletePodcastById(String podcastId) {
        diskIO.execute(() -> mDb.favoritePodcastDao().deletePodcast(podcastId));
        AnalyticsHelper.sentFirebaseAnalyticRemoveFromFavorite(podcastId);
    }

    @Override
    public LiveData<Integer> getPodcastsCount() {
        return mDb.favoritePodcastDao().getRowCount();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////


    //EPISODES


    @Override
    public LiveData<List<FavoriteEpisodeEntity>> getFavoriteEpisodes() {
        return mDb.favoriteEpisodeDao().loadAllEpisodes();
    }

    @Override
    public LiveData<FavoriteEpisodeEntity> getFavoriteEpisodeById(String id) {
        return mDb.favoriteEpisodeDao().loadEpisodeById(id);
    }

    @Override
    public void insertEpisode(FavoriteEpisodeEntity episodeEntity) {
        diskIO.execute(() -> mDb.favoriteEpisodeDao().insertEpisode(episodeEntity));
    }

    @Override
    public void updateEpisode(FavoriteEpisodeEntity episodeEntity) {
        diskIO.execute(() -> mDb.favoriteEpisodeDao().updateEpisode(episodeEntity));
    }

    @Override
    public void deleteEpisodeById(String episodeId) {
        diskIO.execute(() -> mDb.favoriteEpisodeDao().deleteEpisodeById(episodeId));
    }

    @Override
    public LiveData<Integer> getEpisodesCount() {
        return mDb.favoriteEpisodeDao().getRowCount();
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////
}
