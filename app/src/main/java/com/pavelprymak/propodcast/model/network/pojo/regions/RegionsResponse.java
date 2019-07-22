package com.pavelprymak.propodcast.model.network.pojo.regions;


import com.squareup.moshi.Json;

import java.util.Map;


public class RegionsResponse {

    @Json(name = "regions")
    private Map<String, String> regions;

    public void setRegions(Map<String, String> regions) {
        this.regions = regions;
    }

    public Map<String, String> getRegions() {
        return regions;
    }

    @Override
    public String toString() {
        return
                "RegionsResponse{" +
                        "regions = '" + regions + '\'' +
                        "}";
    }
}