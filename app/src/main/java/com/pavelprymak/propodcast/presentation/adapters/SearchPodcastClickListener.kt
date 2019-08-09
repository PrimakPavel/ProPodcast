package com.pavelprymak.propodcast.presentation.adapters

import android.view.View

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem

interface SearchPodcastClickListener {
    fun onPodcastItemClick(podcastId: String)

    fun onPodcastMoreOptionsClick(podcastItem: ResultsItem, v: View)
}
