package com.pavelprymak.propodcast.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsPreferenceManager {
    private static final String APP_PREFERENCES = "settingsPodcastApp";
    private static final String APP_PREFERENCES_GENRE = "settingsGenre";
    private static final String APP_PREFERENCES_REGION = "settingsRegion";

    public static final int INVALID_GENRE = -1;
    public static final String INVALID_REGION = "";

    private SharedPreferences mSettings;

    public SettingsPreferenceManager(Context context) {
        if (context != null) {
            mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
    }

    public void saveFilterGenre(int genreId) {
        if (mSettings != null) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_GENRE, genreId);
            editor.apply();
        }
    }

    public int getFilterGenre() {
        if (mSettings == null) return INVALID_GENRE;
        return mSettings.getInt(APP_PREFERENCES_GENRE, INVALID_GENRE);
    }

    public void saveFilterRegion(String region) {
        if (mSettings != null) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_REGION, region);
            editor.apply();
        }
    }

    public String getFilterRegion() {
        if (mSettings == null) return INVALID_REGION;
        return mSettings.getString(APP_PREFERENCES_REGION, INVALID_REGION);
    }
}
