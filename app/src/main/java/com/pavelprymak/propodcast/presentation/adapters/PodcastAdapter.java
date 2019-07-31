package com.pavelprymak.propodcast.presentation.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.ItemPodcastBinding;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.utils.DateFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {

    // Use default locale format
    private List<PodcastItem> mPodcasts;
    private final PodcastClickListener clickListener;
    private Context mContext;


    public PodcastAdapter(PodcastClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateList(List<PodcastItem> podcasts) {
        mPodcasts = podcasts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemPodcastBinding itemPodcastBinding = DataBindingUtil.inflate(inflater, R.layout.item_podcast, parent, false);
        return new PodcastViewHolder(itemPodcastBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mPodcasts == null) return 0;
        else return mPodcasts.size();
    }

    class PodcastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemPodcastBinding binding;
        private static final String EMPTY = "";

        PodcastViewHolder(@NonNull ItemPodcastBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            podcastBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            PodcastItem podcastItem = mPodcasts.get(position);
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
                        binding.tvCountryLanguage.append("(" + podcastItem.getLanguage().trim() + ")");
                    }
                } else {
                    binding.tvCountryLanguage.setText(EMPTY);
                }

                if (podcastItem.getLatestPubDateMs() > 0L) {
                    Date publishDate = new Date(podcastItem.getLatestPubDateMs());
                    binding.tvLastPublishedDate.setText(R.string.last_published_date_label);
                    binding.tvLastPublishedDate.append(DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate));
                }

                binding.ivMoreOptions.setOnClickListener(v -> clickListener.onPodcastMoreOptionsClick(podcastItem, v));
            }
        }

        @Override
        public void onClick(View v) {
            if (mPodcasts != null) {
                PodcastItem podcastItem = mPodcasts.get(getAdapterPosition());
                if (podcastItem != null) {
                    clickListener.onPodcastItemClick(podcastItem.getId());
                }
            }
        }
    }

}
