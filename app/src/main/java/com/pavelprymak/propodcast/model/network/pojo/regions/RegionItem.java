package com.pavelprymak.propodcast.model.network.pojo.regions;

public class RegionItem {
    private String regionName;
    private String regionShortName;

    public RegionItem() {
    }

    public RegionItem(String regionName, String regionShortName) {
        this.regionName = regionName;
        this.regionShortName = regionShortName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionShortName() {
        return regionShortName;
    }

    public void setRegionShortName(String regionShortName) {
        this.regionShortName = regionShortName;
    }
}
