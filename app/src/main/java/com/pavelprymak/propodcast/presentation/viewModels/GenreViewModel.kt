package com.pavelprymak.propodcast.presentation.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresItem
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresResponse
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.SimpleAndroidViewModel
import com.pavelprymak.propodcast.presentation.common.StatesBatch
import com.pavelprymak.propodcast.presentation.common.bindToStatesBatch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class GenreViewModel(application: Application, private val mRepo: PodcastRepoRx) : SimpleAndroidViewModel(application) {
    private val mGenresDataBatch = StatesBatch<List<GenresItem>?>()
    private val mContext: Context = application.applicationContext
    private val mGenreAllItem = createAllGenreItem()

    fun getGenresBatch(): StatesBatch<List<GenresItem>?> {
        if (mGenresDataBatch.data.value == null) {
            mGenresDataBatch.postLoading(true)
            mRepo.getGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { genresResponse: GenresResponse -> prepareListGenres(genresResponse) }
                .bindToStatesBatch(mGenresDataBatch)
                .untilDestroy()
        }
        return mGenresDataBatch
    }

    private fun prepareListGenres(genresResponse: GenresResponse): List<GenresItem> {
        val newGenres = ArrayList<GenresItem>()
        genresResponse.genres?.let {
            newGenres.add(mGenreAllItem)
            newGenres.addAll(it)
        }
        return newGenres
    }

    fun removeObservers(lifecycleOwner: LifecycleOwner) {
        mGenresDataBatch.data.removeObservers(lifecycleOwner)
        mGenresDataBatch.error.removeObservers(lifecycleOwner)
        mGenresDataBatch.loading.removeObservers(lifecycleOwner)
    }

    private fun createAllGenreItem(): GenresItem {
        val genreAllItem = GenresItem()
        genreAllItem.id = 0
        genreAllItem.name = mContext.getString(R.string.genres_all_title)
        return genreAllItem
    }
}
