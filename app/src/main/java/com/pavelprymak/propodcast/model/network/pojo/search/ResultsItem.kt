package com.pavelprymak.propodcast.model.network.pojo.search

import com.squareup.moshi.Json


data class ResultsItem(

    @Json(name = "image")
    var image: String? = null,

    @Json(name = "thumbnail")
    var thumbnail: String? = null,

    @Json(name = "description_original")
    var descriptionOriginal: String? = null,

    @Json(name = "itunes_id")
    var itunesId: Int = 0,

    @Json(name = "explicit_content")
    var isExplicitContent: Boolean = false,

    @Json(name = "publisher_highlighted")
    var publisherHighlighted: String? = null,

    @Json(name = "earliest_pub_date_ms")
    var earliestPubDateMs: Long = 0,

    @Json(name = "genre_ids")
    var genreIds: List<Int>? = null,

    @Json(name = "listennotes_url")
    var listennotesUrl: String? = null,

    @Json(name = "total_episodes")
    var totalEpisodes: Int = 0,

    @Json(name = "title_highlighted")
    var titleHighlighted: String? = null,

    @Json(name = "title_original")
    var titleOriginal: String? = null,

    @Json(name = "rss")
    var rss: String? = null,

    @Json(name = "description_highlighted")
    var descriptionHighlighted: String? = null,

    @Json(name = "publisher_original")
    var publisherOriginal: String? = null,

    @Json(name = "latest_pub_date_ms")
    var latestPubDateMs: Long = 0,

    @Json(name = "id")
    var id: String? = null,

    @Json(name = "email")
    var email: String? = null

)