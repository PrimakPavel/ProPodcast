package com.pavelprymak.propodcast.presentation.screens.favorites;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentFavoriteItemsBinding;
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.presentation.adapters.FavoriteEpisodeAdapter;
import com.pavelprymak.propodcast.presentation.adapters.FavoriteEpisodeClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.FavoriteEpisodesViewModel;
import com.pavelprymak.propodcast.utils.DateFormatUtil;
import com.pavelprymak.propodcast.utils.ShareUtil;
import com.pavelprymak.propodcast.utils.otto.player.EventStartTack;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pavelprymak.propodcast.utils.firebase.AnalyticsHelper.sentFirebaseAnalyticEpisodeData;

public class FavoriteEpisodesFragment extends Fragment implements FavoriteEpisodeClickListener {
    private FragmentFavoriteItemsBinding mBinding;
    private FavoriteEpisodesViewModel mFavoritesViewModel;
    private List<FavoriteEpisodeEntity> mFavorites = new ArrayList<>();
    private FavoriteEpisodeAdapter mAdapter;


    static FavoriteEpisodesFragment newInstance() {
        FavoriteEpisodesFragment fragment = new FavoriteEpisodesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null)
            mFavoritesViewModel = ViewModelProviders.of(getActivity()).get(FavoriteEpisodesViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_items, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareRecycler();
        mFavoritesViewModel.getFavorites().observe(this, favoriteEpisodesEntities -> {
            mFavorites.clear();
            if (favoriteEpisodesEntities != null && favoriteEpisodesEntities.size() > 0) {
                mFavorites.addAll(favoriteEpisodesEntities);
                mBinding.errorMessage.setVisibility(View.GONE);
            } else {
                mBinding.errorMessage.setText(R.string.empty_favorites_episodes_list);
                mBinding.errorMessage.setVisibility(View.VISIBLE);
            }
            mAdapter.updateList(mFavorites);
        });
    }

    @Override
    public void onEpisodeItemClick(FavoriteEpisodeEntity episodesItem) {
        sentFirebaseAnalyticEpisodeData(episodesItem);
        Date publishDate = new Date(episodesItem.getPubDateMs());
        App.eventBus.post(new EventStartTack(episodesItem.getAudio(), episodesItem.getTitle(), episodesItem.getThumbnail(), DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate)));
        App.eventBus.post(new EventUpdatePlayerVisibility(true));
    }

    @Override
    public void onEpisodeMoreOptionsClick(String episodeId, String link, View v) {
        showPopupMenu(v, link, episodeId);
    }

    private void prepareRecycler() {
        mAdapter = new FavoriteEpisodeAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.recycler_column_count), RecyclerView.VERTICAL, false);
        mBinding.recyclerFavorites.setLayoutManager(layoutManager);
        mBinding.recyclerFavorites.setHasFixedSize(true);
        mBinding.recyclerFavorites.setAdapter(mAdapter);
    }

    private void showPopupMenu(View v, String shareLink, String episodeId) {
        if (getContext() == null) return;

        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.podcast_popup_menu);
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.action_favorite);
        menuItem.setTitle(R.string.remove_from_favorite);
        popupMenu
                .setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_favorite:
                            mFavoritesViewModel.removeFromFavorite(episodeId);
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
