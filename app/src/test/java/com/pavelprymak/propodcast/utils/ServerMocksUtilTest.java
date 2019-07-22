package com.pavelprymak.propodcast.utils;

import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse;
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionsResponse;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ServerMocksUtilTest {

    @Test
    public void testGenresMock() throws IOException {
        GenresResponse genresResponse = ServerMocksUtil.getGenresMock();
        assertNotNull(genresResponse);
    }

    @Test
    public void testRegionsMock() throws IOException {
        RegionsResponse regionsResponse = ServerMocksUtil.getRegionMock();
        assertNotNull(regionsResponse);
    }

    @Test
    public void getBestPodcastsMock() throws IOException {
        BestPodcastsResponse bestPodcastsResponse = ServerMocksUtil.getBestPodcastsMock();
        assertNotNull(bestPodcastsResponse);
    }

    @Test
    public void getPodcastMock() throws IOException {
        PodcastResponse podcastsResponse = ServerMocksUtil.getPodcastMock();
        assertNotNull(podcastsResponse);
    }

    @Test
    public void getRecommendationMock() throws IOException {
        RecommendationsResponse recommendationsResponse = ServerMocksUtil.getRecommendationMock();
        assertNotNull(recommendationsResponse);
    }
}
