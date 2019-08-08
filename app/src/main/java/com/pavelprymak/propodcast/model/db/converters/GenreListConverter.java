package com.pavelprymak.propodcast.model.db.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GenreListConverter {
    private Gson mGson = new Gson();

    @TypeConverter
    public String fromGenresList(List<Integer> genres) {
        if (genres == null) {
            return null;
        }
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return mGson.toJson(genres, type);
    }

    @TypeConverter
    public List<Integer> toGenresList(String genresString) {
        if (genresString == null) {
            return null;
        }
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return mGson.fromJson(genresString, type);
    }
}
