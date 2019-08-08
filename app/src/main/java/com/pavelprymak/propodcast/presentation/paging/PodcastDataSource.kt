package com.pavelprymak.propodcast.presentation.paging

import androidx.paging.PositionalDataSource

import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

const val ITEMS_ON_PAGE = 20
private const val INVALID_PAGE_NUMBER = -1

class PodcastDataSource internal constructor(
    private val mRepoRx: PodcastRepoRx,
    private val mPagingStateBatch: PagingStateBatch,
    private val mGenreId: Int,
    private val mRegion: String,
    private val mPrevLoadingList: List<PodcastItem>?
) : PositionalDataSource<PodcastItem>() {
    private var mCurrentPage = 1

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<PodcastItem>) {
        if (mPrevLoadingList != null && mPrevLoadingList.isNotEmpty()) {
            mCurrentPage = mPrevLoadingList.size / ITEMS_ON_PAGE + 1
            callback.onResult(mPrevLoadingList, 0)
            return
        }
        mPagingStateBatch.postError(null)
        mPagingStateBatch.postLoading(true)
        mPagingStateBatch.postIsEmptyList(false)
        mRepoRx.getBestPodcasts(mGenreId, mRegion, mCurrentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<BestPodcastsResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(podcastResponse: BestPodcastsResponse) {
                    mPagingStateBatch.postLoading(false)
                    if (podcastResponse.isHasNext) {
                        mCurrentPage = podcastResponse.nextPageNumber
                    } else {
                        mCurrentPage = INVALID_PAGE_NUMBER
                    }
                    mPagingStateBatch.postIsEmptyList(podcastResponse.total == 0)
                    podcastResponse.podcasts?.let { callback.onResult(it, 0) }
                }

                override fun onError(e: Throwable) {
                    mPagingStateBatch.postLoading(false)
                    mPagingStateBatch.postIsEmptyList(true)
                    mPagingStateBatch.postError(e)
                }
            })
    }

    override fun loadRange( params: LoadRangeParams, callback: LoadRangeCallback<PodcastItem>) {
        if (mCurrentPage == INVALID_PAGE_NUMBER) {
            return
        }
        mPagingStateBatch.postError(null)
        mPagingStateBatch.postLoading(true)
        mPagingStateBatch.postIsEmptyList(false)
        mRepoRx.getBestPodcasts(mGenreId, mRegion, mCurrentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<BestPodcastsResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(podcastResponse: BestPodcastsResponse) {
                    mPagingStateBatch.postLoading(false)
                    if (podcastResponse.isHasNext) {
                        mCurrentPage = podcastResponse.nextPageNumber
                    } else {
                        mCurrentPage = INVALID_PAGE_NUMBER
                    }
                    podcastResponse.podcasts?.let { callback.onResult(it) }
                }

                override fun onError(e: Throwable) {
                    mPagingStateBatch.postLoading(false)
                    mPagingStateBatch.postError(e)
                }
            })
    }
}
