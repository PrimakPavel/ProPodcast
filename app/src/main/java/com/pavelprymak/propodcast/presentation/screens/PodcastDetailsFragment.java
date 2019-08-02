package com.pavelprymak.propodcast.presentation.screens;


import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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

import com.google.android.material.snackbar.Snackbar;
import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentPodcastDetailsBinding;
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.presentation.adapters.PodcastInfoAdapter;
import com.pavelprymak.propodcast.presentation.adapters.PodcastInfoClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.FavoriteEpisodesViewModel;
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel;
import com.pavelprymak.propodcast.presentation.viewModels.PodcastInfoViewModel;
import com.pavelprymak.propodcast.presentation.viewModels.PodcastInfoViewModelFactory;
import com.pavelprymak.propodcast.utils.DateFormatUtil;
import com.pavelprymak.propodcast.utils.ShareUtil;
import com.pavelprymak.propodcast.utils.otto.player.EventStartTack;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pavelprymak.propodcast.utils.PodcastItemToFavoriteConverter.createFavorite;
import static com.pavelprymak.propodcast.utils.firebase.AnalyticsHelper.sentFirebaseAnalyticEpisodeData;


public class PodcastDetailsFragment extends Fragment implements PodcastInfoClickListener {

    public static final String ARG_PODCAST_ID = "argPodcastId";
    private String mPodcastId;
    private FragmentPodcastDetailsBinding mBinding;
    private NavController mNavController;
    private PodcastInfoAdapter mAdapter;
    private PodcastInfoViewModel mPodcastDataViewModel;
    private FavoritePodcastsViewModel mFavoritePodcastsViewModel;
    private FavoriteEpisodesViewModel mFavoriteEpisodesViewModel;
    private List<FavoritePodcastEntity> mFavoritePodcasts = new ArrayList<>();
    private List<FavoriteEpisodeEntity> mFavoriteEpisodes = new ArrayList<>();
    private List<EpisodesItem> mEpisodes = new ArrayList<>();
    private List<PodcastItem> mRecommendations = new ArrayList<>();


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
        if (mPodcastId != null) {
            PodcastInfoViewModelFactory factory = new PodcastInfoViewModelFactory(mPodcastId);
            mPodcastDataViewModel = ViewModelProviders.of(this, factory).get(PodcastInfoViewModel.class);
        }
        if (getActivity() != null) {
            mFavoritePodcastsViewModel = ViewModelProviders.of(getActivity()).get(FavoritePodcastsViewModel.class);
            mFavoriteEpisodesViewModel = ViewModelProviders.of(getActivity()).get(FavoriteEpisodesViewModel.class);
        }
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_podcast_details, container, false);
        ((MainActivity) getActivity()).setNavViewVisibility(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        mBinding.appBarLayout.setExpanded(false);
        prepareRecycler();
        if (mPodcastDataViewModel != null) {
            mPodcastDataViewModel.preparePodcastInfoData();
            mPodcastDataViewModel.getPodcastDataBatch().getData().observe(this, this::showData);
            mPodcastDataViewModel.getPodcastDataBatch().getLoading().observe(this, isLoading -> {
                if (isLoading != null) {
                    progressBarVisibility(isLoading);
                }
            });
            mPodcastDataViewModel.getPodcastDataBatch().getError().observe(this, throwable -> {
                if (throwable != null) {
                    Snackbar snackbar = Snackbar.make(view, R.string.error_connection, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    mBinding.appBarLayout.setExpanded(false);
                }
            });
            mPodcastDataViewModel.getRecommendData().observe(this, recommendationsItems -> {
                if (recommendationsItems != null) {
                    mRecommendations.clear();
                    mRecommendations.addAll(recommendationsItems);
                    mAdapter.updateLists(mEpisodes, mRecommendations);
                }
            });
        }
        mBinding.appBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });

        mFavoritePodcastsViewModel.getFavorites().observe(this, favoritePodcastEntities -> {
            mFavoritePodcasts.clear();
            if (favoritePodcastEntities != null) {
                mFavoritePodcasts.addAll(favoritePodcastEntities);
            }
        });

        mFavoriteEpisodesViewModel.getFavorites().observe(this, favoriteEpisodesEntities -> {
            mFavoriteEpisodes.clear();
            if (favoriteEpisodesEntities != null) {
                mFavoriteEpisodes.addAll(favoriteEpisodesEntities);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPodcastDataViewModel != null) {
            mPodcastDataViewModel.removePodcastDataBatchObservers(this);
            mPodcastDataViewModel.getRecommendData().removeObservers(this);
        }
        mFavoriteEpisodesViewModel.getFavorites().removeObservers(this);
        mFavoritePodcastsViewModel.getFavorites().removeObservers(this);
    }

    private void prepareRecycler() {
        mAdapter = new PodcastInfoAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.podcastDataRecycler.setLayoutManager(layoutManager);
        mBinding.podcastDataRecycler.setHasFixedSize(true);
        mBinding.podcastDataRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onEpisodeItemClick(EpisodesItem episodesItem) {
        Date publishDate = new Date(episodesItem.getPubDateMs());
        sentFirebaseAnalyticEpisodeData(episodesItem);
        App.eventBus.post(new EventStartTack(episodesItem.getAudio(), episodesItem.getTitle(), episodesItem.getThumbnail(), DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate)));
        App.eventBus.post(new EventUpdatePlayerVisibility(true));
    }

    @Override
    public void onMoreEpisodeClick() {
        if (mPodcastDataViewModel != null) {
            mPodcastDataViewModel.loadMoreEpisoded();
        }
    }

    @Override
    public void onEpisodeMoreOptionClick(EpisodesItem episodesItem, View view) {
        showEpisodePopupMenu(view, episodesItem);
    }

    @Override
    public void onRecommendationItemClick(String podcastId) {
        Bundle args = new Bundle();
        args.putString(ARG_PODCAST_ID, podcastId);
        mNavController.navigate(R.id.podcastDetailsFragment, args);
    }

    @Override
    public void onPodcastMoreOptionsClick(PodcastItem podcastItem, View v) {
        showPodcastPopupMenu(v, podcastItem);
    }

    private void progressBarVisibility(boolean isVisible) {
        if (isVisible) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }

    private void showData(PodcastResponse podcastResponse) {
        if (podcastResponse != null) {

            //Title
            if (!TextUtils.isEmpty(podcastResponse.getTitle()))
                mBinding.tvTitle.setText(podcastResponse.getTitle());
            //Publisher
            if (!TextUtils.isEmpty(podcastResponse.getPublisher())) {
                mBinding.tvPublisher.setText(R.string.publisher_label);
                mBinding.tvPublisher.append(podcastResponse.getPublisher());
            }
            //Country
            if (!TextUtils.isEmpty(podcastResponse.getCountry())) {
                mBinding.tvCountry.setText(R.string.country_label);
                mBinding.tvCountry.append(podcastResponse.getCountry());
            }
            //Total episodes count
            mBinding.tvTotalEpisodes.setText(R.string.episodes_count_label);
            mBinding.tvTotalEpisodes.append(String.valueOf(podcastResponse.getTotalEpisodes()));
            //Set photo
            if (!TextUtils.isEmpty(podcastResponse.getImage()))
                Picasso.get()
                        .load(podcastResponse.getImage())
                        .into(mBinding.photo);
            if (!TextUtils.isEmpty(podcastResponse.getDescription())) {
                mBinding.tvDescription.setText(Html.fromHtml(podcastResponse.getDescription()));
            }
            //Add episodes
            if (podcastResponse.getEpisodes() != null) {
                mEpisodes.clear();
                mEpisodes.addAll(podcastResponse.getEpisodes());
                mAdapter.updateLists(mEpisodes, mRecommendations);
                mAdapter.setMaxEpisodesCount(podcastResponse.getTotalEpisodes());
            }
            mFavoritePodcastsViewModel.getFavoriteById(mPodcastId).removeObservers(this);
            mFavoritePodcastsViewModel.getFavoriteById(mPodcastId).observe(this, podcastEntity -> {
                if (podcastEntity == null) {
                    mBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border);
                    mBinding.fabFavorite.setOnClickListener(v -> {
                        mFavoritePodcastsViewModel.addToFavorite(createFavorite(podcastResponse));
                    });
                } else {
                    mBinding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite);
                    mBinding.fabFavorite.setOnClickListener(v -> {
                        mFavoritePodcastsViewModel.removeFromFavorite(podcastEntity.getId());
                    });
                }
            });
        }
    }

    private void showPodcastPopupMenu(View v, PodcastItem podcastItem) {
        if (getContext() == null) return;

        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.podcast_popup_menu);
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.action_favorite);
        boolean isFavorite = mFavoritePodcastsViewModel.isFavorite(mFavoritePodcasts, podcastItem.getId());
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

    private void showEpisodePopupMenu(View v, EpisodesItem episodesItem) {
        if (getContext() == null) return;

        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.podcast_popup_menu);
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.action_favorite);
        boolean isFavorite = mFavoriteEpisodesViewModel.isFavorite(mFavoriteEpisodes, episodesItem.getId());
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
                                mFavoriteEpisodesViewModel.removeFromFavorite(episodesItem.getId());
                            } else {
                                mFavoriteEpisodesViewModel.addToFavorite(createFavorite(episodesItem));
                            }
                            return true;
                        case R.id.action_share:
                            ShareUtil.shareData(getActivity(), episodesItem.getListennotesUrl());
                            return true;
                        default:
                            return false;
                    }
                });

        popupMenu.show();
    }


}
