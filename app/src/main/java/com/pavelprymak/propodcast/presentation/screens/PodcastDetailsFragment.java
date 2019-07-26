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

import com.pavelprymak.propodcast.MainActivity;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentPodcastDetailsBinding;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.presentation.adapters.PodcastInfoAdapter;
import com.pavelprymak.propodcast.presentation.adapters.PodcastInfoClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel;
import com.pavelprymak.propodcast.presentation.viewModels.PodcastInfoViewModel;
import com.pavelprymak.propodcast.utils.ShareUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.pavelprymak.propodcast.utils.PodcastItemToFavoritePodcastConverter.createFavorite;


public class PodcastDetailsFragment extends Fragment implements PodcastInfoClickListener {

    static final String ARG_PODCAST_ID = "argPodcastId";
    private String mPodcastId;
    private FragmentPodcastDetailsBinding mBinding;
    private NavController mNavController;
    private PodcastInfoAdapter mAdapter;
    private PodcastInfoViewModel mPodcastDataViewModel;
    private FavoritePodcastsViewModel mFavoritePodcastsViewModel;
    private List<FavoritePodcastEntity> mFavorites = new ArrayList<>();
    private List<EpisodesItem> mEpisodes = new ArrayList<>();
    private List<PodcastItem> mRecommendations = new ArrayList<>();


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
        mFavoritePodcastsViewModel = ViewModelProviders.of(getActivity()).get(FavoritePodcastsViewModel.class);
        mPodcastDataViewModel = ViewModelProviders.of(this).get(PodcastInfoViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_podcast_details, container, false);
        ((MainActivity) getActivity()).setNavViewVisibility(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        prepareRecycler();
        mPodcastDataViewModel.getPodcastData().observe(this, podcastResponse -> {
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
                            mFavoritePodcastsViewModel.removeFromFavorite(podcastEntity.getPodcastId());
                        });
                    }
                });
            }
        });
        mPodcastDataViewModel.getRecommendData().observe(this, recommendationsItems -> {
            if (recommendationsItems != null) {
                mRecommendations.clear();
                mRecommendations.addAll(recommendationsItems);
                mAdapter.updateLists(mEpisodes, mRecommendations);
            }
        });
        mBinding.appBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });

        mFavoritePodcastsViewModel.getFavorites().observe(this, favoritePodcastEntities -> {
            mFavorites.clear();
            if (favoritePodcastEntities != null) {
                mFavorites.addAll(favoritePodcastEntities);
            }
        });
    }

    private void prepareRecycler() {
        mAdapter = new PodcastInfoAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.podcastDataRecycler.setLayoutManager(layoutManager);
        mBinding.podcastDataRecycler.setHasFixedSize(true);
        mBinding.podcastDataRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onEpisodeItemClick(String episodeId, String mediaUrl) {
        mNavController.navigate(R.id.playerFragment);
    }

    @Override
    public void onMoreEpisodeClick() {

    }

    @Override
    public void onRecommendationItemClick(String podcastId) {
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
