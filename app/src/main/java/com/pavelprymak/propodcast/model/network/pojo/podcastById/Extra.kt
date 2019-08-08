package com.pavelprymak.propodcast.model.network.pojo.podcastById

import com.squareup.moshi.Json


data class Extra(
    @Json(name = "twitter_handle")
    var twitterHandle: String? = null,

    @Json(name = "instagram_handle")
    var instagramHandle: String? = null,

    @Json(name = "url3")
    var url3: String? = null,

    @Json(name = "url1")
    var url1: String? = null,

    @Json(name = "url2")
    var url2: String? = null,

    @Json(name = "facebook_handle")
    var facebookHandle: String? = null,

    @Json(name = "linkedin_url")
    var linkedinUrl: String? = null,

    @Json(name = "youtube_url")
    var youtubeUrl: String? = null,

    @Json(name = "google_url")
    var googleUrl: String? = null,

    @Json(name = "spotify_url")
    var spotifyUrl: String? = null,

    @Json(name = "wechat_handle")
    var wechatHandle: String? = null,

    @Json(name = "patreon_handle")
    var patreonHandle: String? = null


)