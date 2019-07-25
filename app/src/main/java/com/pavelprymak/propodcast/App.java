package com.pavelprymak.propodcast;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.pavelprymak.propodcast.model.db.AppDatabase;
import com.pavelprymak.propodcast.model.db.repo.DbRepo;
import com.pavelprymak.propodcast.model.db.repo.DbRepoImpl;
import com.pavelprymak.propodcast.utils.AppExecutors;
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager;

import timber.log.Timber;

public class App extends Application {
    public static final String CHANNEL_ID = "ProPodcastChannel";
    public static final String CHANNEL_NAME = "ProPodcast App Channel";
    public static AppExecutors appExecutors;
    public static DbRepo dbRepo;
    public static SettingsPreferenceManager mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        createNotificationChannel();

        appExecutors = new AppExecutors();
        dbRepo = new DbRepoImpl(AppDatabase.getInstance(getApplicationContext()), appExecutors.diskIO());
        mSettings = new SettingsPreferenceManager(getApplicationContext());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
