package com.pavelprymak.propodcast.model.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GenreListConverter {
    @TypeConverter
    public String fromGenresList(List<Integer> genres) {
        if (genres == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.toJson(genres, type);
    }

    @TypeConverter
    public List<Integer> toGenresList(String genresString) {
        if (genresString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.fromJson(genresString, type);
    }
}
