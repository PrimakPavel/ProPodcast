package com.pavelprymak.propodcast.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pavelprymak.propodcast.model.network.pojo.regions.RegionItem
import com.pavelprymak.propodcast.utils.ServerMocksUtil
import java.io.IOException
import java.util.*

class RegionViewModel : ViewModel() {
    private val mRegionData = MutableLiveData<List<RegionItem>>()

    val regions: LiveData<List<RegionItem>>
        get() {
            if (mRegionData.value == null) {
                try {
                    ServerMocksUtil.getRegionMock().regions?.let { mRegionData.setValue(convert(it)) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return mRegionData
        }

    private fun convert(regionsMap: Map<String, String>): List<RegionItem> {
        val regionItems = ArrayList<RegionItem>()
        for ((key, value) in regionsMap) {
            regionItems.add(RegionItem(value, key))
        }
        return regionItems
    }
}
