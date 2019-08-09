package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch
import com.pavelprymak.propodcast.presentation.paging.SearchDataSourceFactory
import com.pavelprymak.propodcast.utils.AppExecutors
import java.util.concurrent.Executor

private const val RESULTS_ON_PAGE = 10
class SearchViewModel(private val mRepo: PodcastRepoRx, executors: AppExecutors) : ViewModel() {
    private val mExecutor: Executor = executors.networkIO()

    private var mSearchPagingLiveData: LiveData<PagedList<ResultsItem>>? = null
    private val mSearchData = MediatorLiveData<PagedList<ResultsItem>>()
    private val mPagingStateBatch = PagingStateBatch()

    private var mSearchQuery: String? = null
    private var mLanguage: String? = null
    private val mPagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(false)
        .setPageSize(RESULTS_ON_PAGE / 2)
        .build()

    fun getSearchResultsObserver(): LiveData<PagedList<ResultsItem>>{
        return mSearchData
    }

    fun getPagingStateBatch(): PagingStateBatch {
        return mPagingStateBatch
    }

    fun prepareSearchRequest(searchQuery: String, language: String) {
        mSearchQuery = searchQuery
        mLanguage = language
        mPagingStateBatch.postLoading(false)
        mPagingStateBatch.postError(null)
        mPagingStateBatch.postIsEmptyList(false)
        val searchDataSourceFactory = SearchDataSourceFactory(mPagingStateBatch, mRepo, searchQuery, language)

        mSearchPagingLiveData?.let {
            mSearchData.removeSource(it)
        }
        mSearchPagingLiveData = LivePagedListBuilder(searchDataSourceFactory, mPagedListConfig)
            .setFetchExecutor(mExecutor)
            .build()
        mSearchPagingLiveData?.let { searchPagingLiveData ->
            mSearchData.addSource(searchPagingLiveData) { mSearchData.setValue(it) }
        }
    }

    fun retryAfterErrorAndPrevLoadingListSize(): Int {
        if (mSearchQuery.isNullOrEmpty() || mLanguage.isNullOrEmpty()) return 0

        mPagingStateBatch.postLoading(false)
        mPagingStateBatch.postError(null)
        mPagingStateBatch.postIsEmptyList(false)
        var prevLoadingList: List<ResultsItem>? = null
        mSearchData.value?.let {
            prevLoadingList = it.subList(0, it.size)
        }
        val searchDataSourceFactory =
            SearchDataSourceFactory(mPagingStateBatch, mRepo, mSearchQuery!!, mLanguage!!, prevLoadingList)
        mSearchPagingLiveData?.let {
            mSearchData.removeSource(it)
        }
        mSearchPagingLiveData = LivePagedListBuilder(searchDataSourceFactory, mPagedListConfig)
            .setFetchExecutor(mExecutor)
            .build()
        mSearchPagingLiveData?.let { searchPagingLiveData ->
            mSearchData.addSource(searchPagingLiveData) { mSearchData.setValue(it) }
        }
        return prevLoadingList?.size ?: 0
    }

    fun removeObservers(lifecycleOwner: LifecycleOwner) {
        mPagingStateBatch.error.removeObservers(lifecycleOwner)
        mPagingStateBatch.loading.removeObservers(lifecycleOwner)
        mPagingStateBatch.isEmptyListData.removeObservers(lifecycleOwner)
        mSearchData.removeObservers(lifecycleOwner)
    }
}
