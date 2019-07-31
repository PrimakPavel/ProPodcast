package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PositionalDataSource;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.BestPodcastsResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PodcastDataSource extends PositionalDataSource<PodcastItem> {
    public static final int ITEMS_ON_PAGE = 20;
    private static final int INVALID_PAGE_NUMBER = -1;
    private MutableLiveData<Boolean> mLoadingData;
    private MutableLiveData<Throwable> mErrorData;
    private PodcastRepoRx mRepoRx;
    private int mCurrentPage = 1;
    private int mGenreId;
    private String mRegion;
    private List<PodcastItem> mPrevLoadingList;

    PodcastDataSource(PodcastRepoRx podcastRepo,
                      MutableLiveData<Boolean> loadingData,
                      MutableLiveData<Throwable> errorData,
                      int genreId,
                      String region,
                      List<PodcastItem> prevLoadingList) {
        mLoadingData = loadingData;
        mErrorData = errorData;
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
        mErrorData.postValue(null);
        mLoadingData.postValue(true);
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
                            mLoadingData.setValue(false);
                            if (podcastResponse.isHasNext()) {
                                mCurrentPage = podcastResponse.getNextPageNumber();
                            } else {
                                mCurrentPage = INVALID_PAGE_NUMBER;
                            }
                            callback.onResult(podcastResponse.getPodcasts(), 0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingData.setValue(false);
                        mErrorData.setValue(e);
                    }
                });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<PodcastItem> callback) {
        if (mCurrentPage == INVALID_PAGE_NUMBER) {
            return;
        }
        mErrorData.postValue(null);
        mLoadingData.postValue(true);
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
                            mLoadingData.setValue(false);
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
                        mLoadingData.setValue(false);
                        mErrorData.setValue(e);
                    }
                });
    }
}
