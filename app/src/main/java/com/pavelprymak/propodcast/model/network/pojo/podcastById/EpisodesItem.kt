package com.pavelprymak.propodcast.model.network.pojo.podcastById


import com.squareup.moshi.Json


data class EpisodesItem(

    @Json(name = "image")
    var image: String? = null,

    @Json(name = "thumbnail")
    var thumbnail: String? = null,

    @Json(name = "explicit_content")
    var isExplicitContent: Boolean = false,

    @Json(name = "listennotes_edit_url")
    var listennotesEditUrl: String? = null,

    @Json(name = "audio_length_sec")
    var audioLengthSec: Int = 0,

    @Json(name = "description")
    var description: String? = null,

    @Json(name = "id")
    var id: String? = null,

    @Json(name = "audio")
    var audio: String? = null,

    @Json(name = "title")
    var title: String? = null,

    @Json(name = "pub_date_ms")
    var pubDateMs: Long = 0,

    @Json(name = "listennotes_url")
    var listennotesUrl: String? = null,

    @Json(name = "maybe_audio_invalid")
    var isMaybeAudioInvalid: Boolean = false

)