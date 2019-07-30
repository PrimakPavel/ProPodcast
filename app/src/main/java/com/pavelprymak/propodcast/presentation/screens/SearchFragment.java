package com.pavelprymak.propodcast.presentation.screens;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentSearchBinding;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.presentation.adapters.SearchPodcastAdapter;
import com.pavelprymak.propodcast.presentation.adapters.SearchPodcastClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel;
import com.pavelprymak.propodcast.presentation.viewModels.SearchViewModel;
import com.pavelprymak.propodcast.utils.KeyboardUtil;
import com.pavelprymak.propodcast.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import static com.pavelprymak.propodcast.presentation.screens.PodcastDetailsFragment.ARG_PODCAST_ID;
import static com.pavelprymak.propodcast.utils.KeyboardUtil.showInputMethod;
import static com.pavelprymak.propodcast.utils.PodcastItemToFavoritePodcastConverter.createFavorite;

public class SearchFragment extends Fragment implements SearchPodcastClickListener {
    private FragmentSearchBinding mBinding;
    private SearchViewModel mSearchViewModel;
    private SearchPodcastAdapter mAdapter;
    private FavoritePodcastsViewModel mFavoritePodcastsViewModel;
    private NavController mNavController;
    private List<FavoritePodcastEntity> mFavorites = new ArrayList<>();
    private Handler mDelayHandler = new Handler();
    private static final String SEARCH_LANGUAGE = "English";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setNavViewVisibility(true);
        mFavoritePodcastsViewModel = ViewModelProviders.of(getActivity()).get(FavoritePodcastsViewModel.class);
        mSearchViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        prepareRecycler();
        mFavoritePodcastsViewModel.getFavorites().observe(this, favoritePodcastEntities -> {
            mFavorites.clear();
            if (favoritePodcastEntities != null) {
                mFavorites.addAll(favoritePodcastEntities);
            }
        });
        mSearchViewModel.getSearchResultsObserver().observe(this, resultsItems -> {
            mBinding.retryBtn.setVisibility(View.GONE);
            mAdapter = new SearchPodcastAdapter(this);
            mBinding.searchRecycler.setAdapter(mAdapter);
            mAdapter.submitList(resultsItems);
            if (resultsItems == null) {
                mBinding.tvEmptySearch.setVisibility(View.VISIBLE);
            } else {
                mBinding.tvEmptySearch.setVisibility(View.GONE);
            }
        });
        mSearchViewModel.getLoadData().observe(this, this::showProgressBar);
        mSearchViewModel.getErrorData().observe(this, throwable -> {
            if (throwable != null) {
                mBinding.retryBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.error_connection, Toast.LENGTH_LONG).show();
            }
        });

        mBinding.retryBtn.setOnClickListener(v -> {
            int size = mSearchViewModel.retryAfterErrorAndPrevLoadingListSize();
            mDelayHandler.postDelayed(() -> mBinding.searchRecycler.scrollToPosition(size - 1), 500);
        });

        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchViewModel.prepareSearchRequest(query, SEARCH_LANGUAGE);
                KeyboardUtil.hideKeyboard(getActivity());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        SearchView.SearchAutoComplete searchText = mBinding.searchView.findViewById(R.id.search_src_text);
        searchText.requestFocus();
        showInputMethod(getContext(),searchText);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFavoritePodcastsViewModel.getFavorites().removeObservers(this);
        mSearchViewModel.getErrorData().removeObservers(this);
        mSearchViewModel.getLoadData().removeObservers(this);
        mSearchViewModel.getSearchResultsObserver().removeObservers(this);

    }

    private void prepareRecycler() {
        mAdapter = new SearchPodcastAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.searchRecycler.setLayoutManager(layoutManager);
        mBinding.searchRecycler.setAdapter(mAdapter);
    }

    private void showProgressBar(Boolean isShow) {
        if (isShow != null && isShow) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onPodcastItemClick(String podcastId) {
        Bundle args = new Bundle();
        args.putString(ARG_PODCAST_ID, podcastId);
        mNavController.navigate(R.id.podcastDetailsFragment, args);
    }

    @Override
    public void onPodcastMoreOptionsClick(ResultsItem podcastItem, View v) {
        showPopupMenu(v, podcastItem);
    }

    private void showPopupMenu(View v, ResultsItem podcastItem) {
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
