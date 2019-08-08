package com.pavelprymak.propodcast.model.network.pojo.languages

import com.squareup.moshi.Json


data class LanguagesResponse(
    @Json(name = "languages")
    var languages: List<String>? = null
)