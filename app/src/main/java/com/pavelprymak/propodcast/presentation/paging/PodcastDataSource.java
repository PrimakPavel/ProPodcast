package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.common.PagingStateBatch;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PodcastDataSource extends PositionalDataSource<PodcastItem> {
    public static final int ITEMS_ON_PAGE = 20;
    private static final int INVALID_PAGE_NUMBER = -1;
    private PagingStateBatch mPagingStateBatch;
    private PodcastRepoRx mRepoRx;
    private int mCurrentPage = 1;
    private int mGenreId;
    private String mRegion;
    private List<PodcastItem> mPrevLoadingList;

    PodcastDataSource(PodcastRepoRx podcastRepo,
                      PagingStateBatch pagingStateBatch,
                      int genreId,
                      String region,
                      List<PodcastItem> prevLoadingList) {
        mPagingStateBatch = pagingStateBatch;
        mRepoRx = podcastRepo;
        mGenreId = genreId;
        mRegion = region;
        mPrevLoadingList = prevLoadingList;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<PodcastItem> callback) {
        if (mPrevLoadingList != null && !mPrevLoadingList.isEmpty()) {
            mCurrentPage = (mPrevLoadingList.size() / ITEMS_ON_PAGE) + 1;
            callback.onResult(mPrevLoadingList, 0);
            return;
        }
        mPagingStateBatch.postError(null);
        mPagingStateBatch.postLoading(true);
        mPagingStateBatch.postIsEmptyList(false);
        mRepoRx.getBestPodcasts(mGenreId, mRegion, mCurrentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BestPodcastsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BestPodcastsResponse podcastResponse) {
                        if (podcastResponse != null) {
                            mPagingStateBatch.postLoading(false);
                            if (podcastResponse.isHasNext()) {
                                mCurrentPage = podcastResponse.getNextPageNumber();
                            } else {
                                mCurrentPage = INVALID_PAGE_NUMBER;
                            }
                            mPagingStateBatch.postIsEmptyList(podcastResponse.getTotal() == 0);
                            callback.onResult(podcastResponse.getPodcasts(), 0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPagingStateBatch.postLoading(false);
                        mPagingStateBatch.postIsEmptyList(true);
                        mPagingStateBatch.postError(e);
                    }
                });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<PodcastItem> callback) {
        if (mCurrentPage == INVALID_PAGE_NUMBER) {
            return;
        }
        mPagingStateBatch.postError(null);
        mPagingStateBatch.postLoading(true);
        mPagingStateBatch.postIsEmptyList(false);
        mRepoRx.getBestPodcasts(mGenreId, mRegion, mCurrentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BestPodcastsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BestPodcastsResponse podcastResponse) {
                        if (podcastResponse != null) {
                            mPagingStateBatch.postLoading(false);
                            if (podcastResponse.isHasNext()) {
                                mCurrentPage = podcastResponse.getNextPageNumber();
                            } else {
                                mCurrentPage = INVALID_PAGE_NUMBER;
                            }
                            callback.onResult(podcastResponse.getPodcasts());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPagingStateBatch.postLoading(false);
                        mPagingStateBatch.postError(e);
                    }
                });
    }
}
