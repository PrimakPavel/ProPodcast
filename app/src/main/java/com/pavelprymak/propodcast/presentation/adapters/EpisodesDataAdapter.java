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
import com.pavelprymak.propodcast.databinding.ItemEpisodeBinding;
import com.pavelprymak.propodcast.databinding.ItemInfoBinding;
import com.pavelprymak.propodcast.databinding.ItemPodcastBinding;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsItem;
import com.pavelprymak.propodcast.utils.DateFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class EpisodesDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EpisodesItem> mEpisodes;
    private List<RecommendationsItem> mRecommendPodcasts;
    private EpisodesDataClickListener mClickListener;
    private Context mContext;
    private static final int EPISODES_VH_ID = 1;
    private static final int INFO_VH_ID = 2;
    private static final int RECOMMENDATION_VH_ID = 3;

    public EpisodesDataAdapter(EpisodesDataClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void updateLists(List<EpisodesItem> episodesItems, List<RecommendationsItem> recommendationsItems) {
        mEpisodes = episodesItems;
        mRecommendPodcasts = recommendationsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case EPISODES_VH_ID:
                ItemEpisodeBinding itemEpisodesBinding = DataBindingUtil.inflate(inflater, R.layout.item_episode, parent, false);
                return new EpisodeViewHolder(itemEpisodesBinding);
            case RECOMMENDATION_VH_ID: {
                ItemPodcastBinding itemPodcastBinding = DataBindingUtil.inflate(inflater, R.layout.item_podcast, parent, false);
                return new RecommendationViewHolder(itemPodcastBinding);
            }
            case INFO_VH_ID: {
                ItemInfoBinding itemInfoBinding = DataBindingUtil.inflate(inflater, R.layout.item_info, parent, false);
                return new InfoViewHolder(itemInfoBinding);
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
                int shift = getShift();
                recommendViewHolder.bind(position - shift);
                break;
            case INFO_VH_ID:
                InfoViewHolder infoViewHolder = (InfoViewHolder) holder;
                infoViewHolder.bind();
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
        if (mEpisodes == null || mEpisodes.isEmpty()) {
            if (mRecommendPodcasts != null) {
                return mRecommendPodcasts.size();
            } else return 0;
        } else if (mRecommendPodcasts == null || mRecommendPodcasts.isEmpty()) {
            return mEpisodes.size();
        } else return mEpisodes.size() + mRecommendPodcasts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecommendPodcasts == null || mRecommendPodcasts.isEmpty()) return EPISODES_VH_ID;
        if (mEpisodes == null || mEpisodes.isEmpty()) return RECOMMENDATION_VH_ID;

        if (position < mEpisodes.size()) return EPISODES_VH_ID;
        if (position > mEpisodes.size()) return RECOMMENDATION_VH_ID;
        if (position == mEpisodes.size()) return INFO_VH_ID;
        else throw new IllegalArgumentException();
    }

    class RecommendationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemPodcastBinding binding;
        private static final String EMPTY = "";

        RecommendationViewHolder(@NonNull ItemPodcastBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            podcastBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            RecommendationsItem podcastItem = mRecommendPodcasts.get(position);
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
                    binding.tvTitle.setText(podcastItem.getTitle());
                } else {
                    binding.tvTitle.setText(EMPTY);
                }
                //Publisher
                if (!TextUtils.isEmpty(podcastItem.getPublisher())) {
                    binding.tvPublisher.setText(podcastItem.getPublisher());
                } else {
                    binding.tvPublisher.setText(EMPTY);
                }
                //Country(Language)
                if (!TextUtils.isEmpty(podcastItem.getCountry())) {
                    binding.tvCountryLanguage.setText(podcastItem.getCountry());
                    if (!TextUtils.isEmpty(podcastItem.getLanguage())) {
                        binding.tvCountryLanguage.append("(" + podcastItem.getLanguage() + ")");
                    }
                } else {
                    binding.tvCountryLanguage.setText(EMPTY);
                }

                if (podcastItem.getLatestPubDateMs() > 0L) {
                    Date publishDate = new Date(podcastItem.getLatestPubDateMs());
                    binding.tvLastPublishedDate.setText(R.string.last_published_date_label);
                    binding.tvLastPublishedDate.append(DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate));
                }

                binding.ivMoreOptions.setOnClickListener(v -> mClickListener.onPodcastMoreOptionsClick(podcastItem.getId(), podcastItem.getListennotesUrl(), v));
            }
        }

        @Override
        public void onClick(View v) {
            if (mRecommendPodcasts != null) {
                RecommendationsItem podcastItem = mRecommendPodcasts.get(getAdapterPosition() - getShift());
                if (podcastItem != null) {
                    mClickListener.onRecommendationItemClick(podcastItem.getId());
                }
            }
        }

    }


    class EpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemEpisodeBinding binding;
        private static final String EMPTY = "";

        EpisodeViewHolder(@NonNull ItemEpisodeBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            podcastBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            EpisodesItem episodeItem = mEpisodes.get(position);
            if (episodeItem != null && mContext != null) {

                //Episodes Duration
                if ((episodeItem.getAudioLengthSec() > 0)) {
                    binding.tvEpisodeDuration.setText(DateFormatUtil.formatTimeHHmm(episodeItem.getAudioLengthSec()));
                } else {
                    binding.tvEpisodeDuration.setText(EMPTY);
                }
                //Title
                if (!TextUtils.isEmpty(episodeItem.getTitle())) {
                    binding.tvTitle.setText(episodeItem.getTitle());
                } else {
                    binding.tvTitle.setText(EMPTY);
                }
                //Description
                if (!TextUtils.isEmpty(episodeItem.getDescription())) {
                    binding.tvDescription.setText(Html.fromHtml(episodeItem.getDescription()));
                } else {
                    binding.tvDescription.setText(EMPTY);
                }

                //Publish date
                if (episodeItem.getPubDateMs() > 0L) {
                    Date publishDate = new Date(episodeItem.getPubDateMs());
                    binding.tvPublishedDate.setText(R.string.published_date_label);
                    binding.tvPublishedDate.append(DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate));
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mEpisodes != null) {
                EpisodesItem episodeItem = mEpisodes.get(getAdapterPosition());
                if (episodeItem != null) {
                    mClickListener.onEpisodeItemClick(episodeItem.getId(), episodeItem.getAudio());
                }
            }
        }
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        private final ItemInfoBinding binding;

        public InfoViewHolder(@NonNull ItemInfoBinding infoBinding) {
            super(infoBinding.getRoot());
            this.binding = infoBinding;
        }

        void bind() {
            binding.tvMore.setOnClickListener(v -> mClickListener.onMoreEpisodeClick());
        }
    }

}
