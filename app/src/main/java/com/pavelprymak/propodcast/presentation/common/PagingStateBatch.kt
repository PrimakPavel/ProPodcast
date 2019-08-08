package com.pavelprymak.propodcast.presentation.common

import androidx.lifecycle.MutableLiveData

class PagingStateBatch {
    val isEmptyListData = MutableLiveData<Boolean>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()

    fun postIsEmptyList(data: Boolean) {
        this.isEmptyListData.postValue(data)
    }

    fun postError(error: Throwable?) {
        this.error.postValue(error)
    }

    fun postLoading(loading: Boolean) {
        this.loading.postValue(loading)
    }
}
