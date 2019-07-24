package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
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

import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentFavoritesPodcastsBinding;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.presentation.adapters.FavoritePodcastAdapter;
import com.pavelprymak.propodcast.presentation.adapters.FavoritePodcastClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel;
import com.pavelprymak.propodcast.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import static com.pavelprymak.propodcast.presentation.screens.PodcastDetailsFragment.ARG_PODCAST_ID;


public class FavoritesPodcastsFragment extends Fragment implements FavoritePodcastClickListener {
    private FragmentFavoritesPodcastsBinding mBinding;
    private FavoritePodcastAdapter mAdapter;
    private FavoritePodcastsViewModel mFavoritesViewModel;
    private List<FavoritePodcastEntity> mFavorites = new ArrayList<>();
    private NavController mNavController;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setNavViewVisibility(true);
        mFavoritesViewModel = ViewModelProviders.of(getActivity()).get(FavoritePodcastsViewModel.class);
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites_podcasts, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        prepareRecycler();
        mFavoritesViewModel.getFavorites().observe(this, favoritePodcastEntities -> {
            mFavorites.clear();
            if (favoritePodcastEntities != null && favoritePodcastEntities.size() > 0) {
                mFavorites.addAll(favoritePodcastEntities);
                mBinding.errorMessage.setVisibility(View.GONE);
            } else {
                mBinding.errorMessage.setText(R.string.empty_favorites_list);
                mBinding.errorMessage.setVisibility(View.VISIBLE);
            }
            mAdapter.updateList(mFavorites);
        });
    }

    private void prepareRecycler() {
        mAdapter = new FavoritePodcastAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerFavorites.setLayoutManager(layoutManager);
        mBinding.recyclerFavorites.setHasFixedSize(true);
        mBinding.recyclerFavorites.setAdapter(mAdapter);
    }

    @Override
    public void onPodcastItemClick(String podcastId) {
        Bundle args = new Bundle();
        args.putString(ARG_PODCAST_ID, podcastId);
        mNavController.navigate(R.id.podcastDetailsFragment, args);
    }

    @Override
    public void onPodcastMoreOptionsClick(String podcastId, String link, View v) {
        showPopupMenu(v, link, podcastId);
    }

    private void showPopupMenu(View v, String shareLink, String podcastId) {
        if (getContext() == null) return;

        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.podcast_popup_menu);
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.action_favorite);
        menuItem.setTitle(R.string.remove_from_favorite);
        popupMenu
                .setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            mFavoritesViewModel.removeFromFavorite(podcastId);
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
