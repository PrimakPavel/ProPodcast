package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.model.db.repo.DbRepo;

import java.util.List;

public class FavoriteEpisodesViewModel extends ViewModel {
    private LiveData<List<FavoriteEpisodeEntity>> mFavorites;
    private DbRepo dbRepo = App.dbRepo;

    public LiveData<List<FavoriteEpisodeEntity>> getFavorites() {
        if (mFavorites == null) {
            mFavorites = dbRepo.getFavoriteEpisodes();
        }
        return mFavorites;
    }

    public void addToFavorite(FavoriteEpisodeEntity favoriteEpisode) {
        dbRepo.insertEpisode(favoriteEpisode);
    }

    public void removeFromFavorite(String favoriteEpisodeId) {
        dbRepo.deleteEpisodeById(favoriteEpisodeId);
    }

    public LiveData<FavoriteEpisodeEntity> getFavoriteById(String episodeId) {
        return dbRepo.getFavoriteEpisodeById(episodeId);
    }

    public boolean isFavorite(List<FavoriteEpisodeEntity> favorites, String episodeId) {
        if (favorites != null && !favorites.isEmpty()) {
            for (FavoriteEpisodeEntity favoriteEpisodeEntity : favorites) {
                if (favoriteEpisodeEntity.getId().equals(episodeId)) return true;
            }
        }
        return false;
    }
}
