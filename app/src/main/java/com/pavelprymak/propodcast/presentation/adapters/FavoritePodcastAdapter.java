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
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritePodcastAdapter extends RecyclerView.Adapter<FavoritePodcastAdapter.FavoritePodcastViewHolder> {

    private List<FavoritePodcastEntity> mFavorites;
    private final FavoritePodcastClickListener mClickListener;
    private Context mContext;


    public FavoritePodcastAdapter(FavoritePodcastClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void updateList(List<FavoritePodcastEntity> podcasts) {
        mFavorites = podcasts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritePodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemPodcastBinding itemPodcastBinding = DataBindingUtil.inflate(inflater, R.layout.item_podcast, parent, false);
        return new FavoritePodcastViewHolder(itemPodcastBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePodcastViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mFavorites == null) return 0;
        else return mFavorites.size();
    }

    class FavoritePodcastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemPodcastBinding binding;
        private static final String EMPTY = "";

        FavoritePodcastViewHolder(@NonNull ItemPodcastBinding podcastBinding) {
            super(podcastBinding.getRoot());
            this.binding = podcastBinding;
            podcastBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            FavoritePodcastEntity podcastItem = mFavorites.get(position);
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
                binding.tvEpisodesCount.setVisibility(View.GONE);
                //Country(Language)
                if (!TextUtils.isEmpty(podcastItem.getCountry())) {
                    binding.tvCountryLanguage.setText(podcastItem.getCountry());
                    if (!TextUtils.isEmpty(podcastItem.getLanguage())) {
                        binding.tvCountryLanguage.append("(" + podcastItem.getLanguage() + ")");
                    }
                } else {
                    binding.tvCountryLanguage.setText(EMPTY);
                }
                binding.ivMoreOptions.setOnClickListener(v -> {
                    if (mClickListener != null)
                        mClickListener.onPodcastMoreOptionsClick(podcastItem.getId(), podcastItem.getListennotesUrl(), v);
                });
            }
        }

        @Override
        public void onClick(View v) {
            if (mFavorites != null) {
                FavoritePodcastEntity podcastItem = mFavorites.get(getAdapterPosition());
                if (podcastItem != null && mClickListener != null) {
                    mClickListener.onPodcastItemClick(podcastItem.getId());
                }
            }
        }
    }

}

