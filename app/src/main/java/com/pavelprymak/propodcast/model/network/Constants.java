package com.pavelprymak.propodcast.model.network;

public class Constants {
    static final String BASE_URL = "https://listen-api.listennotes.com/api/v2/";
    static final String AUTH_HEADER = "X-ListenAPI-Key";

    static final String PATH_GENRES = "genres";
    static final String PATH_REGIONS = "regions";
    static final String PATH_BEST_PODCASTS = "best_podcasts";
    static final String PATH_PODCASTS = "podcasts/";
    static final String PATH_RECOMMENDATIONS = "/recommendations";

    static final String QUERY_GENRE_ID = "genre_id";
    static final String QUERY_PAGE = "page";
    static final String QUERY_REGION = "region";
    static final String QUERY_SAFE_MODE = "safe_mode";
    static final String QUERY_NEXT_EPISODE_PUB_DATE = "next_episode_pub_date";
    static final String QUERY_SORT = "sort";

    public class SortType {
        public static final String SORT_RESENT_FIRST = "recent_first";
        public static final String SORT_OLDEST_FIRST = "oldest_first";
    }

}
