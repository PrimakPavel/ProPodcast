package com.pavelprymak.propodcast.presentation.adapters;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.ItemEpisodeMoreBinding;
import com.pavelprymak.propodcast.databinding.ItemRecommendationBinding;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.utils.DateFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class PodcastInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EpisodesItem> mEpisodes;
    private int mMaxEpisodesCount;
    private List<PodcastItem> mRecommendPodcasts;
    private final PodcastInfoClickListener mClickListener;
    private Context mContext;
    private static final int EPISODES_VH_ID = 1;
    private static final int RECOMMENDATION_VH_ID = 2;

    public PodcastInfoAdapter(PodcastInfoClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void updateLists(List<EpisodesItem> episodesItems, List<PodcastItem> recommendationsItems) {
        mEpisodes = episodesItems;
        mRecommendPodcasts = recommendationsItems;
        notifyDataSetChanged();
    }

    public void setMaxEpisodesCount(int maxEpisodesCount) {
        mMaxEpisodesCount = maxEpisodesCount;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case EPISODES_VH_ID:
                ItemEpisodeMoreBinding itemEpisodesMoreBinding = DataBindingUtil.inflate(inflater, R.layout.item_episode_more, parent, false);
                return new EpisodeViewHolder(itemEpisodesMoreBinding);
            case RECOMMENDATION_VH_ID: {
                ItemRecommendationBinding itemRecommendationBinding = DataBindingUtil.inflate(inflater, R.layout.item_recommendation, parent, false);
                return new RecommendationViewHolder(itemRecommendationBinding);
            }
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case EPISODES_VH_ID:
                EpisodeViewHolder episodesViewHolder = (EpisodeViewHolder) holder;
                episodesViewHolder.bind(position);
                break;
            case RECOMMENDATION_VH_ID:
                RecommendationViewHolder recommendViewHolder = (RecommendationViewHolder) holder;
                recommendViewHolder.bind(position - getShift());
                break;
        }
    }

    private int getShift() {
        int shift = 0;
        if (mEpisodes != null && !mEpisodes.isEmpty()) {
            shift = mEpisodes.size();
        }
        return shift;
    }

    @Override
    public int getItemCount() {
        int episodesCount = 0;
        int recommendedCount = 0;
        if (mEpisodes != null) {
            episodesCount = mEpisodes.size();
        }
        if (mRecommendPodcasts != null) {
            recommendedCount = mRecommendPodcasts.size();
        }
        return episodesCount + recommendedCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mEpisodes != null && position < mEpisodes.size()) return EPISODES_VH_ID;
        else return RECOMMENDATION_VH_ID;
    }

    class RecommendationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemRecommendationBinding binding;
        private static final String EMPTY = "";

        RecommendationViewHolder(@NonNull ItemRecommendationBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            //show first element header
            if (position == 0) {
                binding.recommendationLabel.setVisibility(View.VISIBLE);
            } else {
                binding.recommendationLabel.setVisibility(View.GONE);
            }

            PodcastItem podcastItem = mRecommendPodcasts.get(position);
            if (podcastItem != null && mContext != null) {
                //LOGO
                if (!TextUtils.isEmpty(podcastItem.getThumbnail())) {
                    Picasso.get()
                            .load(podcastItem.getThumbnail())
                            .placeholder(mContext.getResources().getDrawable(R.drawable.image_placeholder))
                            .into(binding.ivPodcastLogo);
                } else {
                    binding.ivPodcastLogo.setImageDrawable(null);
                }

                //Episodes Count
                if ((podcastItem.getTotalEpisodes() > 0)) {
                    binding.tvEpisodesCount.setText(String.valueOf(podcastItem.getTotalEpisodes()));
                    binding.tvEpisodesCount.append(" " + mContext.getString(R.string.episodes_label));

                } else {
                    binding.tvEpisodesCount.setText(EMPTY);
                }
                //Title
                if (!TextUtils.isEmpty(podcastItem.getTitle())) {
                    binding.tvTitle.setText(podcastItem.getTitle().trim());
                } else {
                    binding.tvTitle.setText(EMPTY);
                }
                //Publisher
                if (!TextUtils.isEmpty(podcastItem.getPublisher())) {
                    binding.tvPublisher.setText(podcastItem.getPublisher().trim());
                } else {
                    binding.tvPublisher.setText(EMPTY);
                }
                //Country(Language)
                if (!TextUtils.isEmpty(podcastItem.getCountry())) {
                    binding.tvCountryLanguage.setText(podcastItem.getCountry().trim());
                    if (!TextUtils.isEmpty(podcastItem.getLanguage())) {
                        binding.tvCountryLanguage.append("(" + podcastItem.getLanguage() + ")");
                    }
                } else {
                    binding.tvCountryLanguage.setText(EMPTY);
                }

                if (podcastItem.getLatestPubDateMs() > 0L) {
                    Date publishDate = new Date(podcastItem.getLatestPubDateMs());
                    binding.tvLastPublishedDate.setText(R.string.last_published_date_label);
                    binding.tvLastPublishedDate.append(DateFormatUtil.INSTANCE.getPUBLISH_DATE_FORMAT().format(publishDate));
                }

                binding.ivMoreOptions.setOnClickListener(v -> {
                    if (mClickListener != null)
                        mClickListener.onPodcastMoreOptionsClick(podcastItem, v);
                });
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition() - getShift();
            if (mRecommendPodcasts != null && position < mRecommendPodcasts.size()) {
                PodcastItem podcastItem = mRecommendPodcasts.get(position);
                if (podcastItem != null) {
                    if (mClickListener != null)
                        mClickListener.onRecommendationItemClick(podcastItem.getId());
                }
            }
        }
    }


    class EpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemEpisodeMoreBinding binding;
        private final String EMPTY = mContext.getString(R.string.adapter_empty_string);

        EpisodeViewHolder(@NonNull ItemEpisodeMoreBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            podcastBinding.container.itemContainer.setOnClickListener(this);
        }

        void bind(int position) {
            //Show last element footer
            if (mMaxEpisodesCount > mEpisodes.size()) {
                if (position == mEpisodes.size() - 1) {
                    binding.moreContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.moreContainer.setVisibility(View.GONE);
                }
                binding.tvMore.setOnClickListener(v -> {
                    if (mClickListener != null)
                        mClickListener.onMoreEpisodeClick();
                });
            } else {
                binding.moreContainer.setVisibility(View.GONE);
            }
            EpisodesItem episodeItem = mEpisodes.get(position);
            if (episodeItem != null && mContext != null) {

                //Episodes Duration
                if ((episodeItem.getAudioLengthSec()!=null && episodeItem.getAudioLengthSec() > 0)) {
                    binding.container.tvEpisodeDuration.setText(DateFormatUtil.INSTANCE.formatTimeHHmmss(episodeItem.getAudioLengthSec()));
                } else {
                    binding.container.tvEpisodeDuration.setText(EMPTY);
                }
                //Title
                if (!TextUtils.isEmpty(episodeItem.getTitle())) {
                    binding.container.tvTitle.setText(episodeItem.getTitle().trim());
                } else {
                    binding.container.tvTitle.setText(EMPTY);
                }
                //Description
                if (!TextUtils.isEmpty(episodeItem.getDescription())) {
                    binding.container.tvDescription.setText(Html.fromHtml(episodeItem.getDescription().replaceAll("\n", "").trim()));
                } else {
                    binding.container.tvDescription.setText(EMPTY);
                }

                //Publish date
                if (episodeItem.getPubDateMs() > 0L) {
                    Date publishDate = new Date(episodeItem.getPubDateMs());
                    binding.container.tvPublishedDate.setText(R.string.published_date_label);
                    binding.container.tvPublishedDate.append(DateFormatUtil.INSTANCE.getPUBLISH_DATE_FORMAT().format(publishDate));
                }
                binding.container.ivMoreOptions.setOnClickListener(v -> {
                    if (mClickListener != null)
                        mClickListener.onEpisodeMoreOptionClick(episodeItem, v);
                });
            }
        }

        @Override
        public void onClick(View v) {
            if (mEpisodes != null) {
                EpisodesItem episodeItem = mEpisodes.get(getAdapterPosition());
                if (episodeItem != null && mClickListener != null) {
                    mClickListener.onEpisodeItemClick(episodeItem);
                }
            }
        }
    }


}
