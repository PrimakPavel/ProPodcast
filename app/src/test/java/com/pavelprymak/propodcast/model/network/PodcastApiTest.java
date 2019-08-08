package com.pavelprymak.propodcast.model.network;

import com.pavelprymak.propodcast.BuildConfig;
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse;
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse;
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;

public class PodcastApiTest {
    private PodcastApi api;

    @Before
    public void beforeRequests() {
        api = PodcastApiController.INSTANCE.getPodcastApi();
    }

    @Test
    public void getGenres() throws Exception {
        Response<GenresResponse> response = api.getGenres(BuildConfig.API_KEY).execute();
        GenresResponse genresResponse = response.body();
        assertNotNull(genresResponse);
    }

    @Test
    public void getRegions() throws Exception {
        Response<RegionsResponse> response = api.getRegions(BuildConfig.API_KEY).execute();
        RegionsResponse regionsResponse = response.body();
        assertNotNull(regionsResponse);
    }

    @Test
    public void getBestPodcasts() throws Exception {
        Response<BestPodcastsResponse> response = api.getBestPodcasts(BuildConfig.API_KEY, 93, 26, "us").execute();
        BestPodcastsResponse bestPodcastsResponse = response.body();
        assertNotNull(bestPodcastsResponse);
    }

    @Test
    public void getPodcastById() throws Exception {
        Response<PodcastResponse> response = api.getPodcastById(BuildConfig.API_KEY, "2623fce10ba346a79c1656705d46492c", 0, SortType.SORT_RESENT_FIRST).execute();
        PodcastResponse podcastResponse = response.body();
        assertNotNull(podcastResponse);
    }

    @Test
    public void getPodcastRecommendationsById() throws Exception {
        Response<RecommendationsResponse> response = api.getPodcastRecommendationsById(BuildConfig.API_KEY, "2623fce10ba346a79c1656705d46492c").execute();
        RecommendationsResponse podcastRecommendationsResponse = response.body();
        assertNotNull(podcastRecommendationsResponse);
    }

    @Test
    public void getPodcastSearch() throws Exception {
        int[] genres = new int[0];
        Response<SearchPodcastResponse> response = api.searchPodcast(BuildConfig.API_KEY, "star wars", SearchType.SEARCH_PODCASTS, 0, genres, "English").execute();
        SearchPodcastResponse search = response.body();
        assertNotNull(search);
    }


}
