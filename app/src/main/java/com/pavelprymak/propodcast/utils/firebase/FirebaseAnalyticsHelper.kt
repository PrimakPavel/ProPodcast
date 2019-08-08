package com.pavelprymak.propodcast.utils.firebase

import android.os.Bundle

import com.google.firebase.analytics.FirebaseAnalytics
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem

private const val ANALYTIC_TYPE_EPISODE = "episode"
private const val ANALYTIC_TYPE_PODCAST_ADD_TO_FAVORITE = "podcastAddToFavorite"
private const val ANALYTIC_TYPE_PODCAST_REMOVE_FROM_FAVORITE = "podcastRemoveFromFavorite"
private const val ANALYTIC_TYPE_SEARCH = "searchQuery"

object FirebaseAnalyticsHelper {

    fun sentAnalyticSearchQueryData(searchQuery: String, filterLanguage: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, searchQuery)
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, filterLanguage)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_SEARCH)
        App.mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }

    fun sentAnalyticEpisodeData(episodesItem: EpisodesItem) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, episodesItem.id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, episodesItem.title)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_EPISODE)
        App.mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun sentAnalyticEpisodeData(episodesItem: FavoriteEpisodeEntity) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, episodesItem.id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, episodesItem.title)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_EPISODE)
        App.mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun sentAnalyticAddToFavorite(podcast: FavoritePodcastEntity) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, podcast.id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, podcast.title)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_PODCAST_ADD_TO_FAVORITE)
        App.mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun sentAnalyticRemoveFromFavorite(podcastId: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, podcastId)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_PODCAST_REMOVE_FROM_FAVORITE)
        App.mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}
