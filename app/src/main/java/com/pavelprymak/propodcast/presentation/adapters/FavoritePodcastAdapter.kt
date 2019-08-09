package com.pavelprymak.propodcast.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_podcast.*

internal class FavoritePodcastAdapter(private val mClickListener: FavoritePodcastClickListener?) :
    RecyclerView.Adapter<FavoritePodcastAdapter.FavoritePodcastViewHolder>() {

    private var mFavorites: List<FavoritePodcastEntity?>? = null
    private var mContext: Context? = null

    fun updateList(podcasts: List<FavoritePodcastEntity>) {
        mFavorites = podcasts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePodcastViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        return FavoritePodcastViewHolder(inflater.inflate(R.layout.item_podcast, parent, false))
    }

    override fun onBindViewHolder(holder: FavoritePodcastViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mFavorites?.size ?: 0
    }

    internal inner class FavoritePodcastViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val empty = mContext?.getString(R.string.adapter_empty_string)

        fun bind(position: Int) {
            mFavorites?.get(position)?.let { podcastItem ->
                mContext?.let { context ->
                    itemContainer.setOnClickListener {
                        mClickListener?.onPodcastItemClick(podcastItem.id)
                    }
                    //LOGO
                    if (podcastItem.thumbnail?.isNotEmpty() == true) {
                        Picasso.get()
                            .load(podcastItem.thumbnail)
                            .placeholder(ContextCompat.getDrawable(context, R.drawable.image_placeholder)!!)
                            .into(ivPodcastLogo)
                    } else {
                        ivPodcastLogo.setImageDrawable(null)
                    }
                    //Title
                    tvTitle.text = podcastItem.title ?: empty
                    //Publisher
                    tvPublisher.text = podcastItem.publisher
                    tvEpisodesCount.visibility = View.GONE
                    //Country(Language)
                    if (podcastItem.country?.isNotEmpty() == true) {
                        tvCountryLanguage.text = podcastItem.country
                        if (podcastItem.language?.isNotEmpty() == true) {
                            tvCountryLanguage.append("(" + podcastItem.language + ")")
                        }
                    } else {
                        tvCountryLanguage.text = empty
                    }
                    ivMoreOptions.setOnClickListener { v ->
                        mClickListener?.onPodcastMoreOptionsClick(podcastItem.id, podcastItem.listennotesUrl, v)
                    }
                }
            }
        }
    }

}

