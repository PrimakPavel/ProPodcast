package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class SearchDataSource extends PositionalDataSource<ResultsItem> {
    private final PagingStateBatch mPagingStateBatch;
    private final PodcastRepoRx mRepoRx;
    private int mCurrentOffset = 0;
    private int mTotalCount;
    private final String mSearchQuery;
    private final String mLanguage;
    private final List<ResultsItem> mPrevLoadingList;

    SearchDataSource(PodcastRepoRx podcastRepo,
                     PagingStateBatch pagingStateBatch,
                     @NonNull String searchQuery,
                     String language,
                     List<ResultsItem> prevLoadingList) {
        mPagingStateBatch = pagingStateBatch;
        mRepoRx = podcastRepo;
        mSearchQuery = searchQuery;
        mLanguage = language;
        mPrevLoadingList = prevLoadingList;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ResultsItem> callback) {
        if (mPrevLoadingList != null && !mPrevLoadingList.isEmpty()) {
            callback.onResult(mPrevLoadingList, 0);
            mCurrentOffset = mPrevLoadingList.size();
            mTotalCount = mPrevLoadingList.size();
            return;
        }
        mPagingStateBatch.postError(null);
        mPagingStateBatch.postLoading(true);
        mPagingStateBatch.postIsEmptyList(false);
        mRepoRx.getSearchData(mSearchQuery, mCurrentOffset, mLanguage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SearchPodcastResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SearchPodcastResponse searchPodcastResponse) {
                        mPagingStateBatch.postLoading(false);
                        mCurrentOffset = searchPodcastResponse.getNextOffset();
                        mTotalCount = searchPodcastResponse.getTotal();
                        mPagingStateBatch.postIsEmptyList(searchPodcastResponse.getResults().isEmpty());
                        callback.onResult(searchPodcastResponse.getResults(), 0);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mPagingStateBatch.postLoading(false);
                        mPagingStateBatch.postError(e);
                    }
                });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ResultsItem> callback) {
        //if offset out of pages scope
        if (mCurrentOffset > mTotalCount) {
            return;
        }
        mPagingStateBatch.postError(null);
        mPagingStateBatch.postLoading(true);
        mPagingStateBatch.postIsEmptyList(false);
        mRepoRx.getSearchData(mSearchQuery, mCurrentOffset, mLanguage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SearchPodcastResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SearchPodcastResponse searchPodcastResponse) {
                        mPagingStateBatch.postLoading(false);
                        mCurrentOffset = searchPodcastResponse.getNextOffset();
                        mTotalCount = searchPodcastResponse.getTotal();
                        callback.onResult(searchPodcastResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPagingStateBatch.postLoading(false);
                        mPagingStateBatch.postError(e);
                    }
                });
    }
}
