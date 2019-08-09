package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch
import com.pavelprymak.propodcast.presentation.paging.ITEMS_ON_PAGE
import com.pavelprymak.propodcast.presentation.paging.PodcastDataSourceFactory
import com.pavelprymak.propodcast.utils.AppExecutors
import java.util.concurrent.Executor

private const val INVALID_GENRE_ID = -1
class BestPodcastsViewModel(private val mRepo: PodcastRepoRx, executors: AppExecutors) : ViewModel() {
    private val mExecutor: Executor = executors.networkIO()
    private var mPodcastPagingLiveData: LiveData<PagedList<PodcastItem>>? = null
    private val mPodcastsData = MediatorLiveData<PagedList<PodcastItem>>()
    private val mPagingStateBatch = PagingStateBatch()

    private val mPagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(ITEMS_ON_PAGE / 2)
        .build()

    private var mGenreId = INVALID_GENRE_ID
    private var mRegion: String? = null

    fun getPagingStateBatch(): PagingStateBatch {
        return mPagingStateBatch
    }

    fun prepareBestPodcasts(genreId: Int, region: String): LiveData<PagedList<PodcastItem>> {
        if (genreId != mGenreId || region != mRegion || mPodcastsData.value == null) {
            mGenreId = genreId
            mRegion = region
            mPagingStateBatch.postLoading(true)
            mPagingStateBatch.postError(null)
            mPagingStateBatch.postIsEmptyList(false)
            val podcastDataSourceFactory = PodcastDataSourceFactory(mRepo, mPagingStateBatch, genreId, region)
            mPodcastPagingLiveData?.let {
                mPodcastsData.removeSource(it)
            }
            mPodcastPagingLiveData = LivePagedListBuilder(podcastDataSourceFactory, mPagedListConfig)
                .setFetchExecutor(mExecutor)
                .build()
            mPodcastPagingLiveData?.let { podcastPagingLiveData ->
                mPodcastsData.addSource(podcastPagingLiveData) { mPodcastsData.setValue(it) }
            }
        }
        return mPodcastsData
    }

    fun retryAfterErrorAndPrevLoadingListSize(): Int {
        if (mRegion == null || mGenreId == INVALID_GENRE_ID) return 0
        mPagingStateBatch.postLoading(false)
        mPagingStateBatch.postError(null)
        mPagingStateBatch.postIsEmptyList(false)
        var prevLoadingList: List<PodcastItem>? = null
        mPodcastsData.value?.let {
            prevLoadingList = it.subList(0, it.size)
        }
        val podcastDataSourceFactory =
            PodcastDataSourceFactory(mRepo, mPagingStateBatch, mGenreId, mRegion!!, prevLoadingList)
        mPodcastPagingLiveData?.let {
            mPodcastsData.removeSource(it)
        }
        mPodcastPagingLiveData = LivePagedListBuilder(podcastDataSourceFactory, mPagedListConfig)
            .setFetchExecutor(mExecutor)
            .build()
        mPodcastPagingLiveData?.let { podcastPagingLiveData ->
            mPodcastsData.addSource(podcastPagingLiveData) { mPodcastsData.setValue(it) }
        }
        return prevLoadingList?.size ?: 0
    }

    fun removeObservers(lifecycleOwner: LifecycleOwner) {
        mPagingStateBatch.error.removeObservers(lifecycleOwner)
        mPagingStateBatch.loading.removeObservers(lifecycleOwner)
        mPagingStateBatch.isEmptyListData.removeObservers(lifecycleOwner)
        mPodcastsData.removeObservers(lifecycleOwner)
    }
}
