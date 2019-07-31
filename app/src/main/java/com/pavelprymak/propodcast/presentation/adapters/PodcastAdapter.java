package com.pavelprymak.propodcast.presentation.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.ItemPodcastBinding;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.utils.DateFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class PodcastAdapter extends PagedListAdapter<PodcastItem, PodcastAdapter.PodcastViewHolder> {
    private final PodcastClickListener mClickListener;
    private Context mContext;

    private static final DiffUtil.ItemCallback<PodcastItem> diffUtilCallback = new DiffUtil.ItemCallback<PodcastItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull PodcastItem oldItem, @NonNull PodcastItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PodcastItem oldItem, @NonNull PodcastItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };


    public PodcastAdapter(PodcastClickListener clickListener) {
        super(diffUtilCallback);
        mClickListener = clickListener;
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


    class PodcastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemPodcastBinding binding;
        private static final String EMPTY = "";

        PodcastViewHolder(@NonNull ItemPodcastBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            podcastBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            PodcastItem podcastItem = getItem(position);
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

                binding.ivMoreOptions.setOnClickListener(v -> {
                    if (mClickListener != null)
                        mClickListener.onPodcastMoreOptionsClick(podcastItem, v);
                });
            }
        }

        @Override
        public void onClick(View v) {
            PodcastItem podcastItem = getItem(getAdapterPosition());
            if (podcastItem != null && mClickListener != null) {
                mClickListener.onPodcastItemClick(podcastItem.getId());
            }

        }
    }

}
