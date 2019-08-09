package com.pavelprymak.propodcast.presentation.adapters

import android.view.View

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem

interface PodcastClickListener {
    fun onPodcastItemClick(podcastId: String)

    fun onPodcastMoreOptionsClick(podcastItem: PodcastItem, v: View)

}
