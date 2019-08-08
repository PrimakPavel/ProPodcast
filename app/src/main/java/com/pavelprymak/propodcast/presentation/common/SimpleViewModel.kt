package com.pavelprymak.propodcast.presentation.common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

abstract class SimpleViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

    fun Disposable.untilDestroy() {
        disposable += this
    }

    override fun onCleared() {
        disposable.clear()
    }
}