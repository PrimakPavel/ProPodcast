package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;

import java.util.List;

public class PodcastDataSourceFactory extends DataSource.Factory<Integer, PodcastItem> {
    private MutableLiveData<Boolean> mLoadingData;
    private MutableLiveData<Throwable> mErrorData;
    private PodcastRepoRx mRepoRx;
    private int mGenreId;
    private String mRegion;
    private List<PodcastItem> mPrevLoadingList;

    public PodcastDataSourceFactory(PodcastRepoRx repoRx,
                                    MutableLiveData<Boolean> loadingData,
                                    MutableLiveData<Throwable> errorData,
                                    int genreId,
                                    String region) {
        mLoadingData = loadingData;
        mErrorData = errorData;
        mRepoRx = repoRx;
        mGenreId = genreId;
        mRegion = region;
    }

    public PodcastDataSourceFactory(PodcastRepoRx repoRx,
                                    MutableLiveData<Boolean> loadingData,
                                    MutableLiveData<Throwable> errorData,
                                    int genreId,
                                    String region,
                                    List<PodcastItem> prevLoadingList) {
        mLoadingData = loadingData;
        mErrorData = errorData;
        mRepoRx = repoRx;
        mGenreId = genreId;
        mRegion = region;
        mPrevLoadingList = prevLoadingList;
    }

    @NonNull
    @Override
    public DataSource<Integer, PodcastItem> create() {
        return new PodcastDataSource(mRepoRx, mLoadingData, mErrorData, mGenreId, mRegion, mPrevLoadingList);
    }
}
