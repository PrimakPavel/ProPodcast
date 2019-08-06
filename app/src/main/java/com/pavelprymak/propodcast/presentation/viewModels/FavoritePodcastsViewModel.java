package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.db.repo.DbRepo;

import java.util.List;

public class FavoritePodcastsViewModel extends ViewModel {
    private LiveData<List<FavoritePodcastEntity>> mFavorites;
    private final DbRepo dbRepo = App.dbRepo;

    public LiveData<List<FavoritePodcastEntity>> getFavorites() {
        if (mFavorites == null) {
            mFavorites = dbRepo.getFavoritePodcasts();
        }
        return mFavorites;
    }

    public void addToFavorite(FavoritePodcastEntity favoritePodcast) {
        dbRepo.insertPodcast(favoritePodcast);
    }

    public void removeFromFavorite(String favoritePodcastId) {
        dbRepo.deletePodcastById(favoritePodcastId);
    }

    public LiveData<FavoritePodcastEntity> getFavoriteById(String podcastId) {
        return dbRepo.getFavoritePodcastById(podcastId);
    }

    public boolean isFavorite(List<FavoritePodcastEntity> favorites, String podcastId) {
        if (favorites != null && !favorites.isEmpty()) {
            for (FavoritePodcastEntity favoritePodcastEntity : favorites) {
                if (favoritePodcastEntity.getId().equals(podcastId)) return true;
            }
        }
        return false;
    }
}
