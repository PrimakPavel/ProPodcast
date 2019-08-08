package com.pavelprymak.propodcast.model.network

import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PodcastApi {
    @GET(Constants.PATH_GENRES)
    fun getGenres(@Header(Constants.AUTH_HEADER) apiKey: String): Call<GenresResponse>

    @GET(Constants.PATH_GENRES)
    fun getGenresRx(@Header(Constants.AUTH_HEADER) apiKey: String): Single<GenresResponse>


    @GET(Constants.PATH_REGIONS)
    fun getRegions(@Header(Constants.AUTH_HEADER) apiKey: String): Call<RegionsResponse>

    @GET(Constants.PATH_REGIONS)
    fun getRegionsRx(@Header(Constants.AUTH_HEADER) apiKey: String): Single<RegionsResponse>

    @GET(Constants.PATH_BEST_PODCASTS)
    fun getBestPodcasts(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Query(Constants.QUERY_GENRE_ID) genreId: Int,
        @Query(Constants.QUERY_PAGE) page: Int,
        @Query(Constants.QUERY_REGION) region: String
    ): Call<BestPodcastsResponse>

    @GET(Constants.PATH_BEST_PODCASTS)
    fun getBestPodcastsRx(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Query(Constants.QUERY_GENRE_ID) genreId: Int,
        @Query(Constants.QUERY_PAGE) page: Int,
        @Query(Constants.QUERY_REGION) region: String
    ): Single<BestPodcastsResponse>

    @GET(Constants.PATH_PODCASTS + "{id}")
    fun getPodcastById(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Path("id") podcastId: String,
        @Query(Constants.QUERY_NEXT_EPISODE_PUB_DATE) nextEpisodePubDate: Long,
        @Query(Constants.QUERY_SORT) sortType: String
    ): Call<PodcastResponse>

    @GET(Constants.PATH_PODCASTS + "{id}")
    fun getPodcastByIdRx(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Path("id") podcastId: String,
        @Query(Constants.QUERY_NEXT_EPISODE_PUB_DATE) nextEpisodePubDate: Long,
        @Query(Constants.QUERY_SORT) sortType: String
    ): Single<PodcastResponse>

    @GET(Constants.PATH_PODCASTS + "{id}" + Constants.PATH_RECOMMENDATIONS)
    fun getPodcastRecommendationsById(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Path("id") podcastId: String
    ): Call<RecommendationsResponse>

    @GET(Constants.PATH_PODCASTS + "{id}" + Constants.PATH_RECOMMENDATIONS)
    fun getPodcastRecommendationsByIdRx(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Path("id") podcastId: String
    ): Single<RecommendationsResponse>

    @GET(Constants.PATH_SEARCH)
    fun searchPodcast(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Query(Constants.QUERY_Q) searchQuery: String,
        @Query(Constants.QUERY_TYPE) type: String,
        @Query(Constants.QUERY_OFFSET) offset: Int,
        @Query(Constants.QUERY_GENDER_IDS) genreIds: IntArray,
        @Query(Constants.QUERY_LANGUAGE) language: String
    ): Call<SearchPodcastResponse>

    @GET(Constants.PATH_SEARCH)
    fun searchPodcastRx(
        @Header(Constants.AUTH_HEADER) apiKey: String,
        @Query(Constants.QUERY_Q) searchQuery: String,
        @Query(Constants.QUERY_TYPE) type: String,
        @Query(Constants.QUERY_OFFSET) offset: Int,
        @Query(Constants.QUERY_GENDER_IDS) genreIds: IntArray,
        @Query(Constants.QUERY_LANGUAGE) language: String
    ): Single<SearchPodcastResponse>
}
