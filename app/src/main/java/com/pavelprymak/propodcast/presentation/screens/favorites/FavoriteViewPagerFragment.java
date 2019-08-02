package com.pavelprymak.propodcast.presentation.screens.favorites;


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
import com.pavelprymak.propodcast.databinding.FragmentFavoritePagerBinding;


public class FavoriteViewPagerFragment extends Fragment {
    private FragmentFavoritePagerBinding mBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setNavViewVisibility(true);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_pager, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FavoritesPagerAdapter adapter = new FavoritesPagerAdapter(getChildFragmentManager());
        mBinding.viewPagerFavorites.setAdapter(adapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPagerFavorites);
        mBinding.viewPagerFavorites.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
        mBinding.viewPagerFavorites.clearOnPageChangeListeners();
    }

    private class FavoritesPagerAdapter extends FragmentStatePagerAdapter {
        FavoritesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return FavoritePodcastsFragment.newInstance();
            }
            return FavoriteEpisodesFragment.newInstance();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.favorite_podcats_title);
            }
            return getString(R.string.favorite_episodes_title);
        }


        @Override
        public int getCount() {
            return 2;
        }
    }

}
