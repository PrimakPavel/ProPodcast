package com.pavelprymak.propodcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.pavelprymak.propodcast.model.data
import com.pavelprymak.propodcast.presentation.presentation
import com.squareup.otto.Bus
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(data, presentation))
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        //MultiDex
        MultiDex.install(this)
        createPlayerNotificationChannel()
        createFcmNotificationChannel()
        Stetho.initializeWithDefaults(this)
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
            //Crashlytics.getInstance().crash(); // Force a crash first test
        }
        //Analytics
        // Obtain the FirebaseAnalytics instance.
        if (!FirebaseApp.getApps(this).isEmpty()) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
            //FCM push token init
            initFCMPushToken()
        }
    }

    private fun createPlayerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                getString(R.string.player_notification_channel_id),
                getString(R.string.player_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createFcmNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                getString(R.string.fcm_notification_channel_id),
                getString(R.string.fcm_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun initFCMPushToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                // Get new Instance ID token
                if (task.isSuccessful && task.result != null) {
                    fcmPushToken = task.result!!.token
                    Timber.d(fcmPushToken)
                }
            }
    }

    companion object {
        var mFirebaseAnalytics: FirebaseAnalytics? = null
        val eventBus = Bus()
        private var fcmPushToken: String? = null
    }

}
