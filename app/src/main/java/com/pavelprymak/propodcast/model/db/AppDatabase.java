package com.pavelprymak.propodcast.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pavelprymak.propodcast.model.db.converters.GenreListConverter;

import timber.log.Timber;

@Database(entities = {FavoritePodcastEntity.class}, version = 1, exportSchema = false)
@TypeConverters(value = {GenreListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "db_podcasts";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Timber.d("Getting the database instance");
        return sInstance;
    }

    public abstract FavoritePodcastDao favoritePodcastDao();
}