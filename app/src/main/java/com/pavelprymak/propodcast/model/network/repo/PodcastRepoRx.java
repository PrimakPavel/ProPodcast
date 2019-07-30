package com.pavelprymak.propodcast.model.network.repo;

import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse;
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse;
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse;

import io.reactivex.Single;

public interface PodcastRepoRx {

    Single<GenresResponse> getGenres();

    Single<RegionsResponse> getRegions();

    Single<BestPodcastsResponse> getBestPodcasts(int genderId, String region, int page);

    Single<PodcastResponse> getPodcastById(String podcastId, long nextEpisodesPubDate);

    Single<RecommendationsResponse> getPodcastRecommendations(String podcastId);

    Single<SearchPodcastResponse> getSearchData(String q, int offset, String language);

}
