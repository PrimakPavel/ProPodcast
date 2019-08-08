package com.pavelprymak.propodcast.model.network.repo

import com.pavelprymak.propodcast.BuildConfig
import com.pavelprymak.propodcast.model.network.Constants
import com.pavelprymak.propodcast.model.network.PodcastApi
import com.pavelprymak.propodcast.model.network.SearchType
import com.pavelprymak.propodcast.model.network.SortType
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse

import io.reactivex.Single

class PodcastRepoImpl(private val mApi: PodcastApi) : PodcastRepoRx {

    override fun getGenres(): Single<GenresResponse> {
        return mApi.getGenresRx(API_KEY)
    }

    override fun getRegions(): Single<RegionsResponse> {
        return mApi.getRegionsRx(API_KEY)
    }

    override fun getBestPodcasts(genderId: Int, region: String, page: Int): Single<BestPodcastsResponse> {
        return mApi.getBestPodcastsRx(API_KEY, genderId, page, region)
    }

    override fun getPodcastById(podcastId: String, nextEpisodesPubDate: Long): Single<PodcastResponse> {
        return mApi.getPodcastByIdRx(API_KEY, podcastId, nextEpisodesPubDate, SortType.SORT_RESENT_FIRST)
    }

    override fun getPodcastRecommendations(podcastId: String): Single<RecommendationsResponse> {
        return mApi.getPodcastRecommendationsByIdRx(API_KEY, podcastId)
    }

    override fun getSearchData(q: String, offset: Int, language: String): Single<SearchPodcastResponse> {
        val genres = IntArray(0)
        return mApi.searchPodcastRx(API_KEY, q, SearchType.SEARCH_PODCASTS, offset, genres, language)
    }

    companion object {
        private val API_KEY = BuildConfig.API_KEY
    }
}
