package com.pavelprymak.propodcast.utils.widget;

import android.content.Context;
import android.content.SharedPreferences;

public class LastTrackPreferenceManager {
    private static final String APP_PREFERENCES = "settingsLastTrack";
    private static final String APP_PREFERENCES_TRACK_AUDIO_URL = "trackAudioUrl";
    private static final String APP_PREFERENCES_TRACK_TITLE = "trackTitle";
    private static final String APP_PREFERENCES_TRACK_AUTHOR = "trackAuthor";
    private static final String APP_PREFERENCES_TRACK_LOGO = "trackLogo";
    private static final String APP_PREFERENCES_TRACK_CURRENT_POSITION = "trackResumePosition";

    private SharedPreferences mSettings;

    public LastTrackPreferenceManager(Context context) {
        if (context != null) {
            mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
    }

    public void saveTrackInfo(String trackAudioUrl, String trackTitle, String trackAuthor, String logoUrl) {
        if (mSettings != null) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_TRACK_AUDIO_URL, trackAudioUrl);
            editor.putString(APP_PREFERENCES_TRACK_TITLE, trackTitle);
            editor.putString(APP_PREFERENCES_TRACK_AUTHOR, trackAuthor);
            editor.putString(APP_PREFERENCES_TRACK_LOGO, logoUrl);
            editor.apply();
        }
    }

    public String getTrackAudioUrl() {
        if (mSettings == null) return null;
        return mSettings.getString(APP_PREFERENCES_TRACK_AUDIO_URL, null);
    }

    public String getTrackTitle() {
        if (mSettings == null) return null;
        return mSettings.getString(APP_PREFERENCES_TRACK_TITLE, null);
    }

    public String getTrackAuthor() {
        if (mSettings == null) return null;
        return mSettings.getString(APP_PREFERENCES_TRACK_AUTHOR, null);
    }

    public String getTrackLogo() {
        if (mSettings == null) return null;
        return mSettings.getString(APP_PREFERENCES_TRACK_LOGO, null);
    }

    public void saveTrackCurrentPosition(long position) {
        if (mSettings != null) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putLong(APP_PREFERENCES_TRACK_CURRENT_POSITION, position);
            editor.apply();
        }
    }

    public long getTrackCurrentPosition() {
        if (mSettings == null) return 0L;
        return mSettings.getLong(APP_PREFERENCES_TRACK_CURRENT_POSITION, 0L);
    }

}
