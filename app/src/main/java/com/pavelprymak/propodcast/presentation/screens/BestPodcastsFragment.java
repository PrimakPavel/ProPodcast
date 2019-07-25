package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentBestPodcastsBinding;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.presentation.adapters.PodcastAdapter;
import com.pavelprymak.propodcast.presentation.adapters.PodcastClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.BestPodcastsViewModel;
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel;
import com.pavelprymak.propodcast.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import static com.pavelprymak.propodcast.presentation.screens.PodcastDetailsFragment.ARG_PODCAST_ID;
import static com.pavelprymak.propodcast.utils.PodcastItemToFavoritePodcastConverter.createFavorite;


public class BestPodcastsFragment extends Fragment implements PodcastClickListener {
    private FragmentBestPodcastsBinding mBinding;
    private BestPodcastsViewModel mBestPodcastsViewModel;
    private FavoritePodcastsViewModel mFavoritePodcastsViewModel;
    private List<FavoritePodcastEntity> mFavorites = new ArrayList<>();
    private PodcastAdapter mAdapter;
    private NavController mNavController;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setNavViewVisibility(true);
        mFavoritePodcastsViewModel = ViewModelProviders.of(getActivity()).get(FavoritePodcastsViewModel.class);
        mBestPodcastsViewModel = ViewModelProviders.of(this).get(BestPodcastsViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_best_podcasts, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        prepareRecycler();
        mBestPodcastsViewModel.getBestPodcasts().observe(this, podcastItems -> {
            if (podcastItems != null && podcastItems.size() > 0) {
                mAdapter.updateList(podcastItems);
            }
        });
        mFavoritePodcastsViewModel.getFavorites().observe(this, favoritePodcastEntities -> {
            mFavorites.clear();
            if (favoritePodcastEntities != null) {
                mFavorites.addAll(favoritePodcastEntities);
            }
        });
        mBinding.toolbar.inflateMenu(R.menu.filters_menu);
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_filters) {
                mNavController.navigate(R.id.filtersViewPagerFragment);
                return true;
            }
            return false;
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
        Bundle args = new Bundle();
        args.putString(ARG_PODCAST_ID, podcastId);
        mNavController.navigate(R.id.podcastDetailsFragment, args);
    }

    @Override
    public void onPodcastMoreOptionsClick(PodcastItem podcastItem, View v) {
        showPopupMenu(v, podcastItem);
    }

    private void showPopupMenu(View v, PodcastItem podcastItem) {
        if (getContext() == null) return;

        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.podcast_popup_menu);
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.action_favorite);
        boolean isFavorite = mFavoritePodcastsViewModel.isFavorite(mFavorites, podcastItem.getId());
        if (isFavorite) {
            menuItem.setTitle(R.string.remove_from_favorite);
        } else {
            menuItem.setTitle(R.string.add_to_favorite);
        }
        popupMenu
                .setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            if (isFavorite) {
                                mFavoritePodcastsViewModel.removeFromFavorite(podcastItem.getId());
                            } else {
                                mFavoritePodcastsViewModel.addToFavorite(createFavorite(podcastItem));
                            }
                            return true;
                        case R.id.action_share:
                            ShareUtil.shareData(getActivity(), podcastItem.getListennotesUrl());
                            return true;
                        default:
                            return false;
                    }
                });

        popupMenu.show();
    }

}
