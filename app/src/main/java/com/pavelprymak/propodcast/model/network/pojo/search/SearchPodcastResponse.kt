package com.pavelprymak.propodcast.model.network.pojo.search

import com.squareup.moshi.Json


data class SearchPodcastResponse(

    @Json(name = "took")
    var took: Double = 0.0,

    @Json(name = "total")
    var total: Int = 0,

    @Json(name = "count")
    var count: Int = 0,

    @Json(name = "next_offset")
    var nextOffset: Int = 0,

    @Json(name = "results")
    var results: List<ResultsItem>? = null

)