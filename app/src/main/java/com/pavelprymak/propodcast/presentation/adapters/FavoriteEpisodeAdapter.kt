package com.pavelprymak.propodcast.presentation.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.utils.DateFormatUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_episode.*
import java.util.*

internal class FavoriteEpisodeAdapter(private val mClickListener: FavoriteEpisodeClickListener?) :
    RecyclerView.Adapter<FavoriteEpisodeAdapter.FavoriteEpisodeViewHolder>() {
    private var mFavorites: List<FavoriteEpisodeEntity?>? = null
    private var mContext: Context? = null

    fun updateList(episodes: List<FavoriteEpisodeEntity>) {
        mFavorites = episodes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEpisodeViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        return FavoriteEpisodeViewHolder(inflater.inflate(R.layout.item_episode, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteEpisodeViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mFavorites?.size ?: 0
    }

    internal inner class FavoriteEpisodeViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val empty = mContext?.getString(R.string.adapter_empty_string)

        fun bind(position: Int) {
            val episodeItem = mFavorites?.get(position)
            episodeItem?.let {
                itemContainer.setOnClickListener { mClickListener?.onEpisodeItemClick(episodeItem) }
                //Episodes Duration
                tvEpisodeDuration.text = DateFormatUtil.formatTimeHHmmss(episodeItem.audioLengthSec ?: 0)
                //Title
                tvTitle.text = episodeItem.title?.trim() ?: empty
                //Description
                tvDescription.text =
                    Html.fromHtml(episodeItem.description?.replace("\n".toRegex(), "")?.trim() ?: empty)
                //Publish date
                if (episodeItem.pubDateMs > 0L) {
                    val publishDate = Date(episodeItem.pubDateMs)
                    tvPublishedDate.setText(R.string.published_date_label)
                    tvPublishedDate.append(DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate))
                }
                ivMoreOptions.setOnClickListener { v ->
                    mClickListener?.onEpisodeMoreOptionsClick(episodeItem.id, episodeItem.listennotesUrl, v)
                }
            }
        }
    }
}
