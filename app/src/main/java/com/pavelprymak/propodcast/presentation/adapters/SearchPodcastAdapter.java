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
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.utils.DateFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class SearchPodcastAdapter extends PagedListAdapter<ResultsItem, SearchPodcastAdapter.PodcastViewHolder> {
    private final SearchPodcastClickListener mClickListener;
    private Context mContext;

    private static final DiffUtil.ItemCallback<ResultsItem> diffUtilCallback = new DiffUtil.ItemCallback<ResultsItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ResultsItem oldItem, @NonNull ResultsItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ResultsItem oldItem, @NonNull ResultsItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };


    public SearchPodcastAdapter(SearchPodcastClickListener clickListener) {
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
        private final String EMPTY = mContext.getString(R.string.adapter_empty_string);

        PodcastViewHolder(@NonNull ItemPodcastBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            podcastBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            ResultsItem podcastItem = getItem(position);
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
                if (!TextUtils.isEmpty(podcastItem.getTitleOriginal())) {
                    binding.tvTitle.setText(podcastItem.getTitleOriginal());
                } else {
                    binding.tvTitle.setText(EMPTY);
                }
                //Publisher
                if (!TextUtils.isEmpty(podcastItem.getPublisherOriginal())) {
                    binding.tvPublisher.setText(podcastItem.getPublisherOriginal());
                } else {
                    binding.tvPublisher.setText(EMPTY);
                }
                binding.tvCountryLanguage.setVisibility(View.GONE);

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
            ResultsItem podcastItem = getItem(getAdapterPosition());
            if (podcastItem != null && mClickListener != null) {
                mClickListener.onPodcastItemClick(podcastItem.getId());
            }

        }
    }

}

