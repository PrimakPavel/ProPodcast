package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.utils.ServerMocksUtil;

import java.io.IOException;
import java.util.List;

public class BestPodcastsViewModel extends ViewModel {
    private MutableLiveData<List<PodcastItem>> mBestPodcastsData = new MutableLiveData<>();


    public LiveData<List<PodcastItem>> getBestPodcasts() {
        if (mBestPodcastsData.getValue() == null)
            try {
                BestPodcastsResponse bestPodcastsResponse = ServerMocksUtil.getBestPodcastsMock();
                mBestPodcastsData.setValue(bestPodcastsResponse.getPodcasts());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return mBestPodcastsData;
    }
}
