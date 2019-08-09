package com.pavelprymak.propodcast.presentation.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.utils.DateFormatUtil
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_episode.view.*
import kotlinx.android.synthetic.main.item_episode_more.*
import kotlinx.android.synthetic.main.item_recommendation.*
import java.util.*

private const val EPISODES_VH_ID = 1
private const val RECOMMENDATION_VH_ID = 2

class PodcastInfoAdapter(private val mClickListener: PodcastInfoClickListener?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mEpisodes: List<EpisodesItem>? = null
    private var mMaxEpisodesCount: Int = 0
    private var mRecommendPodcasts: List<PodcastItem>? = null
    private var mContext: Context? = null

    fun updateLists(episodesItems: List<EpisodesItem>, recommendationsItems: List<PodcastItem>) {
        mEpisodes = episodesItems
        mRecommendPodcasts = recommendationsItems
        notifyDataSetChanged()
    }

    fun setMaxEpisodesCount(maxEpisodesCount: Int) {
        mMaxEpisodesCount = maxEpisodesCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        return when (viewType) {
            EPISODES_VH_ID ->
                EpisodeViewHolder(inflater.inflate(R.layout.item_episode_more, parent, false))
            RECOMMENDATION_VH_ID -> {
                RecommendationViewHolder(inflater.inflate(R.layout.item_recommendation, parent, false))
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            EPISODES_VH_ID -> {
                val episodesViewHolder = holder as EpisodeViewHolder
                episodesViewHolder.bind(position)
            }
            RECOMMENDATION_VH_ID -> {
                val recommendViewHolder = holder as RecommendationViewHolder
                recommendViewHolder.bind(position - getShift())
            }
        }
    }

    override fun getItemCount(): Int {
        return (mEpisodes?.size ?: 0) + (mRecommendPodcasts?.size ?: 0)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < mEpisodes?.size ?: 0) EPISODES_VH_ID else RECOMMENDATION_VH_ID
    }

    internal inner class RecommendationViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val empty = mContext?.getString(R.string.adapter_empty_string)
        fun bind(position: Int) {
            mContext?.let { context ->
                //show first element header
                if (position == 0) {
                    recommendationLabel.visibility = View.VISIBLE
                } else {
                    recommendationLabel.visibility = View.GONE
                }
                mRecommendPodcasts?.get(position)?.let { podcastItem ->
                    recommendContainer.setOnClickListener { mClickListener?.onRecommendationItemClick(podcastItem.id) }
                    //LOGO
                    if (podcastItem.thumbnail?.isNotEmpty() == true) {
                        Picasso.get()
                            .load(podcastItem.thumbnail)
                            .placeholder(context.resources.getDrawable(R.drawable.image_placeholder))
                            .into(ivPodcastLogo)
                    } else {
                        ivPodcastLogo.setImageDrawable(null)
                    }

                    //Episodes Count
                    if (podcastItem.totalEpisodes > 0) {
                        tvEpisodesCount.text = podcastItem.totalEpisodes.toString()
                        tvEpisodesCount.append(" " + context.getString(R.string.episodes_label))

                    } else {
                        tvEpisodesCount.text = empty
                    }
                    //Title
                    tvTitle.text = podcastItem.title?.trim() ?: empty

                    //Publisher
                    tvPublisher.text = podcastItem.publisher?.trim() ?: empty

                    //Country(Language)
                    if (podcastItem.country?.isNotEmpty() == true) {
                        tvCountryLanguage.text = podcastItem.country?.trim()
                        if (podcastItem.language?.isNotEmpty() == true) {
                            tvCountryLanguage.append("(" + podcastItem.language + ")")
                        }
                    } else {
                        tvCountryLanguage.text = empty
                    }

                    if (podcastItem.latestPubDateMs > 0L) {
                        val publishDate = Date(podcastItem.latestPubDateMs)
                        tvLastPublishedDate.setText(R.string.last_published_date_label)
                        tvLastPublishedDate.append(DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate))
                    }

                    ivMoreOptions.setOnClickListener { v -> mClickListener?.onPodcastMoreOptionsClick(podcastItem, v) }
                }
            }
        }
    }

    internal inner class EpisodeViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val empty = mContext?.getString(R.string.adapter_empty_string)

        fun bind(position: Int) {
            //Show last element footer
            moreContainer.visibility = View.GONE
            mEpisodes?.let { episodes ->
                if (mMaxEpisodesCount > episodes.size) {
                    if (position == episodes.size - 1) {
                        moreContainer.visibility = View.VISIBLE
                        tvMore.setOnClickListener { mClickListener?.onMoreEpisodeClick() }
                    } else {
                        moreContainer.visibility = View.GONE
                    }
                }
            }
            mEpisodes?.get(position)?.let { episodeItem ->
                container.setOnClickListener { mClickListener?.onEpisodeItemClick(episodeItem) }
                //Episodes Duration
                if (episodeItem.audioLengthSec > 0) {
                    container.tvEpisodeDuration.text = DateFormatUtil.formatTimeHHmmss(episodeItem.audioLengthSec)
                } else {
                    container.tvEpisodeDuration.text = empty
                }
                //Title
                container.tvTitle.text = episodeItem.title?.trim() ?: empty

                //Description
                container.tvDescription.text =
                    Html.fromHtml(episodeItem.description?.replace("\n".toRegex(), "")?.trim() ?: empty)

                //Publish date
                if (episodeItem.pubDateMs > 0L) {
                    val publishDate = Date(episodeItem.pubDateMs)
                    container.tvPublishedDate.setText(R.string.published_date_label)
                    container.tvPublishedDate.append(DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate))
                }
                container.ivMoreOptions.setOnClickListener { v ->
                    mClickListener?.onEpisodeMoreOptionClick(episodeItem, v)
                }
            }
        }
    }

    private fun getShift(): Int {
        return mEpisodes?.size ?: 0
    }
}
