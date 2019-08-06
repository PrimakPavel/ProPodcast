package com.pavelprymak.propodcast.presentation.viewModels;

import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.model.network.PodcastApiController;
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoImpl;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch;
import com.pavelprymak.propodcast.presentation.paging.SearchDataSourceFactory;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private static final int RESULTS_ON_PAGE = 10;

    private LiveData<PagedList<ResultsItem>> mSearchPagingLiveData;
    private final MediatorLiveData<PagedList<ResultsItem>> mSearchData = new MediatorLiveData<>();
    private final PagingStateBatch mPagingStateBatch = new PagingStateBatch();
    private final PodcastRepoRx mRepo = new PodcastRepoImpl(PodcastApiController.getInstance().getPodcastApi());
    private String mSearchQuery;
    private String mLanguage;
    private final PagedList.Config mPagedListConfig = new PagedList.Config.Builder().setEnablePlaceholders(false)
            .setPageSize(RESULTS_ON_PAGE / 2)
            .build();

    public void prepareSearchRequest(String searchQuery, String language) {
        mSearchQuery = searchQuery;
        mLanguage = language;
        mPagingStateBatch.postLoading(false);
        mPagingStateBatch.postError(null);
        mPagingStateBatch.postIsEmptyList(false);
        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(mPagingStateBatch, mRepo, searchQuery, language);

        if (mSearchPagingLiveData != null) {
            mSearchData.removeSource(mSearchPagingLiveData);
        }
        mSearchPagingLiveData = new LivePagedListBuilder<>(searchDataSourceFactory, mPagedListConfig)
                .setFetchExecutor(App.appExecutors.networkIO())
                .build();

        mSearchData.addSource(mSearchPagingLiveData, mSearchData::setValue);
    }

    public int retryAfterErrorAndPrevLoadingListSize() {
        if (TextUtils.isEmpty(mSearchQuery) || TextUtils.isEmpty(mLanguage)) return 0;

        mPagingStateBatch.postLoading(false);
        mPagingStateBatch.postError(null);
        mPagingStateBatch.postIsEmptyList(false);
        List<ResultsItem> prevLoadingList = null;
        if (mSearchData.getValue() != null) {
            prevLoadingList = mSearchData.getValue().subList(0, mSearchData.getValue().size());
        }
        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(mPagingStateBatch, mRepo, mSearchQuery, mLanguage, prevLoadingList);
        if (mSearchPagingLiveData != null) {
            mSearchData.removeSource(mSearchPagingLiveData);
        }
        mSearchPagingLiveData = new LivePagedListBuilder<>(searchDataSourceFactory, mPagedListConfig)
                .setFetchExecutor(App.appExecutors.networkIO())
                .build();

        mSearchData.addSource(mSearchPagingLiveData, mSearchData::setValue);
        if (prevLoadingList != null) {
            return prevLoadingList.size();
        } else return 0;
    }

    public LiveData<PagedList<ResultsItem>> getSearchResultsObserver() {
        return mSearchData;
    }

    public LiveData<Boolean> getLoadData() {
        return mPagingStateBatch.getLoading();
    }

    public LiveData<Throwable> getErrorData() {
        return mPagingStateBatch.getError();
    }

    public LiveData<Boolean> getIsEmptyListData() {
        return mPagingStateBatch.getIsEmptyListData();
    }

    public void removeObservers(LifecycleOwner lifecycleOwner) {
        mPagingStateBatch.getError().removeObservers(lifecycleOwner);
        mPagingStateBatch.getLoading().removeObservers(lifecycleOwner);
        mPagingStateBatch.getIsEmptyListData().removeObservers(lifecycleOwner);
        mSearchData.removeObservers(lifecycleOwner);
    }
}
