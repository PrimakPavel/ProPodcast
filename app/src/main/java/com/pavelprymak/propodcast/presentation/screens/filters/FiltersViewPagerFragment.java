package com.pavelprymak.propodcast.presentation.screens.filters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentFiltersViewPagerBinding;


public class FiltersViewPagerFragment extends Fragment {
    private FragmentFiltersViewPagerBinding mBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setNavViewVisibility(false);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters_view_pager, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FiltersPagerAdapter adapter = new FiltersPagerAdapter(getChildFragmentManager());
        mBinding.viewPagerFilters.setAdapter(adapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPagerFilters);
        if (mBinding.tabLayout.getTabAt(0) != null) {
            mBinding.tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_category);
        }
        if(mBinding.tabLayout.getTabAt(1)!=null){
            mBinding.tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_region);
        }
        mBinding.viewPagerFilters.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        // Back btn init
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.viewPagerFilters.clearOnPageChangeListeners();
    }

    private class FiltersPagerAdapter extends FragmentStatePagerAdapter {
        FiltersPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return GenreFilterFragment.newInstance();
            }
            return RegionFilterFragment.newInstance();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.filters_genre_title);
            }
            return getString(R.string.filters_region_title);
        }


        @Override
        public int getCount() {
            return 2;
        }
    }

}
