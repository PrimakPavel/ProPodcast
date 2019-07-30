package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;

import java.util.List;

public class SearchDataSourceFactory extends DataSource.Factory<Integer, ResultsItem> {
    private MutableLiveData<Boolean> mLoadingData;
    private MutableLiveData<Throwable> mErrorData;
    private PodcastRepoRx mRepoRx;
    private String mSearchQuery;
    private String mLanguage;
    private List<ResultsItem> mPrevLoadingList;

    public SearchDataSourceFactory(MutableLiveData<Boolean> loadingData, MutableLiveData<Throwable> errorData, PodcastRepoRx repoRx, String searchQuery, String language) {
        mLoadingData = loadingData;
        mErrorData = errorData;
        mRepoRx = repoRx;
        mSearchQuery = searchQuery;
        mLanguage = language;
    }

    public SearchDataSourceFactory(MutableLiveData<Boolean> loadingData, MutableLiveData<Throwable> errorData, PodcastRepoRx repoRx, String searchQuery, String language, List<ResultsItem> prevLoadingList) {
        mLoadingData = loadingData;
        mErrorData = errorData;
        mRepoRx = repoRx;
        mSearchQuery = searchQuery;
        mLanguage = language;
        mPrevLoadingList = prevLoadingList;
    }

    @NonNull
    @Override
    public DataSource<Integer, ResultsItem> create() {
        return new SearchDataSource(mRepoRx, mLoadingData, mErrorData, mSearchQuery, mLanguage, mPrevLoadingList);
    }
}
