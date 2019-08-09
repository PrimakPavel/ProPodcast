package com.pavelprymak.propodcast.presentation.paging

import androidx.paging.PositionalDataSource

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class SearchDataSource(
    private val mRepoRx: PodcastRepoRx,
    private val mPagingStateBatch: PagingStateBatch,
    private val mSearchQuery: String,
    private val mLanguage: String,
    private val mPrevLoadingList: List<ResultsItem>?
) : PositionalDataSource<ResultsItem>() {
    private var mCurrentOffset = 0
    private var mTotalCount: Int = 0

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<ResultsItem>) {
        if (!mPrevLoadingList.isNullOrEmpty()) {
            callback.onResult(mPrevLoadingList, 0)
            mCurrentOffset = mPrevLoadingList.size
            mTotalCount = mPrevLoadingList.size
            return
        }
        mPagingStateBatch.postError(null)
        mPagingStateBatch.postLoading(true)
        mPagingStateBatch.postIsEmptyList(false)
        mRepoRx.getSearchData(mSearchQuery, mCurrentOffset, mLanguage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<SearchPodcastResponse> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onSuccess(searchPodcastResponse: SearchPodcastResponse) {
                    mPagingStateBatch.postLoading(false)
                    mCurrentOffset = searchPodcastResponse.nextOffset
                    mTotalCount = searchPodcastResponse.total
                    mPagingStateBatch.postIsEmptyList(searchPodcastResponse.results?.isEmpty() ?: true)
                    searchPodcastResponse.results?.let { callback.onResult(it, 0) }
                }

                override fun onError(e: Throwable) {
                    mPagingStateBatch.postLoading(false)
                    mPagingStateBatch.postError(e)
                }
            })
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ResultsItem>) {
        //if offset out of pages scope
        if (mCurrentOffset > mTotalCount) {
            return
        }
        mPagingStateBatch.postError(null)
        mPagingStateBatch.postLoading(true)
        mPagingStateBatch.postIsEmptyList(false)
        mRepoRx.getSearchData(mSearchQuery, mCurrentOffset, mLanguage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<SearchPodcastResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(searchPodcastResponse: SearchPodcastResponse) {
                    mPagingStateBatch.postLoading(false)
                    mCurrentOffset = searchPodcastResponse.nextOffset
                    mTotalCount = searchPodcastResponse.total
                    searchPodcastResponse.results?.let { callback.onResult(it) }
                }

                override fun onError(e: Throwable) {
                    mPagingStateBatch.postLoading(false)
                    mPagingStateBatch.postError(e)
                }
            })
    }
}
