package com.pavelprymak.propodcast.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import com.pavelprymak.propodcast.MainActivity
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.services.*
import com.pavelprymak.propodcast.services.PlayerService
import com.pavelprymak.propodcast.utils.widget.LastTrackPreferenceManager
import com.squareup.picasso.Picasso

private const val PENDING_MAIN_ACTIVITY_REQUEST_CODE = 10
private const val PENDING_STOP_REQUEST_CODE = 12
private const val PENDING_PLAY_PAUSE_REQUEST_CODE = 14

class PlayerAppWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, appWidgetIds: IntArray
    ) {
        val lastTrackPreferenceManager = LastTrackPreferenceManager(context)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.player_app_widget)
        //title
        lastTrackPreferenceManager.trackTitle?.let { title ->
            views.setTextViewText(R.id.tvTitle, title)
        }
        //logo
        lastTrackPreferenceManager.trackLogo?.let { logoUrl ->
            Picasso.get().load(logoUrl)
                .into(views, R.id.ivLogo, appWidgetIds)
            val mainActivityIntent = Intent(context, MainActivity::class.java)
            val mainActivityPendingIntent = PendingIntent.getActivity(
                context,
                PENDING_MAIN_ACTIVITY_REQUEST_CODE,
                mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.ivLogo, mainActivityPendingIntent)
        }
        //Author
        lastTrackPreferenceManager.trackAuthor?.let { author ->
            views.setTextViewText(R.id.tvAuthor, author)
        }
        if (lastTrackPreferenceManager.trackAudioUrl != null) {
            //Stop service btn prepare
            stopBtnPrepare(context, views)
            //Restore or Play or Pause btn prepare
            if (!PlayerService.isStartService) {
                //Restore service player
                configPlayPauseRestoreBtn(
                    context, views, COMMAND_CONTINUE_LAST_TRACK,
                    R.drawable.baseline_restore_black_36, R.string.content_description_restore
                )
            } else {
                if (PlayerService.isTrackPlayNow) {
                    //Pause service player
                    configPlayPauseRestoreBtn(
                        context, views, COMMAND_PAUSE,
                        R.drawable.baseline_pause_black_36, R.string.content_description_pause
                    )
                } else {
                    //Play service player
                    configPlayPauseRestoreBtn(
                        context, views, COMMAND_PLAY,
                        R.drawable.baseline_play_arrow_black_36, R.string.content_description_play
                    )
                }
            }
        } else {
            views.setTextViewText(R.id.tvTitle, context.getString(R.string.error_continue_last_track))
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun configPlayPauseRestoreBtn(
        context: Context, views: RemoteViews, playerCommandType: String, iconRes: Int, contentDescriptionRes: Int
    ) {
        val serviceIntent = Intent(context, PlayerService::class.java)
        serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, playerCommandType)
        var commandServicePendingIntent = PendingIntent.getService(
            context,
            PENDING_PLAY_PAUSE_REQUEST_CODE,
            serviceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            commandServicePendingIntent = PendingIntent.getForegroundService(
                context,
                PENDING_PLAY_PAUSE_REQUEST_CODE,
                serviceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        views.setImageViewBitmap(
            R.id.ivPlayPauseBtn,
            BitmapFactory.decodeResource(context.resources, iconRes)
        )
        views.setContentDescription(
            R.id.ivPlayPauseBtn,
            context.getString(contentDescriptionRes)
        )
        views.setOnClickPendingIntent(R.id.ivPlayPauseBtn, commandServicePendingIntent)
    }

    private fun stopBtnPrepare(context: Context, views: RemoteViews) {
        if (PlayerService.isStartService) {
            val serviceIntent = Intent(context, PlayerService::class.java)
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, COMMAND_STOP_SERVICE)
            var stopServicePendingIntent = PendingIntent.getService(
                context,
                PENDING_STOP_REQUEST_CODE,
                serviceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopServicePendingIntent = PendingIntent.getForegroundService(
                    context,
                    PENDING_STOP_REQUEST_CODE,
                    serviceIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            views.setOnClickPendingIntent(R.id.ivStopBtn, stopServicePendingIntent)
        }
    }
}

