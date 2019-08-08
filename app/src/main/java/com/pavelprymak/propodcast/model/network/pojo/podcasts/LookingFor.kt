package com.pavelprymak.propodcast.model.network.pojo.podcasts

import com.squareup.moshi.Json


data class LookingFor(

    @Json(name = "cross_promotion")
    var isCrossPromotion: Boolean = false,

    @Json(name = "sponsors")
    var isSponsors: Boolean = false,

    @Json(name = "guests")
    var isGuests: Boolean = false,

    @Json(name = "cohosts")
    var isCohosts: Boolean = false

)