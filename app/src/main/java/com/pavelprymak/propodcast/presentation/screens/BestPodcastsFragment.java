package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentBestPodcastsBinding;


public class BestPodcastsFragment extends Fragment {
    private FragmentBestPodcastsBinding mBinding;


    public BestPodcastsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_best_podcasts, container, false);
        return mBinding.getRoot();
    }

}
