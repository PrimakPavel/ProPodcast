package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentBestPodcastsBinding;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.presentation.adapters.PodcastAdapter;
import com.pavelprymak.propodcast.presentation.adapters.PodcastClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.BestPodcastsViewModel;
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel;
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager;
import com.pavelprymak.propodcast.utils.ShareUtil;
import com.pavelprymak.propodcast.utils.otto.filters.EventUpdateGenreFilter;
import com.pavelprymak.propodcast.utils.otto.filters.EventUpdateRegionFilter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.pavelprymak.propodcast.presentation.screens.PodcastDetailsFragment.ARG_PODCAST_ID;
import static com.pavelprymak.propodcast.utils.PodcastItemToFavoriteConverter.createFavorite;

public class BestPodcastsFragment extends Fragment implements PodcastClickListener {
    private FragmentBestPodcastsBinding mBinding;
    private BestPodcastsViewModel mBestPodcastsViewModel;
    private FavoritePodcastsViewModel mFavoritePodcastsViewModel;
    private final List<FavoritePodcastEntity> mFavorites = new ArrayList<>();
    private PodcastAdapter mAdapter;
    private NavController mNavController;
    private final Handler mDelayHandler = new Handler();
    private final SettingsPreferenceManager mSettings = App.mSettings;
    private static final long SCROLL_DELAY = 300L;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setNavViewVisibility(true);
        }
        if (getActivity() != null) {
            mFavoritePodcastsViewModel = ViewModelProviders.of(getActivity()).get(FavoritePodcastsViewModel.class);
            mBestPodcastsViewModel = ViewModelProviders.of(getActivity()).get(BestPodcastsViewModel.class);
        }

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_best_podcasts, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        prepareRecycler();
        mBestPodcastsViewModel.prepareBestPodcasts(mSettings.getFilterGenre(), mSettings.getFilterRegion());
        mBestPodcastsViewModel.getBestPodcastsObserver().observe(this, podcastItems -> {
            mBinding.retryBtn.setVisibility(View.GONE);
            mAdapter = new PodcastAdapter(this);
            mBinding.recyclerBestPodcasts.setAdapter(mAdapter);
            mAdapter.submitList(podcastItems);
        });
        mBestPodcastsViewModel.getLoadData().observe(this, this::showProgressBar);
        mBestPodcastsViewModel.getErrorData().observe(this, throwable -> {
            if (throwable != null) {
                mBinding.retryBtn.setVisibility(View.VISIBLE);
            }
        });
        mBestPodcastsViewModel.getIsEmptyListData().observe(this, isEmptyList -> {
            if (isEmptyList) {
                mBinding.errorMessage.setVisibility(View.VISIBLE);
            } else {
                mBinding.errorMessage.setVisibility(View.GONE);
            }
        });
        mBinding.retryBtn.setOnClickListener(v -> {
            int size = mBestPodcastsViewModel.retryAfterErrorAndPrevLoadingListSize();
            mDelayHandler.postDelayed(() -> mBinding.recyclerBestPodcasts.scrollToPosition(size - 1), SCROLL_DELAY);
        });
        mFavoritePodcastsViewModel.getFavorites().observe(this, favoritePodcastEntities -> {
            mFavorites.clear();
            if (favoritePodcastEntities != null) {
                mFavorites.addAll(favoritePodcastEntities);
            }
        });
        if (!getResources().getBoolean(R.bool.isTablet)) {
            mBinding.toolbar.inflateMenu(R.menu.filters_menu);
            mBinding.toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_filters) {
                    mNavController.navigate(R.id.actionFromBestToFilters);
                    return true;
                }
                return false;
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBestPodcastsViewModel.removeObservers(this);
        mFavoritePodcastsViewModel.getFavorites().removeObservers(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.eventBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.eventBus.register(this);
    }


    @Subscribe
    public void onGenreFilterUpdate(EventUpdateGenreFilter eventUpdateGenreFilter) {
        mBestPodcastsViewModel.prepareBestPodcasts(mSettings.getFilterGenre(), mSettings.getFilterRegion());
    }

    @Subscribe
    public void onRegionFilterUpdate(EventUpdateRegionFilter eventUpdateRegionFilter) {
        mBestPodcastsViewModel.prepareBestPodcasts(mSettings.getFilterGenre(), mSettings.getFilterRegion());
    }

    private void showProgressBar(Boolean isShow) {
        if (isShow != null && isShow) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }


    private void prepareRecycler() {
        mAdapter = new PodcastAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerBestPodcasts.setLayoutManager(layoutManager);
        mBinding.recyclerBestPodcasts.setAdapter(mAdapter);
    }

    @Override
    public void onPodcastItemClick(String podcastId) {
        Bundle args = new Bundle();
        args.putString(ARG_PODCAST_ID, podcastId);
        mNavController.navigate(R.id.actionFromBestToDetails, args);
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
