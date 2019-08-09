package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LifecycleOwner
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.model.network.pojo.recommendations.RecommendationsResponse
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.SimpleViewModel
import com.pavelprymak.propodcast.presentation.common.StatesBatch
import com.pavelprymak.propodcast.presentation.common.bindToStatesBatch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PodcastInfoViewModel(private val mRepo: PodcastRepoRx) : SimpleViewModel() {
    private val mPodcastDataBatch = StatesBatch<PodcastResponse>()
    private val mRecommendDataBatch = StatesBatch<List<PodcastItem>?>()
    private var mId = ""

    fun setItemId(mId: String) {
        this.mId = mId
    }

    fun prepareRecommendData(): StatesBatch<List<PodcastItem>?> {
        if (mId.isEmpty()) throw IllegalArgumentException()
        if (mRecommendDataBatch.data.value == null) {
            mRepo.getPodcastRecommendations(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { recommendResponse: RecommendationsResponse -> recommendResponse.recommendations }
                .bindToStatesBatch(mRecommendDataBatch)
                .untilDestroy()
        }
        return mRecommendDataBatch
    }

    fun preparePodcastInfoData(): StatesBatch<PodcastResponse> {
        if (mId.isEmpty()) throw IllegalArgumentException()
        if (mPodcastDataBatch.data.value == null) {
            mPodcastDataBatch.postLoading(true)
            mRepo.getPodcastById(mId, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToStatesBatch(mPodcastDataBatch)
                .untilDestroy()
        }
        return mPodcastDataBatch
    }

    fun loadMoreEpisodes() {
        if (mId.isEmpty()) throw IllegalArgumentException()
        mPodcastDataBatch.data.value?.let { prevPodcastResponse ->
            prevPodcastResponse.episodes?.let { prevEpisodes ->
                if (prevEpisodes.size < prevPodcastResponse.totalEpisodes) {
                    mPodcastDataBatch.postLoading(true)
                    mRepo.getPodcastById(mId, prevPodcastResponse.nextEpisodePubDate)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        //concat episodes lists (current and new)
                        .map { podcastInfo: PodcastResponse ->
                            concatPrevWithCurrentEpisodes(
                                podcastInfo,
                                prevEpisodes
                            )
                        }
                        .bindToStatesBatch(mPodcastDataBatch)
                        .untilDestroy()
                }
            }
        }
    }

    private fun concatPrevWithCurrentEpisodes(
        podcastInfo: PodcastResponse,
        episodes: List<EpisodesItem>
    ): PodcastResponse {
        val prevEpisodesList: ArrayList<EpisodesItem> = episodes as ArrayList<EpisodesItem>
        podcastInfo.episodes?.let { currentEpisodes ->
            prevEpisodesList.addAll(currentEpisodes)
        }
        podcastInfo.episodes = prevEpisodesList
        return podcastInfo
    }

    fun removePodcastDataBatchObservers(lifecycleOwner: LifecycleOwner) {
        mPodcastDataBatch.data.removeObservers(lifecycleOwner)
        mPodcastDataBatch.loading.removeObservers(lifecycleOwner)
        mPodcastDataBatch.error.removeObservers(lifecycleOwner)
        mRecommendDataBatch.data.removeObservers(lifecycleOwner)
        mRecommendDataBatch.loading.removeObservers(lifecycleOwner)
        mRecommendDataBatch.error.removeObservers(lifecycleOwner)
    }
}
