package com.pavelprymak.propodcast.model.network.pojo.languages;

import com.squareup.moshi.Json;

import java.util.List;


public class LanguagesResponse {

    @Json(name = "languages")
    private List<String> languages;

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    @Override
    public String toString() {
        return
                "LanguagesResponse{" +
                        "languages = '" + languages + '\'' +
                        "}";
    }
}