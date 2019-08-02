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
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.utils.DateFormatUtil;

import java.util.Date;
import java.util.List;

public class FavoriteEpisodeAdapter extends RecyclerView.Adapter<FavoriteEpisodeAdapter.FavoriteEpisodeViewHolder> {
    private List<FavoriteEpisodeEntity> mFavorites;
    private final FavoriteEpisodeClickListener mClickListener;
    private Context mContext;


    public FavoriteEpisodeAdapter(FavoriteEpisodeClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void updateList(List<FavoriteEpisodeEntity> episodes) {
        mFavorites = episodes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteEpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemEpisodeBinding itemPodcastBinding = DataBindingUtil.inflate(inflater, R.layout.item_episode, parent, false);
        return new FavoriteEpisodeViewHolder(itemPodcastBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteEpisodeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mFavorites == null) return 0;
        else return mFavorites.size();
    }

    class FavoriteEpisodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemEpisodeBinding binding;
        private static final String EMPTY = "";

        FavoriteEpisodeViewHolder(@NonNull ItemEpisodeBinding episodesBinding) {
            super(episodesBinding.getRoot());
            this.binding = episodesBinding;
            episodesBinding.itemContainer.setOnClickListener(this);
        }

        void bind(int position) {
            FavoriteEpisodeEntity episodeItem = mFavorites.get(position);
            if (episodeItem != null && mContext != null) {

                //Episodes Duration
                if ((episodeItem.getAudioLengthSec() > 0)) {
                    binding.tvEpisodeDuration.setText(DateFormatUtil.formatTimeHHmmss(episodeItem.getAudioLengthSec()));
                } else {
                    binding.tvEpisodeDuration.setText(EMPTY);
                }
                //Title
                if (!TextUtils.isEmpty(episodeItem.getTitle())) {
                    binding.tvTitle.setText(episodeItem.getTitle().trim());
                } else {
                    binding.tvTitle.setText(EMPTY);
                }
                //Description
                if (!TextUtils.isEmpty(episodeItem.getDescription())) {
                    binding.tvDescription.setText(Html.fromHtml(episodeItem.getDescription().trim()));
                } else {
                    binding.tvDescription.setText(EMPTY);
                }

                //Publish date
                if (episodeItem.getPubDateMs() > 0L) {
                    Date publishDate = new Date(episodeItem.getPubDateMs());
                    binding.tvPublishedDate.setText(R.string.published_date_label);
                    binding.tvPublishedDate.append(DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate));
                }

                binding.ivMoreOptions.setOnClickListener(v -> {
                    if (mClickListener != null) {
                        mClickListener.onEpisodeMoreOptionsClick(episodeItem.getId(), episodeItem.getListennotesUrl(), v);
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            if (mFavorites != null) {
                FavoriteEpisodeEntity episodeItem = mFavorites.get(getAdapterPosition());
                if (episodeItem != null && mClickListener != null) {
                    mClickListener.onEpisodeItemClick(episodeItem);
                }
            }
        }
    }
}
