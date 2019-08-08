package com.pavelprymak.propodcast.presentation.paging

import androidx.paging.DataSource

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch

class PodcastDataSourceFactory : DataSource.Factory<Int, PodcastItem> {
    private val mPagingStateBatch: PagingStateBatch
    private val mRepoRx: PodcastRepoRx
    private val mGenreId: Int
    private val mRegion: String
    private var mPrevLoadingList: List<PodcastItem>? = null

    constructor(
        repoRx: PodcastRepoRx,
        pagingStateBatch: PagingStateBatch,
        genreId: Int,
        region: String
    ) {
        mPagingStateBatch = pagingStateBatch
        mRepoRx = repoRx
        mGenreId = genreId
        mRegion = region
    }

    constructor(
        repoRx: PodcastRepoRx,
        pagingStateBatch: PagingStateBatch,
        genreId: Int,
        region: String,
        prevLoadingList: List<PodcastItem>?
    ) {
        mPagingStateBatch = pagingStateBatch
        mRepoRx = repoRx
        mGenreId = genreId
        mRegion = region
        mPrevLoadingList = prevLoadingList
    }

    override fun create(): DataSource<Int, PodcastItem> {
        return PodcastDataSource(mRepoRx, mPagingStateBatch, mGenreId, mRegion, mPrevLoadingList)
    }
}
