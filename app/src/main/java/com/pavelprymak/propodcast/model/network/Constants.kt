package com.pavelprymak.propodcast.model.network


object SortType {
    const val SORT_RESENT_FIRST = "recent_first"
    const val SORT_OLDEST_FIRST = "oldest_first"
}

object SearchType {
    const val SEARCH_PODCASTS = "podcast"
    const val SEARCH_EPISODE = "episode"
}

object ApiErrorCodes {
    const val WRONG_API_KEY = 401
    const val WRONG_ITEM_ID = 404
    const val FREE_PLAN_QUOTA_LIMIT = 429
    const val SOMETHING_WRONG = 500
}

object Constants {
    const val BASE_URL = "https://listen-api.listennotes.com/api/v2/"
    const val AUTH_HEADER = "X-ListenAPI-Key"

    const val PATH_GENRES = "genres"
    const val PATH_REGIONS = "regions"
    const val PATH_BEST_PODCASTS = "best_podcasts"
    const val PATH_PODCASTS = "podcasts/"
    const val PATH_SEARCH = "search"
    const val PATH_RECOMMENDATIONS = "/recommendations"

    const val QUERY_GENRE_ID = "genre_id"
    const val QUERY_PAGE = "page"
    const val QUERY_REGION = "region"
    const val QUERY_SAFE_MODE = "safe_mode"
    const val QUERY_NEXT_EPISODE_PUB_DATE = "next_episode_pub_date"
    const val QUERY_SORT = "sort"
    const val QUERY_Q = "q"
    const val QUERY_TYPE = "type"
    const val QUERY_OFFSET = "offset"
    const val QUERY_GENDER_IDS = "genre_ids"
    const val QUERY_LANGUAGE = "language"
}

