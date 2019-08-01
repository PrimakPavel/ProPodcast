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
import com.pavelprymak.propodcast.databinding.ItemFilterLanguageBinding;

import java.util.List;

import static com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_LANGUAGES;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    // Use default locale format
    private List<String> mLanguages;
    private final LanguageClickListener mClickListener;
    private Context mContext;
    private String mSelectedLanguage = ALL_LANGUAGES;


    public LanguageAdapter(LanguageClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void updateList(List<String> languages, @NonNull String selectedLanguage) {
        mLanguages = languages;
        mSelectedLanguage = selectedLanguage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemFilterLanguageBinding itemFilterGenreBinding = DataBindingUtil.inflate(inflater, R.layout.item_filter_language, parent, false);
        return new LanguageViewHolder(itemFilterGenreBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mLanguages == null) return 0;
        else return mLanguages.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemFilterLanguageBinding binding;
        private static final String EMPTY = "";

        LanguageViewHolder(@NonNull ItemFilterLanguageBinding regionBinding) {
            super(regionBinding.getRoot());
            this.binding = regionBinding;
            regionBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            String language = mLanguages.get(position);
            if (language != null && mContext != null) {

                //Title
                if (!TextUtils.isEmpty(language)) {
                    binding.tvValue.setText(language);
                } else {
                    binding.tvValue.setText(EMPTY);
                }
                if (language.equals(mSelectedLanguage)) {
                    binding.ivIsSelected.setVisibility(View.VISIBLE);
                } else {
                    binding.ivIsSelected.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mLanguages != null) {
                String language = mLanguages.get(getAdapterPosition());
                if (language != null && !mSelectedLanguage.equals(language)) {
                    int previousPosition = getPositionByLanguageName(mSelectedLanguage);
                    mSelectedLanguage = language;
                    notifyItemChanged(previousPosition);
                    notifyItemChanged(getAdapterPosition());
                    if (mClickListener != null)
                        mClickListener.onLanguageItemClick(language);
                }
            }
        }
    }

    public int getPositionByLanguageName(String regShortName) {
        if (mLanguages != null && regShortName != null) {
            for (int i = 0; i < mLanguages.size(); i++) {
                String region = mLanguages.get(i);
                if (region.equals(regShortName)) {
                    return i;
                }
            }
        }
        return 0;
    }

}


