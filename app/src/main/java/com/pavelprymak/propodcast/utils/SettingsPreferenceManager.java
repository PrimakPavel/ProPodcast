package com.pavelprymak.propodcast.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsPreferenceManager {
    private static final String APP_PREFERENCES = "settingsPodcastApp";
    private static final String APP_PREFERENCES_GENRE = "settingsGenre";
    private static final String APP_PREFERENCES_REGION = "settingsRegion";
    private static final String APP_PREFERENCES_LANGUAGE = "settingsLanguage";

    public static final int ALL_GENRE = 0;
    public static final String ALL_REGIONS = "all";
    public static final String ALL_LANGUAGES = "Any language";

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
        if (mSettings == null) return ALL_GENRE;
        return mSettings.getInt(APP_PREFERENCES_GENRE, ALL_GENRE);
    }

    public void saveFilterRegion(String region) {
        if (mSettings != null) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_REGION, region);
            editor.apply();
        }
    }

    public String getFilterRegion() {
        if (mSettings == null) return ALL_REGIONS;
        return mSettings.getString(APP_PREFERENCES_REGION, ALL_REGIONS);
    }


    public void saveFilterLanguage(String language) {
        if (mSettings != null) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_LANGUAGE, language);
            editor.apply();
        }
    }

    public String getFilterLanguage() {
        if (mSettings == null) return ALL_LANGUAGES;
        return mSettings.getString(APP_PREFERENCES_LANGUAGE, ALL_LANGUAGES);
    }
}
