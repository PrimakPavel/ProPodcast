package com.pavelprymak.propodcast.model.network;

import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse;
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PodcastApi {
    @GET(Constants.PATH_GENRES)
    Call<GenresResponse> getGenres(@Header(Constants.AUTH_HEADER) String apiKey);

    @GET(Constants.PATH_GENRES)
    Single<GenresResponse> getGenresRx(@Header(Constants.AUTH_HEADER) String apiKey);


    @GET(Constants.PATH_REGIONS)
    Call<RegionsResponse> getRegions(@Header(Constants.AUTH_HEADER) String apiKey);

    @GET(Constants.PATH_REGIONS)
    Single<RegionsResponse> getRegionsRx(@Header(Constants.AUTH_HEADER) String apiKey);

    @GET(Constants.PATH_BEST_PODCASTS)
    Call<BestPodcastsResponse> getBestPodcasts(@Header(Constants.AUTH_HEADER) String apiKey,
                                               @Query(Constants.QUERY_GENRE_ID) int genreId,
                                               @Query(Constants.QUERY_PAGE) int page,
                                               @Query(Constants.QUERY_REGION) String region);

    @GET(Constants.PATH_BEST_PODCASTS)
    Single<BestPodcastsResponse> getBestPodcastsRx(@Header(Constants.AUTH_HEADER) String apiKey,
                                                   @Query(Constants.QUERY_GENRE_ID) int genreId,
                                                   @Query(Constants.QUERY_PAGE) int page,
                                                   @Query(Constants.QUERY_REGION) String region);

    @GET(Constants.PATH_PODCASTS + "{id}")
    Call<PodcastResponse> getPodcastById(@Header(Constants.AUTH_HEADER) String apiKey,
                                         @Path("id") String podcastId,
                                         @Query(Constants.QUERY_NEXT_EPISODE_PUB_DATE) long nextEpisodePubDate,
                                         @Query(Constants.QUERY_SORT) String sortType);

    @GET(Constants.PATH_PODCASTS + "{id}")
    Single<PodcastResponse> getPodcastByIdRx(@Header(Constants.AUTH_HEADER) String apiKey,
                                             @Path("id") String podcastId,
                                             @Query(Constants.QUERY_NEXT_EPISODE_PUB_DATE) long nextEpisodePubDate,
                                             @Query(Constants.QUERY_SORT) String sortType);

    @GET(Constants.PATH_PODCASTS + "{id}" + Constants.PATH_RECOMMENDATIONS)
    Call<RecommendationsResponse> getPodcastRecommendationsById(@Header(Constants.AUTH_HEADER) String apiKey,
                                                                @Path("id") String podcastId);

    @GET(Constants.PATH_PODCASTS + "{id}" + Constants.PATH_RECOMMENDATIONS)
    Single<RecommendationsResponse> getPodcastRecommendationsByIdRx(@Header(Constants.AUTH_HEADER) String apiKey,
                                                                    @Path("id") String podcastId);
}
