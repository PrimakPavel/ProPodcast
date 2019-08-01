package com.pavelprymak.propodcast.presentation.screens.filters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentRegionFilterBinding;
import com.pavelprymak.propodcast.presentation.adapters.RegionAdapter;
import com.pavelprymak.propodcast.presentation.adapters.RegionClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.RegionViewModel;

import static com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_REGIONS;


public class RegionFilterFragment extends Fragment implements RegionClickListener {
    private FragmentRegionFilterBinding mBinding;
    private RegionAdapter mAdapter;
    private RegionViewModel mRegionViewModel;

    public RegionFilterFragment() {
    }

    static RegionFilterFragment newInstance() {
        Bundle arg = new Bundle();
        RegionFilterFragment fragment = new RegionFilterFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRegionViewModel = ViewModelProviders.of(this).get(RegionViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_region_filter, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareRecycler();
        mRegionViewModel.getRegions().observe(this, regionItems -> {
            if (regionItems != null) {
                String selectedRegionShortName = App.mSettings.getFilterRegion();
                mAdapter.updateList(regionItems, selectedRegionShortName);
                if (!selectedRegionShortName.equals(ALL_REGIONS)) {
                    int selectedItemPosition = mAdapter.getPositionByRegionShortName(selectedRegionShortName);
                    mBinding.recyclerRegions.smoothScrollToPosition(selectedItemPosition);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRegionViewModel.getRegions().removeObservers(this);
    }

    private void prepareRecycler() {
        mAdapter = new RegionAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerRegions.setLayoutManager(layoutManager);
        mBinding.recyclerRegions.setHasFixedSize(true);
        mBinding.recyclerRegions.setAdapter(mAdapter);
    }

    @Override
    public void onRegionItemClick(String regionName) {
        App.mSettings.saveFilterRegion(regionName);
    }
}
