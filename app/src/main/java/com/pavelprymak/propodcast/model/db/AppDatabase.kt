package com.pavelprymak.propodcast.model.db

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.pavelprymak.propodcast.model.db.converters.GenreListConverter

import timber.log.Timber

@Database(entities = [FavoritePodcastEntity::class, FavoriteEpisodeEntity::class], version = 1, exportSchema = false)
@TypeConverters(value = [GenreListConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritePodcastDao(): FavoritePodcastDao
    abstract fun favoriteEpisodeDao(): FavoriteEpisodeDao

    companion object {
        private val LOCK = Any()
        private val DATABASE_NAME = "db_podcasts"
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Timber.d("Creating new database instance")
                    sInstance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, DATABASE_NAME
                    ).build()
                }
            }
            Timber.d("Getting the database instance")
            return sInstance!!
        }
    }
}