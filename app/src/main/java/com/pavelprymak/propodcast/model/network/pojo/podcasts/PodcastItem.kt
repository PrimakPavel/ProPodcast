package com.pavelprymak.propodcast.model.network.pojo.podcasts

import com.squareup.moshi.Json


data class PodcastItem(

    @Json(name = "image")
    var image: String? = null,

    @Json(name = "country")
    var country: String? = null,

    @Json(name = "thumbnail")
    var thumbnail: String? = null,

    @Json(name = "website")
    var website: String? = null,

    @Json(name = "explicit_content")
    var isExplicitContent: Boolean = false,

    @Json(name = "itunes_id")
    var itunesId: Int = 0,

    @Json(name = "description")
    var description: String? = null,

    @Json(name = "earliest_pub_date_ms")
    var earliestPubDateMs: Long = 0,

    @Json(name = "language")
    var language: String? = null,

    @Json(name = "title")
    var title: String? = null,

    @Json(name = "genre_ids")
    var genreIds: List<Int>? = null,

    @Json(name = "listennotes_url")
    var listennotesUrl: String? = null,

    @Json(name = "total_episodes")
    var totalEpisodes: Int = 0,

    @Json(name = "is_claimed")
    var isIsClaimed: Boolean = false,

    @Json(name = "rss")
    var rss: String? = null,

    @Json(name = "looking_for")
    var lookingFor: LookingFor? = null,

    @Json(name = "extra")
    var extra: Extra? = null,

    @Json(name = "publisher")
    var publisher: String? = null,

    @Json(name = "latest_pub_date_ms")
    var latestPubDateMs: Long = 0,

    @Json(name = "id")
    var id: String = "",

    @Json(name = "email")
    var email: String? = null

)