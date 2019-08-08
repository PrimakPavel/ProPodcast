package com.pavelprymak.propodcast.presentation.common

import androidx.lifecycle.MutableLiveData
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class StatesBatch<T> {
    val data = MutableLiveData<T>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()

    fun postData(data: T) {
        postError(null)
        postLoading(false)
        this.data.postValue(data)
    }

    fun postError(error: Throwable?) {
        postLoading(false)
        this.error.postValue(error)
    }

    fun postLoading(loading: Boolean) {
        this.loading.postValue(loading)
    }
}

fun <T> Single<T>.bindToStatesBatch(
    statesBatch: StatesBatch<T>,
    postScheduler: Scheduler = AndroidSchedulers.mainThread()
): Disposable {
    return this.observeOn(postScheduler)
        .doOnSubscribe { statesBatch.postLoading(true) }
        .subscribe({ statesBatch.postData(it) }, { statesBatch.postError(it) })
}

fun <T> Maybe<T>.bindToStatesBatch(
    statesBatch: StatesBatch<T>,
    postScheduler: Scheduler = AndroidSchedulers.mainThread()
): Disposable {
    return this.observeOn(postScheduler)
        .doOnSubscribe { statesBatch.postLoading(true) }
        .subscribe({ statesBatch.postData(it) }, { statesBatch.postError(it) })
}

fun <T> Observable<T>.bindToStatesBatch(
    statesBatch: StatesBatch<T>,
    postScheduler: Scheduler = AndroidSchedulers.mainThread()
): Disposable {
    return this.observeOn(postScheduler)
        .doOnSubscribe { statesBatch.postLoading(true) }
        .subscribe({ statesBatch.postData(it) }, { statesBatch.postError(it) })
}

fun Completable.bindToStatesBatch(
    statesBatch: StatesBatch<Unit>,
    postScheduler: Scheduler = AndroidSchedulers.mainThread()
): Disposable {
    return this.observeOn(postScheduler)
        .doOnSubscribe { statesBatch.postLoading(true) }
        .subscribe({ statesBatch.postData(Unit) }, { statesBatch.postError(it) })
}
