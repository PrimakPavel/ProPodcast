package com.pavelprymak.propodcast.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_podcasts")
data class FavoritePodcastEntity(

    @PrimaryKey
    var id: String = "",

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "country")
    var country: String? = null,

    @ColumnInfo(name = "thumbnail")
    var thumbnail: String? = null,

    @ColumnInfo(name = "website")
    var website: String? = null,

    @ColumnInfo(name = "itunes_id")
    var itunesId: Int = 0,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "earliest_pub_date_ms")
    var earliestPubDateMs: Long = 0,

    @ColumnInfo(name = "language")
    var language: String? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "genre_ids")
    var genreIds: List<Int>? = null,

    @ColumnInfo(name = "listennotes_url")
    var listennotesUrl: String? = null,

    @ColumnInfo(name = "total_episodes")
    var totalEpisodes: Int = 0,

    @ColumnInfo(name = "is_claimed")
    var isClaimed: Boolean = false,

    @ColumnInfo(name = "rss")
    var rss: String? = null,

    @ColumnInfo(name = "publisher")
    var publisher: String? = null,

    @ColumnInfo(name = "latest_pub_date_ms")
    var latestPubDateMs: Long = 0,

    @ColumnInfo(name = "email")
    var email: String? = null
)
