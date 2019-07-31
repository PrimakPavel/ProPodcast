package com.pavelprymak.propodcast.presentation.common;

import androidx.lifecycle.MutableLiveData;

public class StatesBatch<T> {
    private MutableLiveData<T> mData = new MutableLiveData<>();
    private MutableLiveData<Throwable> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLoading = new MutableLiveData<>();

    public void postData(T data) {
        postError(null);
        postLoading(false);
        mData.postValue(data);
    }

    public void postError(Throwable error) {
        postLoading(false);
        mError.postValue(error);
    }

    public void postLoading(boolean loading) {
        mLoading.postValue(loading);
    }

    public MutableLiveData<T> getData() {
        return mData;
    }

    public MutableLiveData<Throwable> getError() {
        return mError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }
}
