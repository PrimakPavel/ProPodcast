package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.pavelprymak.propodcast.utils.ServerMocksUtil

import java.io.IOException

class LanguageViewModel : ViewModel() {
    private val mLanguagesData = MutableLiveData<List<String>>()

    val languages: LiveData<List<String>>
        get() {
            if (mLanguagesData.value == null) {
                try {
                    mLanguagesData.setValue(ServerMocksUtil.getLanguagesMock().languages)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return mLanguagesData
        }
}
