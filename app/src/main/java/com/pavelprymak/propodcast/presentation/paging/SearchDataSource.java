package com.pavelprymak.propodcast.presentation.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PositionalDataSource;

import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;
import com.pavelprymak.propodcast.model.network.pojo.search.SearchPodcastResponse;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchDataSource extends PositionalDataSource<ResultsItem> {
    private MutableLiveData<Boolean> mLoadingData;
    private MutableLiveData<Throwable> mErrorData;
    private PodcastRepoRx mRepoRx;
    private int mCurrentOffset = 0;
    private int mTotalCount;
    private String mSearchQuery;
    private String mLanguage;
    private List<ResultsItem> mPrevLoadingList;

    SearchDataSource(PodcastRepoRx podcastRepo,
                            MutableLiveData<Boolean> mLoadingData,
                            MutableLiveData<Throwable> mErrorData,
                            @NonNull String searchQuery,
                            String language,
                            List<ResultsItem> prevLoadingList) {
        this.mLoadingData = mLoadingData;
        this.mErrorData = mErrorData;
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
        mErrorData.postValue(null);
        mLoadingData.postValue(true);
        mRepoRx.getSearchData(mSearchQuery, mCurrentOffset, mLanguage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SearchPodcastResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SearchPodcastResponse searchPodcastResponse) {
                        mLoadingData.setValue(false);
                        mCurrentOffset = searchPodcastResponse.getNextOffset();
                        mTotalCount = searchPodcastResponse.getTotal();
                        callback.onResult(searchPodcastResponse.getResults(), 0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingData.setValue(false);
                        mErrorData.setValue(e);
                    }
                });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ResultsItem> callback) {
        //if offset out of pages scope
        if (mCurrentOffset > mTotalCount) {
            return;
        }
        mErrorData.postValue(null);
        mLoadingData.postValue(true);
        mRepoRx.getSearchData(mSearchQuery, mCurrentOffset, mLanguage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SearchPodcastResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SearchPodcastResponse searchPodcastResponse) {
                        mLoadingData.setValue(false);
                        mCurrentOffset = searchPodcastResponse.getNextOffset();
                        mTotalCount = searchPodcastResponse.getTotal();
                        callback.onResult(searchPodcastResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingData.setValue(false);
                        mErrorData.setValue(e);
                    }
                });
    }
}
