package com.pavelprymak.propodcast.presentation.common;

import androidx.lifecycle.MutableLiveData;

public class PagingStateBatch {
    private MutableLiveData<Boolean> mIsEmptyListData = new MutableLiveData<>();
    private MutableLiveData<Throwable> mError = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLoading = new MutableLiveData<>();

    public void postIsEmptyList(boolean data) {
        mIsEmptyListData.postValue(data);
    }

    public void postError(Throwable error) {
        mError.postValue(error);
    }

    public void postLoading(boolean loading) {
        mLoading.postValue(loading);
    }

    public MutableLiveData<Boolean> getIsEmptyListData() {
        return mIsEmptyListData;
    }

    public MutableLiveData<Throwable> getError() {
        return mError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }
}
