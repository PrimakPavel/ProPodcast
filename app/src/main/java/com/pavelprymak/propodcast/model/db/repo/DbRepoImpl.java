package com.pavelprymak.propodcast.model.db.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.pavelprymak.propodcast.model.db.AppDatabase;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;

import java.util.List;
import java.util.concurrent.Executor;

public class DbRepoImpl implements DbRepo {

    private final AppDatabase mDb;
    private final Executor diskIO;

    public DbRepoImpl(@NonNull AppDatabase appDatabase, @NonNull Executor discIOExecutor) {
        mDb = appDatabase;
        diskIO = discIOExecutor;
    }

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
    }

    @Override
    public void updatePodcast(FavoritePodcastEntity podcastEntity) {
        diskIO.execute(() -> mDb.favoritePodcastDao().updatePodcast(podcastEntity));
    }

    @Override
    public void deletePodcastById(String podcastId) {
        diskIO.execute(() -> mDb.favoritePodcastDao().deletePodcast(podcastId));
    }

    @Override
    public LiveData<Integer> getPodcastsCount() {
        return mDb.favoritePodcastDao().getRowCount();
    }
}
