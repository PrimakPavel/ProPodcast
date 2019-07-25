package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.model.network.pojo.genres.GenresItem;
import com.pavelprymak.propodcast.utils.ServerMocksUtil;

import java.io.IOException;
import java.util.List;

public class GenreViewModel extends ViewModel {
    private MutableLiveData<List<GenresItem>> mGenresData = new MutableLiveData<>();

    public LiveData<List<GenresItem>> getGenres() {
        if (mGenresData.getValue() == null) {
            try {
                mGenresData.setValue(ServerMocksUtil.getGenresMock().getGenres());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mGenresData;
    }
}
