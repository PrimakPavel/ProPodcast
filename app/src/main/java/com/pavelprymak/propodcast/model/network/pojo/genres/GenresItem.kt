package com.pavelprymak.propodcast.model.network.pojo.genres


import com.squareup.moshi.Json


data class GenresItem(
    @Json(name = "parent_id")
    var parentId: Int? = null,

    @Json(name = "name")
    var name: String? = null,

    @Json(name = "id")
    var id: Int = 0
)