package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.model.network.PodcastApiController;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoImpl;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.paging.PodcastDataSourceFactory;

import java.util.List;
import java.util.concurrent.Executor;

import static com.pavelprymak.propodcast.presentation.paging.PodcastDataSource.ITEMS_ON_PAGE;

public class BestPodcastsViewModel extends ViewModel {
    public static final int INVALID_GENRE_ID = -1;
    private PodcastRepoRx mRepo = new PodcastRepoImpl(PodcastApiController.getInstance().getPodcastApi());
    private Executor mExecutor = App.appExecutors.networkIO();

    private LiveData<PagedList<PodcastItem>> mPodcastPagingLiveData;
    private MediatorLiveData<PagedList<PodcastItem>> mPodcastsData = new MediatorLiveData<>();
    private MutableLiveData<Boolean> mLoadData = new MutableLiveData<>();
    private MutableLiveData<Throwable> mErrorData = new MutableLiveData<>();

    private PagedList.Config mPagedListConfig = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEMS_ON_PAGE / 2)
            .build();

    private int mGenreId = INVALID_GENRE_ID;
    private String mRegion;


    public void prepareBestPodcasts(int genreId, String region) {
        if (genreId != mGenreId || !region.equals(mRegion) || mPodcastsData.getValue() == null) {
            mGenreId = genreId;
            mRegion = region;
            mLoadData.postValue(false);
            mErrorData.postValue(null);
            PodcastDataSourceFactory podcastDataSourceFactory = new PodcastDataSourceFactory(mRepo, mLoadData, mErrorData, genreId, region);
            if (mPodcastPagingLiveData != null) {
                mPodcastsData.removeSource(mPodcastPagingLiveData);
            }
            mPodcastPagingLiveData = new LivePagedListBuilder<>(podcastDataSourceFactory, mPagedListConfig)
                    .setFetchExecutor(mExecutor)
                    .build();

            mPodcastsData.addSource(mPodcastPagingLiveData, resultsItems -> mPodcastsData.setValue(resultsItems));
        }
    }

    public int retryAfterErrorAndPrevLoadingListSize() {
        if (mRegion == null || mGenreId == INVALID_GENRE_ID) return 0;

        mLoadData.postValue(false);
        mErrorData.postValue(null);
        List<PodcastItem> prevLoadingList = null;
        if (mPodcastsData.getValue() != null) {
            prevLoadingList = mPodcastsData.getValue().subList(0, mPodcastsData.getValue().size());
        }
        PodcastDataSourceFactory podcastDataSourceFactory = new PodcastDataSourceFactory(mRepo, mLoadData, mErrorData, mGenreId, mRegion, prevLoadingList);
        if (mPodcastPagingLiveData != null) {
            mPodcastsData.removeSource(mPodcastPagingLiveData);
        }
        mPodcastPagingLiveData = new LivePagedListBuilder<>(podcastDataSourceFactory, mPagedListConfig)
                .setFetchExecutor(mExecutor)
                .build();

        mPodcastsData.addSource(mPodcastPagingLiveData, resultsItems -> mPodcastsData.setValue(resultsItems));
        if (prevLoadingList != null) {
            return prevLoadingList.size();
        } else return 0;
    }

    public LiveData<PagedList<PodcastItem>> getBestPodcastsObserver() {
        return mPodcastsData;
    }

    public MutableLiveData<Boolean> getLoadData() {
        return mLoadData;
    }

    public MutableLiveData<Throwable> getErrorData() {
        return mErrorData;
    }
}
