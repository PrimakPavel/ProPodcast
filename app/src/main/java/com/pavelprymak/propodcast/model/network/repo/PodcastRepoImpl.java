package com.pavelprymak.propodcast.model.network.repo;

import com.pavelprymak.propodcast.BuildConfig;
import com.pavelprymak.propodcast.model.network.Constants;
import com.pavelprymak.propodcast.model.network.PodcastApi;
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse;
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse;
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse;

import io.reactivex.Single;

public class PodcastRepoImpl implements PodcastRepoRx {
    private PodcastApi mApi;
    private static final String API_KEY = BuildConfig.API_KEY;

    public PodcastRepoImpl(PodcastApi api) {
        mApi = api;
    }

    @Override
    public Single<GenresResponse> getGenres() {
        return mApi.getGenresRx(API_KEY);
    }

    @Override
    public Single<RegionsResponse> getRegions() {
        return mApi.getRegionsRx(API_KEY);
    }

    @Override
    public Single<BestPodcastsResponse> getBestPodcasts(int genderId, String region, int page) {
        return mApi.getBestPodcastsRx(API_KEY, genderId, page, region);
    }

    @Override
    public Single<PodcastResponse> getPodcastById(String podcastId, long nextEpisodesPubDate) {
        return mApi.getPodcastByIdRx(API_KEY, podcastId, nextEpisodesPubDate, Constants.SortType.SORT_RESENT_FIRST);
    }

    @Override
    public Single<RecommendationsResponse> getPodcastRecommendations(String podcastId) {
        return mApi.getPodcastRecommendationsByIdRx(API_KEY, podcastId);
    }

    @Override
    public Single<SearchPodcastResponse> getSearchData(String q, int offset, String language) {
        int[] genres = new int[0];
        return mApi.searchPodcastRx(API_KEY, q, Constants.SearchType.SEARCH_PODCASTS, offset, genres, language);
    }
}
