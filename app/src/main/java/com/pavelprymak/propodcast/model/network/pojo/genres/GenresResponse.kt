package com.pavelprymak.propodcast.model.network.pojo.genres

import com.squareup.moshi.Json


data class GenresResponse(
    @Json(name = "genres")
    var genres: List<GenresItem>? = null
)