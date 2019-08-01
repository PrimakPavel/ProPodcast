package com.pavelprymak.propodcast.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pavelprymak.propodcast.model.network.pojo.regions.RegionItem;
import com.pavelprymak.propodcast.utils.ServerMocksUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionViewModel extends ViewModel {
    private MutableLiveData<List<RegionItem>> mRegionData = new MutableLiveData<>();

    public LiveData<List<RegionItem>> getRegions() {
        if (mRegionData.getValue() == null) {
            try {
                mRegionData.setValue(convert(ServerMocksUtil.getRegionMock().getRegions()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mRegionData;
    }

    private List<RegionItem> convert(Map<String, String> regionsMap) {
        ArrayList<RegionItem> regionItems = new ArrayList<>();
        for (Map.Entry<String, String> entry : regionsMap.entrySet()) {
            regionItems.add(new RegionItem(entry.getValue(), entry.getKey()));
        }
        return regionItems;
    }
}
