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
import com.pavelprymak.propodcast.databinding.ItemFilterGenreBinding;
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresItem;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    // Use default locale format
    private List<GenresItem> mGenres;
    private final GenreClickListener clickListener;
    private Context mContext;
    private int mSelectedGenreId;


    public GenreAdapter(GenreClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateList(List<GenresItem> genresItems, int selectedGenreId) {
        mGenres = genresItems;
        mSelectedGenreId = selectedGenreId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemFilterGenreBinding itemFilterGenreBinding = DataBindingUtil.inflate(inflater, R.layout.item_filter_genre, parent, false);
        return new GenreViewHolder(itemFilterGenreBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mGenres == null) return 0;
        else return mGenres.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemFilterGenreBinding binding;
        private final String EMPTY = mContext.getString(R.string.adapter_empty_string);

        GenreViewHolder(@NonNull ItemFilterGenreBinding genderBinding) {
            super(genderBinding.getRoot());
            this.binding = genderBinding;
            genderBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            GenresItem genresItem = mGenres.get(position);
            if (genresItem != null && mContext != null) {

                //Title
                if (!TextUtils.isEmpty(genresItem.getName())) {
                    binding.tvValue.setText(genresItem.getName());
                } else {
                    binding.tvValue.setText(EMPTY);
                }
                if (genresItem.getId() == mSelectedGenreId) {
                    binding.ivIsSelected.setVisibility(View.VISIBLE);
                } else {
                    binding.ivIsSelected.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mGenres != null) {
                GenresItem genresItem = mGenres.get(getAdapterPosition());
                if (genresItem != null && mSelectedGenreId != genresItem.getId()) {
                    int previousPosition = getPositionByGenreId(mSelectedGenreId);
                    mSelectedGenreId = genresItem.getId();
                    notifyItemChanged(previousPosition);
                    notifyItemChanged(getAdapterPosition());
                    clickListener.onGenreClick(genresItem.getId());
                }
            }
        }


    }

    public int getPositionByGenreId(int genderId) {
        if (mGenres != null && genderId > 0) {
            for (int i = 0; i < mGenres.size(); i++) {
                GenresItem genresItem = mGenres.get(i);
                if (genresItem.getId() == genderId) {
                    return i;
                }
            }
        }
        return 0;
    }

}
