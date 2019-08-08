package com.pavelprymak.propodcast.presentation.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.network.pojo.genres.GenresItem
import com.pavelprymak.propodcast.model.network.repo.PodcastRepoRx
import com.pavelprymak.propodcast.presentation.common.SimpleAndroidViewModel
import com.pavelprymak.propodcast.presentation.common.StatesBatch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class GenreViewModel(application: Application, private val mRepo: PodcastRepoRx) : SimpleAndroidViewModel(application) {
    private val mGenresDataBatch = StatesBatch<List<GenresItem>>()
    private val mContext: Context = application.applicationContext
    private val mGenreAllItem = createAllGenreItem()

    val genresBatch: StatesBatch<List<GenresItem>>
        get() {
            if (mGenresDataBatch.data.value == null) {
                mGenresDataBatch.postLoading(true)
                mRepo.getGenres()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ genres ->
                        genres?.genres?.let {
                            val newGenres = ArrayList<GenresItem>()
                            newGenres.add(mGenreAllItem)
                            newGenres.addAll(it)
                            mGenresDataBatch.postData(newGenres)
                        }

                    }, { error -> mGenresDataBatch.postError(error) })
                    .untilDestroy()
            }
            return mGenresDataBatch
        }

    fun removeObservers(lifecycleOwner: LifecycleOwner) {
        mGenresDataBatch.data.removeObservers(lifecycleOwner)
        mGenresDataBatch.error.removeObservers(lifecycleOwner)
        mGenresDataBatch.loading.removeObservers(lifecycleOwner)

    }

    private fun createAllGenreItem(): GenresItem {
        val genreAll = GenresItem()
        genreAll.id = 0
        genreAll.name = mContext.getString(R.string.genres_all_title)
        return genreAll
    }
}
