package com.pavelprymak.propodcast.presentation.viewModels;

import android.text.TextUtils;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.model.network.PodcastApiController;
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoImpl;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.paging.SearchDataSourceFactory;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private LiveData<PagedList<ResultsItem>> mSearchPagingLiveData;
    private static final int RESULTS_ON_PAGE = 10;
    private MediatorLiveData<PagedList<ResultsItem>> mSearchData = new MediatorLiveData<>();
    private MutableLiveData<Boolean> mLoadData = new MutableLiveData<>();
    private MutableLiveData<Throwable> mErrorData = new MutableLiveData<>();
    private PodcastRepoRx mRepo = new PodcastRepoImpl(PodcastApiController.getInstance().getPodcastApi());
    private String mSearchQuery;
    private String mLanguage;

    public void prepareSearchRequest(String searchQuery, String language) {
        mSearchQuery = searchQuery;
        mLanguage = language;
        mLoadData.postValue(false);
        mErrorData.postValue(null);
        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(mLoadData, mErrorData, mRepo, searchQuery, language);
        PagedList.Config pagedListConfig = new PagedList.Config.Builder().setEnablePlaceholders(false)
                .setPageSize(RESULTS_ON_PAGE)
                .build();

        if (mSearchPagingLiveData != null) {
            mSearchData.removeSource(mSearchPagingLiveData);
        }
        mSearchPagingLiveData = new LivePagedListBuilder<>(searchDataSourceFactory, pagedListConfig)
                .setFetchExecutor(App.appExecutors.networkIO())
                .build();

        mSearchData.addSource(mSearchPagingLiveData, resultsItems -> mSearchData.setValue(resultsItems));
    }

    public int retryAfterErrorAndPrevLoadingListSize() {
        if (TextUtils.isEmpty(mSearchQuery) || TextUtils.isEmpty(mLanguage)) return 0;

        mLoadData.postValue(false);
        mErrorData.postValue(null);
        List<ResultsItem> prevLoadingList = null;
        if (mSearchData.getValue() != null) {
            prevLoadingList = mSearchData.getValue().subList(0, mSearchData.getValue().size());
        }

        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(mLoadData, mErrorData, mRepo, mSearchQuery, mLanguage, prevLoadingList);
        PagedList.Config pagedListConfig = new PagedList.Config.Builder().setEnablePlaceholders(false)
                .setPageSize(RESULTS_ON_PAGE)
                .build();

        if (mSearchPagingLiveData != null) {
            mSearchData.removeSource(mSearchPagingLiveData);
        }
        mSearchPagingLiveData = new LivePagedListBuilder<>(searchDataSourceFactory, pagedListConfig)
                .setFetchExecutor(App.appExecutors.networkIO())
                .build();

        mSearchData.addSource(mSearchPagingLiveData, resultsItems -> mSearchData.setValue(resultsItems));
        if (prevLoadingList != null) {
            return prevLoadingList.size();
        } else return 0;
    }

    public LiveData<PagedList<ResultsItem>> getSearchResultsObserver() {
        return mSearchData;
    }

    public LiveData<Boolean> getLoadData() {
        return mLoadData;
    }

    public LiveData<Throwable> getErrorData() {
        return mErrorData;
    }
}
