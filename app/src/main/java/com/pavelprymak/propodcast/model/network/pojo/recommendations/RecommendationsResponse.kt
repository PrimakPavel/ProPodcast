package com.pavelprymak.propodcast.model.network.pojo.recommendations

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.squareup.moshi.Json


data class RecommendationsResponse(
    @Json(name = "recommendations")
    var recommendations: List<PodcastItem>? = null
)