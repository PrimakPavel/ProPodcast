package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.utils.ServerMocksUtil;

import java.io.IOException;
import java.util.List;

public class LanguageViewModel extends ViewModel {
    private MutableLiveData<List<String>> mLanguagesData = new MutableLiveData<>();

    public LiveData<List<String>> getLanguages() {
        if (mLanguagesData.getValue() == null) {
            try {
                mLanguagesData.setValue((ServerMocksUtil.getLanguagesMock().getLanguages()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mLanguagesData;
    }
}
