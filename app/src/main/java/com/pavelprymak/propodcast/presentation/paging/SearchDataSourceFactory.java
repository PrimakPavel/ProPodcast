package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch;

import java.util.List;

public class SearchDataSourceFactory extends DataSource.Factory<Integer, ResultsItem> {
    private PagingStateBatch mPagingStateBatch;
    private PodcastRepoRx mRepoRx;
    private String mSearchQuery;
    private String mLanguage;
    private List<ResultsItem> mPrevLoadingList;

    public SearchDataSourceFactory(PagingStateBatch pagingStateBatch, PodcastRepoRx repoRx, String searchQuery, String language) {
        mPagingStateBatch = pagingStateBatch;
        mRepoRx = repoRx;
        mSearchQuery = searchQuery;
        mLanguage = language;
    }

    public SearchDataSourceFactory(PagingStateBatch pagingStateBatch, PodcastRepoRx repoRx, String searchQuery, String language, List<ResultsItem> prevLoadingList) {
        mPagingStateBatch = pagingStateBatch;
        mRepoRx = repoRx;
        mSearchQuery = searchQuery;
        mLanguage = language;
        mPrevLoadingList = prevLoadingList;
    }

    @NonNull
    @Override
    public DataSource<Integer, ResultsItem> create() {
        return new SearchDataSource(mRepoRx, mPagingStateBatch, mSearchQuery, mLanguage, mPrevLoadingList);
    }
}
