package com.pavelprymak.propodcast.model.network.pojo.podcasts

import com.squareup.moshi.Json


data class BestPodcastsResponse(

    @Json(name = "total")
    var total: Int = 0,

    @Json(name = "podcasts")
    var podcasts: List<PodcastItem>? = null,

    @Json(name = "page_number")
    var pageNumber: Int = 0,

    @Json(name = "has_previous")
    var isHasPrevious: Boolean = false,

    @Json(name = "parent_id")
    var parentId: Any? = null,

    @Json(name = "next_page_number")
    var nextPageNumber: Int = 0,

    @Json(name = "name")
    var name: String? = null,

    @Json(name = "has_next")
    var isHasNext: Boolean = false,

    @Json(name = "id")
    var id: Int = 0,

    @Json(name = "listennotes_url")
    var listennotesUrl: String? = null,

    @Json(name = "previous_page_number")
    var previousPageNumber: Int = 0


)