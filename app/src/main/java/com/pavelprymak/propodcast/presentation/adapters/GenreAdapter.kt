package com.pavelprymak.propodcast.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_filter_genre.*

internal class GenreAdapter(private val clickListener: GenreClickListener?) :
    RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    private var mGenres: List<GenresItem?>? = null
    private var mContext: Context? = null
    private var mSelectedGenreId: Int = 0

    fun updateList(genresItems: List<GenresItem>, selectedGenreId: Int) {
        mGenres = genresItems
        mSelectedGenreId = selectedGenreId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        return GenreViewHolder(inflater.inflate(R.layout.item_filter_genre, parent, false))
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mGenres?.size ?: 0
    }

    internal inner class GenreViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val empty = mContext?.getString(R.string.adapter_empty_string)

        fun bind(position: Int) {
            mGenres?.get(position)?.let { genresItem ->
                itemContainer.setOnClickListener {
                    if (mSelectedGenreId != genresItem.id) {
                        val previousPosition = getPositionByGenreId(mSelectedGenreId)
                        mSelectedGenreId = genresItem.id
                        notifyItemChanged(previousPosition)
                        notifyItemChanged(adapterPosition)
                        clickListener?.onGenreClick(genresItem.id)
                    }
                }
                //Title
                tvValue.text = genresItem.name ?: empty
                //isSelected view
                if (genresItem.id == mSelectedGenreId) {
                    ivIsSelected.visibility = View.VISIBLE
                } else {
                    ivIsSelected.visibility = View.GONE
                }
            }
        }
    }

    fun getPositionByGenreId(genderId: Int): Int {
        return mGenres?.indexOfFirst { it?.id == genderId } ?: 0
    }
}
