package com.pavelprymak.propodcast.presentation.paging

import androidx.paging.DataSource

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch

class SearchDataSourceFactory : DataSource.Factory<Int, ResultsItem> {
    private val mPagingStateBatch: PagingStateBatch
    private val mRepoRx: PodcastRepoRx
    private val mSearchQuery: String
    private val mLanguage: String
    private var mPrevLoadingList: List<ResultsItem>? =null

    constructor(pagingStateBatch: PagingStateBatch, repoRx: PodcastRepoRx, searchQuery: String, language: String) {
        mPagingStateBatch = pagingStateBatch
        mRepoRx = repoRx
        mSearchQuery = searchQuery
        mLanguage = language
    }

    constructor(
        pagingStateBatch: PagingStateBatch,
        repoRx: PodcastRepoRx,
        searchQuery: String,
        language: String,
        prevLoadingList: List<ResultsItem>?
    ) {
        mPagingStateBatch = pagingStateBatch
        mRepoRx = repoRx
        mSearchQuery = searchQuery
        mLanguage = language
        mPrevLoadingList = prevLoadingList
    }

    override fun create(): DataSource<Int, ResultsItem> {
        return SearchDataSource(mRepoRx, mPagingStateBatch, mSearchQuery, mLanguage, mPrevLoadingList)
    }
}
