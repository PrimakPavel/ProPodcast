package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.utils.ServerMocksUtil;

import java.io.IOException;
import java.util.List;

public class PodcastInfoViewModel extends ViewModel {
    private MutableLiveData<PodcastResponse> mPodcastData = new MutableLiveData<>();
    private MutableLiveData<List<PodcastItem>> mRecommendData = new MutableLiveData<>();

    public LiveData<PodcastResponse> getPodcastData() {
        if (mPodcastData.getValue() == null) {
            try {
                mPodcastData.setValue(ServerMocksUtil.getPodcastMock());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mPodcastData;
    }

    public LiveData<List<PodcastItem>> getRecommendData() {
        if (mRecommendData.getValue() == null || mRecommendData.getValue().size() == 0) {
            try {
                mRecommendData.setValue(ServerMocksUtil.getRecommendationMock().getRecommendations());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mRecommendData;
    }

}
