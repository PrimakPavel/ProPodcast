package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch;

import java.util.List;

public class PodcastDataSourceFactory extends DataSource.Factory<Integer, PodcastItem> {
    private final PagingStateBatch mPagingStateBatch;
    private final PodcastRepoRx mRepoRx;
    private final int mGenreId;
    private final String mRegion;
    private List<PodcastItem> mPrevLoadingList;

    public PodcastDataSourceFactory(PodcastRepoRx repoRx,
                                    PagingStateBatch pagingStateBatch,
                                    int genreId,
                                    String region) {
        mPagingStateBatch = pagingStateBatch;
        mRepoRx = repoRx;
        mGenreId = genreId;
        mRegion = region;
    }

    public PodcastDataSourceFactory(PodcastRepoRx repoRx,
                                    PagingStateBatch pagingStateBatch,
                                    int genreId,
                                    String region,
                                    List<PodcastItem> prevLoadingList) {
        mPagingStateBatch = pagingStateBatch;
        mRepoRx = repoRx;
        mGenreId = genreId;
        mRegion = region;
        mPrevLoadingList = prevLoadingList;
    }

    @NonNull
    @Override
    public DataSource<Integer, PodcastItem> create() {
        return new PodcastDataSource(mRepoRx, mPagingStateBatch, mGenreId, mRegion, mPrevLoadingList);
    }
}
