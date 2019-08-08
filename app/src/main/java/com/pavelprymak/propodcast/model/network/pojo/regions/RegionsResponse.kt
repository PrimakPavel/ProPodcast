package com.pavelprymak.propodcast.model.network.pojo.regions

import com.squareup.moshi.Json

data class RegionsResponse(
    @Json(name = "regions")
    var regions: Map<String, String>? = null
)