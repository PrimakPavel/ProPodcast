package com.pavelprymak.propodcast.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionItem
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_REGIONS
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_filter_region.*

internal class RegionAdapter(private val mClickListener: RegionClickListener?) :
    RecyclerView.Adapter<RegionAdapter.RegionViewHolder>() {
    private var mRegions: List<RegionItem?>? = null
    private var mContext: Context? = null
    private var mSelectedRegionShortName: String = ALL_REGIONS

    fun updateList(regionItems: List<RegionItem>, selectedRegionShortName: String) {
        mRegions = regionItems
        mSelectedRegionShortName = selectedRegionShortName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        return RegionViewHolder(inflater.inflate(R.layout.item_filter_region, parent, false))
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mRegions?.size ?: 0
    }

    internal inner class RegionViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val empty = mContext?.getString(R.string.adapter_empty_string)

        fun bind(position: Int) {
            mRegions?.get(position)?.let { region ->
                itemContainer.setOnClickListener { onClick(region) }
                //Title
                tvValue.text = region.regionName ?: empty

                if (region.regionShortName == mSelectedRegionShortName) {
                    ivIsSelected.visibility = View.VISIBLE
                } else {
                    ivIsSelected.visibility = View.GONE
                }
            }
        }

        private fun onClick(regionItem: RegionItem) {
            regionItem.regionShortName?.let { regionShortName ->
                if (mSelectedRegionShortName != regionShortName) {
                    val previousPosition = getPositionByRegionShortName(mSelectedRegionShortName)
                    mSelectedRegionShortName = regionShortName
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(adapterPosition)
                    mClickListener?.onRegionItemClick(regionShortName)
                }
            }
        }
    }

    fun getPositionByRegionShortName(regShortName: String): Int {
        return mRegions?.indexOfFirst { it?.regionShortName == regShortName } ?: 0
    }
}

