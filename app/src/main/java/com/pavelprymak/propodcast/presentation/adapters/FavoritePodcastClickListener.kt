package com.pavelprymak.propodcast.presentation.adapters

import android.view.View

interface FavoritePodcastClickListener {
    fun onPodcastItemClick(podcastId: String)

    fun onPodcastMoreOptionsClick(podcastId: String, link: String?, v: View)
}
