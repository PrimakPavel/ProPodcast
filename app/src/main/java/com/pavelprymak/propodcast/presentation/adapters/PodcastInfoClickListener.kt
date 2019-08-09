package com.pavelprymak.propodcast.presentation.adapters

import android.view.View

import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem

interface PodcastInfoClickListener {
    fun onEpisodeItemClick(episodesItem: EpisodesItem)

    fun onMoreEpisodeClick()

    fun onEpisodeMoreOptionClick(episodesItem: EpisodesItem, view: View)

    fun onRecommendationItemClick(podcastId: String)

    fun onPodcastMoreOptionsClick(podcastItem: PodcastItem, v: View)
}
