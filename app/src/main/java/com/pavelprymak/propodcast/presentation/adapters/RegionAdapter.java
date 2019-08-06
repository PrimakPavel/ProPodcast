package com.pavelprymak.propodcast.presentation.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.ItemFilterRegionBinding;
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionItem;

import java.util.List;

import static com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_REGIONS;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {
    // Use default locale format
    private List<RegionItem> mRegions;
    private final RegionClickListener mClickListener;
    private Context mContext;
    private String mSelectedRegionShortName = ALL_REGIONS;


    public RegionAdapter(RegionClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void updateList(List<RegionItem> regionItems, String selectedRegionShortName) {
        mRegions = regionItems;
        mSelectedRegionShortName = selectedRegionShortName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemFilterRegionBinding itemFilterGenreBinding = DataBindingUtil.inflate(inflater, R.layout.item_filter_region, parent, false);
        return new RegionViewHolder(itemFilterGenreBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mRegions == null) return 0;
        else return mRegions.size();
    }

    class RegionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemFilterRegionBinding binding;
        private final String EMPTY = mContext.getString(R.string.adapter_empty_string);

        RegionViewHolder(@NonNull ItemFilterRegionBinding regionBinding) {
            super(regionBinding.getRoot());
            this.binding = regionBinding;
            regionBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            RegionItem region = mRegions.get(position);
            if (region != null && mContext != null) {

                //Title
                if (!TextUtils.isEmpty(region.getRegionName())) {
                    binding.tvValue.setText(region.getRegionName());
                } else {
                    binding.tvValue.setText(EMPTY);
                }
                if (region.getRegionShortName().equals(mSelectedRegionShortName)) {
                    binding.ivIsSelected.setVisibility(View.VISIBLE);
                } else {
                    binding.ivIsSelected.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mRegions != null) {
                RegionItem regionItem = mRegions.get(getAdapterPosition());
                if (regionItem != null && !mSelectedRegionShortName.equals(regionItem.getRegionShortName())) {
                    int previousPosition = getPositionByRegionShortName(mSelectedRegionShortName);
                    mSelectedRegionShortName = regionItem.getRegionShortName();
                    notifyItemChanged(previousPosition);
                    notifyItemChanged(getAdapterPosition());
                    if (mClickListener != null)
                        mClickListener.onRegionItemClick(regionItem.getRegionShortName());
                }
            }
        }
    }

    public int getPositionByRegionShortName(String regShortName) {
        if (mRegions != null && !mSelectedRegionShortName.equals(ALL_REGIONS)) {
            for (int i = 0; i < mRegions.size(); i++) {
                RegionItem region = mRegions.get(i);
                if (region.getRegionShortName().equals(regShortName)) {
                    return i;
                }
            }
        }
        return 0;
    }

}

