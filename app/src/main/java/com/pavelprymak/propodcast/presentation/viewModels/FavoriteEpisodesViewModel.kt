package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.model.db.repo.DbRepo

class FavoriteEpisodesViewModel(private val dbRepo: DbRepo) : ViewModel() {
    private var mFavorites: LiveData<List<FavoriteEpisodeEntity>>? = null

    val favorites: LiveData<List<FavoriteEpisodeEntity>>
        get() {
            if (mFavorites == null) {
                mFavorites = dbRepo.getFavoriteEpisodes()
            }
            return mFavorites!!
        }

    fun addToFavorite(favoriteEpisode: FavoriteEpisodeEntity) {
        dbRepo.insertEpisode(favoriteEpisode)
    }

    fun removeFromFavorite(favoriteEpisodeId: String) {
        dbRepo.deleteEpisodeById(favoriteEpisodeId)
    }

    fun getFavoriteById(episodeId: String): LiveData<FavoriteEpisodeEntity> {
        return dbRepo.getFavoriteEpisodeById(episodeId)
    }

    fun isFavorite(favorites: List<FavoriteEpisodeEntity>?, episodeId: String): Boolean {
        if (favorites != null && favorites.isNotEmpty()) {
            for ((id) in favorites) {
                if (id == episodeId) return true
            }
        }
        return false
    }
}
