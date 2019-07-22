package com.pavelprymak.propodcast.model.network.pojo.genres;

import com.squareup.moshi.Json;

import java.util.List;


public class GenresResponse {

    @Json(name = "genres")
    private List<GenresItem> genres;

    public void setGenres(List<GenresItem> genres) {
        this.genres = genres;
    }

    public List<GenresItem> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return
                "GenresResponse{" +
                        "genres = '" + genres + '\'' +
                        "}";
    }
}