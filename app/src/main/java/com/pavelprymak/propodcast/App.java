package com.pavelprymak.propodcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pavelprymak.propodcast.model.db.AppDatabase;
import com.pavelprymak.propodcast.model.db.repo.DbRepo;
import com.pavelprymak.propodcast.model.db.repo.DbRepoImpl;
import com.pavelprymak.propodcast.utils.AppExecutors;
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager;
import com.pavelprymak.propodcast.utils.widget.LastTrackPreferenceManager;
import com.squareup.otto.Bus;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class App extends MultiDexApplication {
    public static final String CHANNEL_ID = "ProPodcastChannel";
    private static final String CHANNEL_NAME = "ProPodcast App Channel";
    public static AppExecutors appExecutors;
    public static FirebaseAnalytics mFirebaseAnalytics;
    public static DbRepo dbRepo;
    public static final Bus eventBus = new Bus();
    public static SettingsPreferenceManager mSettings;
    public static LastTrackPreferenceManager mLastTrackSettings;
    private static String fcmPushToken;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        //MultiDex
        MultiDex.install(this);
        createPlayerNotificationChannel();
        createFcmNotificationChannel();
        Stetho.initializeWithDefaults(this);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
            //Crashlytics.getInstance().crash(); // Force a crash first test
        }
        //Analytics
        // Obtain the FirebaseAnalytics instance.
        if (!FirebaseApp.getApps(this).isEmpty()) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            //FCM push token init
            initFCMPushToken();
        }
        appExecutors = new AppExecutors();
        dbRepo = new DbRepoImpl(AppDatabase.getInstance(getApplicationContext()), appExecutors.diskIO());
        mSettings = new SettingsPreferenceManager(getApplicationContext());
        mLastTrackSettings = new LastTrackPreferenceManager(getApplicationContext());
    }

    private void createPlayerNotificationChannel() {
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

    private void createFcmNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    getString(R.string.fcm_notification_channel_id),
                    getString(R.string.fcm_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void initFCMPushToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    // Get new Instance ID token
                    if (task.isSuccessful() && task.getResult() != null) {
                        fcmPushToken = task.getResult().getToken();
                        Timber.d(fcmPushToken);
                    }
                });
    }

}
