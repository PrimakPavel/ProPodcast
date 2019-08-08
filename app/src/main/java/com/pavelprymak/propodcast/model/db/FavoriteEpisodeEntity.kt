package com.pavelprymak.propodcast.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_episodes")
data class FavoriteEpisodeEntity(
    @PrimaryKey
    var id: String = "",

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "thumbnail")
    var thumbnail: String? = null,

    @ColumnInfo(name = "audio_length_sec")
    var audioLengthSec: Int? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "audio")
    var audio: String? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "pub_date_ms")
    var pubDateMs: Long = 0,

    @ColumnInfo(name = "listennotes_url")
    var listennotesUrl: String? = null
)
