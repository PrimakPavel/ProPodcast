package com.pavelprymak.propodcast.model.network.repo

import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse

import io.reactivex.Single

interface PodcastRepoRx {

    fun getGenres(): Single<GenresResponse>

    fun getRegions(): Single<RegionsResponse>

    fun getBestPodcasts(genderId: Int, region: String, page: Int): Single<BestPodcastsResponse>

    fun getPodcastById(podcastId: String, nextEpisodesPubDate: Long): Single<PodcastResponse>

    fun getPodcastRecommendations(podcastId: String): Single<RecommendationsResponse>

    fun getSearchData(q: String, offset: Int, language: String): Single<SearchPodcastResponse>

}
