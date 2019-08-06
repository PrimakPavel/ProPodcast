package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.model.network.PodcastApiController;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoImpl;
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx;
import com.pavelprymak.propodcast.presentation.common.StatesBatch;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PodcastInfoViewModel extends ViewModel {
    private final StatesBatch<PodcastResponse> mPodcastDataBatch = new StatesBatch<>();
    private final MutableLiveData<List<PodcastItem>> mRecommendData = new MutableLiveData<>();
    private final PodcastRepoRx mRepo = new PodcastRepoImpl(PodcastApiController.getInstance().getPodcastApi());
    private final String mId;

    PodcastInfoViewModel(String mId) {
        this.mId = mId;
    }

    public void preparePodcastInfoData() {
        if (mPodcastDataBatch.getData().getValue() == null) {
            mPodcastDataBatch.postLoading(true);
            Disposable disposable = mRepo.getPodcastById(mId, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mPodcastDataBatch::postData, mPodcastDataBatch::postError);
        }
    }

    public void loadMoreEpisoded() {
        PodcastResponse currentPodcastResponse = mPodcastDataBatch.getData().getValue();
        if (currentPodcastResponse != null && currentPodcastResponse.getEpisodes() != null && currentPodcastResponse.getEpisodes().size() < currentPodcastResponse.getTotalEpisodes()) {
            mPodcastDataBatch.postLoading(true);
            Disposable disposable = mRepo.getPodcastById(mId, currentPodcastResponse.getNextEpisodePubDate())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(podcastResponse -> {
                        if (podcastResponse != null) {
                            //concat episodes lists (current and new)
                            List<EpisodesItem> currentEpisodesList = currentPodcastResponse.getEpisodes();
                            if (podcastResponse.getEpisodes() != null) {
                                currentEpisodesList.addAll(podcastResponse.getEpisodes());
                            }
                            podcastResponse.setEpisodes(currentEpisodesList);
                            mPodcastDataBatch.postData(podcastResponse);
                        }
                    }, mPodcastDataBatch::postError);
        }

    }

    public StatesBatch<PodcastResponse> getPodcastDataBatch() {
        return mPodcastDataBatch;
    }

    public void removePodcastDataBatchObservers(LifecycleOwner lifecycleOwner) {
        mPodcastDataBatch.getData().removeObservers(lifecycleOwner);
        mPodcastDataBatch.getLoading().removeObservers(lifecycleOwner);
        mPodcastDataBatch.getError().removeObservers(lifecycleOwner);
    }

    public LiveData<List<PodcastItem>> getRecommendData() {
        if (mRecommendData.getValue() == null || mRecommendData.getValue().size() == 0) {
            Disposable disposable = mRepo.getPodcastRecommendations(mId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(recommendations -> mRecommendData.setValue(recommendations.getRecommendations()), error -> {
                    });
        }
        return mRecommendData;
    }

}
