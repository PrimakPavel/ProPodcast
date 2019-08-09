package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.model.db.repo.DbRepo

class FavoritePodcastsViewModel(private val dbRepo: DbRepo) : ViewModel() {
    private var mFavorites: LiveData<List<FavoritePodcastEntity>>? = null

    val favorites: LiveData<List<FavoritePodcastEntity>>
        get() {
            if (mFavorites == null) {
                mFavorites = dbRepo.getFavoritePodcasts()
            }
            return mFavorites!!
        }

    fun addToFavorite(favoritePodcast: FavoritePodcastEntity) {
        dbRepo.insertPodcast(favoritePodcast)
    }

    fun removeFromFavorite(favoritePodcastId: String) {
        dbRepo.deletePodcastById(favoritePodcastId)
    }

    fun getFavoriteById(podcastId: String): LiveData<FavoritePodcastEntity> {
        return dbRepo.getFavoritePodcastById(podcastId)
    }

    fun isFavorite(favorites: List<FavoritePodcastEntity>?, podcastId: String): Boolean {
        if (!favorites.isNullOrEmpty()) {
            for ((id) in favorites) {
                if (id == podcastId) return true
            }
        }
        return false
    }
}
