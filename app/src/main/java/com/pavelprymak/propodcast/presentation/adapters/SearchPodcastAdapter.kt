package com.pavelprymak.propodcast.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem
import com.pavelprymak.propodcast.utils.DateFormatUtil
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_podcast.*
import java.util.*

internal class SearchPodcastAdapter(private val mClickListener: SearchPodcastClickListener?) :
    PagedListAdapter<ResultsItem, SearchPodcastAdapter.PodcastViewHolder>(diffUtilCallback) {
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        return PodcastViewHolder(inflater.inflate(R.layout.item_podcast, parent, false))
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        holder.bind(position)
    }


    internal inner class PodcastViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val empty = mContext?.getString(R.string.adapter_empty_string)

        fun bind(position: Int) {
            mContext?.let { context ->
                getItem(position)?.let { podcastItem ->
                    itemContainer.setOnClickListener { mClickListener?.onPodcastItemClick(podcastItem.id) }
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
                    tvTitle.text = podcastItem.titleOriginal ?: empty

                    //Publisher
                    tvPublisher.text = podcastItem.publisherOriginal ?: empty
                    tvCountryLanguage.visibility = View.GONE

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

    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

