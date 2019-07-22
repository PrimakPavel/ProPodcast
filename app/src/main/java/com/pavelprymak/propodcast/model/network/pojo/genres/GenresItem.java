package com.pavelprymak.propodcast.model.network.pojo.genres;


import androidx.annotation.NonNull;

import com.squareup.moshi.Json;


public class GenresItem {

    @Json(name = "parent_id")
    private Integer parentId;

    @Json(name = "name")
    private String name;

    @Json(name = "id")
    private int id;

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "GenresItem{" +
                        "parent_id = '" + parentId + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}