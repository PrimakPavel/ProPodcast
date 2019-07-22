package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentFavoritesPodcastsBinding;


public class FavoritesPodcastsFragment extends Fragment {
    private FragmentFavoritesPodcastsBinding mBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites_podcasts, container, false);
        return mBinding.getRoot();
    }

}
