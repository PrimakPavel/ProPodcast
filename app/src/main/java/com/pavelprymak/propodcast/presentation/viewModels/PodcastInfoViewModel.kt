package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.SimpleViewModel
import com.pavelprymak.propodcast.presentation.common.StatesBatch
import com.pavelprymak.propodcast.presentation.common.bindToStatesBatch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PodcastInfoViewModel(private val mRepo: PodcastRepoRx) : SimpleViewModel() {
    val podcastDataBatch = StatesBatch<PodcastResponse>()
    private val mRecommendData = MutableLiveData<List<PodcastItem>>()
    private var mId = ""

    fun setItemId(mId: String) {
        this.mId = mId
    }

    fun getRecommendData(): LiveData<List<PodcastItem>> {
        if (mId.isEmpty()) throw IllegalArgumentException()
        if (mRecommendData.value == null || mRecommendData.value?.isEmpty() == true) {
            mRepo.getPodcastRecommendations(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { recommendations -> mRecommendData.setValue(recommendations.recommendations) },
                    { error -> })
                .untilDestroy()
        }
        return mRecommendData
    }

    fun preparePodcastInfoData() {
        if (mId.isEmpty()) throw IllegalArgumentException()
        if (podcastDataBatch.data.value == null) {
            podcastDataBatch.postLoading(true)
            mRepo.getPodcastById(mId, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToStatesBatch(podcastDataBatch)
                .untilDestroy()
        }
    }

    fun loadMoreEpisodes() {
        if (mId.isEmpty()) throw IllegalArgumentException()
        podcastDataBatch.data.value?.let { currentPodcastResponse ->
            currentPodcastResponse.episodes?.let { episodes ->
                if (episodes.size < currentPodcastResponse.totalEpisodes) {
                    podcastDataBatch.postLoading(true)
                    mRepo.getPodcastById(mId, currentPodcastResponse.nextEpisodePubDate)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ podcastResponse ->
                            if (podcastResponse != null) {
                                //concat episodes lists (current and new)
                                val currentEpisodesList: ArrayList<EpisodesItem> = episodes as ArrayList<EpisodesItem>
                                podcastResponse.episodes?.let {
                                    currentEpisodesList.addAll(it)
                                }
                                podcastResponse.episodes = currentEpisodesList
                                podcastDataBatch.postData(podcastResponse)
                            }
                        }, { error -> podcastDataBatch.postError(error) })
                        .untilDestroy()
                }
            }
        }
    }

    fun removePodcastDataBatchObservers(lifecycleOwner: LifecycleOwner) {
        podcastDataBatch.data.removeObservers(lifecycleOwner)
        podcastDataBatch.loading.removeObservers(lifecycleOwner)
        podcastDataBatch.error.removeObservers(lifecycleOwner)
    }
}
