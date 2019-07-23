package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentBestPodcastsBinding;
import com.pavelprymak.propodcast.presentation.adapters.PodcastAdapter;
import com.pavelprymak.propodcast.presentation.adapters.PodcastsListClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.BestPodcastsViewModel;
import com.pavelprymak.propodcast.utils.ShareUtil;


public class BestPodcastsFragment extends Fragment implements PodcastsListClickListener {
    private FragmentBestPodcastsBinding mBinding;
    private BestPodcastsViewModel mViewModel;
    private PodcastAdapter mAdapter;


    public BestPodcastsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(BestPodcastsViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_best_podcasts, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareRecycler();
        mViewModel.getBestPodcasts().observe(this, podcastItems -> {
            if (podcastItems != null && podcastItems.size() > 0) {
                mAdapter.updateList(podcastItems);
            }
        });
    }

    private void prepareRecycler() {
        mAdapter = new PodcastAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerBestPodcasts.setLayoutManager(layoutManager);
        mBinding.recyclerBestPodcasts.setHasFixedSize(true);
        mBinding.recyclerBestPodcasts.setAdapter(mAdapter);
    }

    @Override
    public void onPodcastItemClick(String podcastId) {

    }

    @Override
    public void onPodcastMoreOptionsClick(String podcastId, String link, View v) {
        showPopupMenu(v, link);
    }

    private void showPopupMenu(View v, String shareLink) {
        if (getContext() == null) return;

        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.podcast_popup_menu);

        popupMenu
                .setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            Toast.makeText(getContext(),
                                    "Вы выбрали PopupMenu 1",
                                    Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.action_share:
                            ShareUtil.shareData(getActivity(), shareLink);
                            return true;
                        default:
                            return false;
                    }
                });

        popupMenu.show();
    }
}
