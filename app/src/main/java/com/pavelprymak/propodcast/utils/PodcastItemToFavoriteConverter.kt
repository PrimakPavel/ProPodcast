package com.pavelprymak.propodcast.utils

import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem

object PodcastItemToFavoriteConverter {
    fun createFavorite(podcastItem: PodcastItem): FavoritePodcastEntity {
        val favoritePodcastEntity = FavoritePodcastEntity()
        favoritePodcastEntity.id = podcastItem.id
        favoritePodcastEntity.image = podcastItem.image
        favoritePodcastEntity.country = podcastItem.country
        favoritePodcastEntity.description = podcastItem.description?.trim { it <= ' ' }
        favoritePodcastEntity.publisher = podcastItem.publisher?.trim { it <= ' ' }
        favoritePodcastEntity.isClaimed = podcastItem.isIsClaimed
        favoritePodcastEntity.earliestPubDateMs = podcastItem.earliestPubDateMs
        favoritePodcastEntity.language = podcastItem.language
        favoritePodcastEntity.thumbnail = podcastItem.thumbnail
        favoritePodcastEntity.email = podcastItem.email
        favoritePodcastEntity.genreIds = podcastItem.genreIds
        favoritePodcastEntity.itunesId = podcastItem.itunesId
        favoritePodcastEntity.listennotesUrl = podcastItem.listennotesUrl
        favoritePodcastEntity.rss = podcastItem.rss
        favoritePodcastEntity.title = podcastItem.title?.trim { it <= ' ' }
        favoritePodcastEntity.totalEpisodes = podcastItem.totalEpisodes
        favoritePodcastEntity.website = podcastItem.website
        return favoritePodcastEntity
    }

    fun createFavorite(podcastItem: PodcastResponse): FavoritePodcastEntity {
        val favoritePodcastEntity = FavoritePodcastEntity()
        favoritePodcastEntity.id = podcastItem.id ?: ""
        favoritePodcastEntity.image = podcastItem.image
        favoritePodcastEntity.country = podcastItem.country
        favoritePodcastEntity.description = podcastItem.description?.trim { it <= ' ' }
        favoritePodcastEntity.publisher = podcastItem.publisher?.trim { it <= ' ' }
        favoritePodcastEntity.isClaimed = podcastItem.isIsClaimed
        favoritePodcastEntity.earliestPubDateMs = podcastItem.earliestPubDateMs
        favoritePodcastEntity.language = podcastItem.language
        favoritePodcastEntity.thumbnail = podcastItem.thumbnail
        favoritePodcastEntity.email = podcastItem.email
        favoritePodcastEntity.genreIds = podcastItem.genreIds
        favoritePodcastEntity.itunesId = podcastItem.itunesId
        favoritePodcastEntity.listennotesUrl = podcastItem.listennotesUrl
        favoritePodcastEntity.rss = podcastItem.rss
        favoritePodcastEntity.title = podcastItem.title?.trim { it <= ' ' }
        favoritePodcastEntity.totalEpisodes = podcastItem.totalEpisodes
        favoritePodcastEntity.website = podcastItem.website
        return favoritePodcastEntity
    }

    fun createFavorite(podcastItem: ResultsItem): FavoritePodcastEntity {
        val favoritePodcastEntity = FavoritePodcastEntity()
        favoritePodcastEntity.id = podcastItem.id
        favoritePodcastEntity.image = podcastItem.image
        favoritePodcastEntity.description = podcastItem.descriptionOriginal?.trim { it <= ' ' }
        favoritePodcastEntity.publisher = podcastItem.publisherOriginal?.trim { it <= ' ' }
        favoritePodcastEntity.earliestPubDateMs = podcastItem.earliestPubDateMs
        favoritePodcastEntity.thumbnail = podcastItem.thumbnail
        favoritePodcastEntity.email = podcastItem.email
        favoritePodcastEntity.genreIds = podcastItem.genreIds
        favoritePodcastEntity.itunesId = podcastItem.itunesId
        favoritePodcastEntity.listennotesUrl = podcastItem.listennotesUrl
        favoritePodcastEntity.rss = podcastItem.rss
        favoritePodcastEntity.title = podcastItem.titleOriginal?.trim { it <= ' ' }
        favoritePodcastEntity.totalEpisodes = podcastItem.totalEpisodes
        return favoritePodcastEntity
    }

    fun createFavorite(episodesItem: EpisodesItem): FavoriteEpisodeEntity {
        val favoriteEpisodeEntity = FavoriteEpisodeEntity()
        favoriteEpisodeEntity.id = episodesItem.id ?: ""
        favoriteEpisodeEntity.image = episodesItem.image
        favoriteEpisodeEntity.description = episodesItem.description?.trim { it <= ' ' }
        favoriteEpisodeEntity.thumbnail = episodesItem.thumbnail
        favoriteEpisodeEntity.listennotesUrl = episodesItem.listennotesUrl
        favoriteEpisodeEntity.title = episodesItem.title?.trim { it <= ' ' }
        favoriteEpisodeEntity.audio = episodesItem.audio
        favoriteEpisodeEntity.pubDateMs = episodesItem.pubDateMs
        favoriteEpisodeEntity.audioLengthSec = episodesItem.audioLengthSec
        return favoriteEpisodeEntity
    }
}
