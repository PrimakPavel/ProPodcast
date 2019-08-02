package com.pavelprymak.propodcast.utils.firebase;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;

public class AnalyticsHelper {

    private static final String ANALYTIC_TYPE_EPISODE = "episode";
    private static final String ANALYTIC_TYPE_PODCAST_ADD_TO_FAVORITE = "podcastAddToFavorite";
    private static final String ANALYTIC_TYPE_PODCAST_REMOVE_FROM_FAVORITE = "podcastRemoveFromFavorite";
    private static final String ANALYTIC_TYPE_SEARCH = "searchQuery";

    public static void sentFirebaseAnalyticSearchQueryData(String searchQuery, String filterLanguage) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, searchQuery);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, filterLanguage);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_SEARCH);
        if (App.mFirebaseAnalytics != null)
            App.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    public static void sentFirebaseAnalyticEpisodeData(EpisodesItem episodesItem) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, episodesItem.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, episodesItem.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_EPISODE);
        if (App.mFirebaseAnalytics != null)
            App.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void sentFirebaseAnalyticEpisodeData(FavoriteEpisodeEntity episodesItem) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, episodesItem.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, episodesItem.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_EPISODE);
        if (App.mFirebaseAnalytics != null)
            App.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void sentFirebaseAnalyticAddToFavorite(FavoritePodcastEntity podcast) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, podcast.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, podcast.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_PODCAST_ADD_TO_FAVORITE);
        if (App.mFirebaseAnalytics != null)
            App.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void sentFirebaseAnalyticRemoveFromFavorite(String podcastId) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, podcastId);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, ANALYTIC_TYPE_PODCAST_REMOVE_FROM_FAVORITE);
        if (App.mFirebaseAnalytics != null)
            App.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
