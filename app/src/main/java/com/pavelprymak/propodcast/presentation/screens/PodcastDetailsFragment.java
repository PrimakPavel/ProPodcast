package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentPodcastDetailsBinding;
import com.pavelprymak.propodcast.presentation.adapters.EpisodesDataAdapter;
import com.pavelprymak.propodcast.presentation.adapters.EpisodesDataClickListener;


public class PodcastDetailsFragment extends Fragment implements EpisodesDataClickListener {

    private static final String ARG_PODCAST_ID = "argPodcastId";
    private String mPodcastId;
    private FragmentPodcastDetailsBinding mBinding;
    private EpisodesDataAdapter mAdapter;


    public static PodcastDetailsFragment newInstance(String podcastId) {
        PodcastDetailsFragment fragment = new PodcastDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PODCAST_ID, podcastId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPodcastId = getArguments().getString(ARG_PODCAST_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_podcast_details, container, false);
        return mBinding.getRoot();
    }

    private void prepareRecycler() {
        mAdapter = new EpisodesDataAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.podcastDataRecycler.setLayoutManager(layoutManager);
        mBinding.podcastDataRecycler.setHasFixedSize(true);
        mBinding.podcastDataRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onEpisodeItemClick(String episodeId, String mediaUrl) {

    }

    @Override
    public void onMoreEpisodeClick() {

    }

    @Override
    public void onRecommendationItemClick(String podcastId) {

    }

    @Override
    public void onPodcastMoreOptionsClick(String podcastId, String link, View v) {

    }
}
