package com.pavelprymak.propodcast.presentation.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

abstract class SimpleAndroidViewModel(application: Application) : AndroidViewModel(application) {
    private val disposable = CompositeDisposable()

    fun Disposable.untilDestroy() {
        disposable += this
    }

    override fun onCleared() {
        disposable.clear()
    }
}