package com.pavelprymak.propodcast.model.network;

public class Constants {
    static final String BASE_URL = "https://listen-api.listennotes.com/api/v2/";
    static final String AUTH_HEADER = "X-ListenAPI-Key";

    static final String PATH_GENRES = "genres";
    static final String PATH_REGIONS = "regions";
    static final String PATH_BEST_PODCASTS = "best_podcasts";
    static final String PATH_PODCASTS = "podcasts/";
    static final String PATH_SEARCH = "search";
    static final String PATH_RECOMMENDATIONS = "/recommendations";

    static final String QUERY_GENRE_ID = "genre_id";
    static final String QUERY_PAGE = "page";
    static final String QUERY_REGION = "region";
    static final String QUERY_SAFE_MODE = "safe_mode";
    static final String QUERY_NEXT_EPISODE_PUB_DATE = "next_episode_pub_date";
    static final String QUERY_SORT = "sort";
    static final String QUERY_Q = "q";
    static final String QUERY_TYPE = "type";
    static final String QUERY_OFFSET = "offset";
    static final String QUERY_GENDER_IDS = "genre_ids";
    static final String QUERY_LANGUAGE = "language";

    public class SortType {
        public static final String SORT_RESENT_FIRST = "recent_first";
        public static final String SORT_OLDEST_FIRST = "oldest_first";
    }

    public class SearchType {
        public static final String SEARCH_PODCASTS = "podcast";
        public static final String SEARCH_EPISODE = "episode";
    }

    public class ApiErrorCodes {
        public static final int WRONG_API_KEY = 401;
        public static final int WRONG_ITEM_ID = 404;
        public static final int FREE_PLAN_QUOTA_LIMIT = 429;
        public static final int SOMETHING_WRONG = 500;
    }

}
