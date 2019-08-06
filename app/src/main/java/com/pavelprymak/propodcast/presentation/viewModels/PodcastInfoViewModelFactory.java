package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PodcastInfoViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final String mId;

    public PodcastInfoViewModelFactory(String mId) {
        this.mId = mId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PodcastInfoViewModel.class)) {
            return (T) new PodcastInfoViewModel(mId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
