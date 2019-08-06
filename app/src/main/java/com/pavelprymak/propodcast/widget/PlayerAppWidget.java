package com.pavelprymak.propodcast.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.services.PlayerService;
import com.pavelprymak.propodcast.utils.widget.LastTrackPreferenceManager;
import com.squareup.picasso.Picasso;

import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_COMMAND_PLAYER;


public class PlayerAppWidget extends AppWidgetProvider {

    public static final int PENDING_STOP_REQUEST_CODE = 12;
    public static final int PENDING_PLAY_PAUSE_REQUEST_CODE = 14;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int[] appWidgetIds) {
        LastTrackPreferenceManager lastTrackPreferenceManager = new LastTrackPreferenceManager(context);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.player_app_widget);
        //title
        String trackTitle = lastTrackPreferenceManager.getTrackTitle();
        if (trackTitle != null) {
            views.setTextViewText(R.id.tvTitle, trackTitle);
        }
        //logo
        String logoUrl = lastTrackPreferenceManager.getTrackLogo();
        if (logoUrl != null) {
            Picasso.get().load(logoUrl)
                    .into(views, R.id.ivLogo, appWidgetIds);
        }
        //Author
        String trackAuthor = lastTrackPreferenceManager.getTrackAuthor();
        if (trackAuthor != null) {
            views.setTextViewText(R.id.tvAuthor, trackAuthor);
        }
        //Stop service btn
        if (PlayerService.isStartService) {
            Intent serviceIntent = new Intent(context, PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_STOP_SERVICE);
            PendingIntent stopServicePendingIntent = PendingIntent.getService(context, PENDING_STOP_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                stopServicePendingIntent = PendingIntent.getForegroundService(context, PENDING_STOP_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            views.setOnClickPendingIntent(R.id.ivStopBtn, stopServicePendingIntent);
        }

        //Continue or Play or Pause btn
        //Continue last track service player
        if (lastTrackPreferenceManager.getTrackAudioUrl() != null) {
            if (!PlayerService.isStartService) {
                Intent serviceIntent = new Intent(context, PlayerService.class);
                serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_CONTINUE_LAST_TRACK);
                PendingIntent stopServicePendingIntent = PendingIntent.getService(context, PENDING_PLAY_PAUSE_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    stopServicePendingIntent = PendingIntent.getForegroundService(context, PENDING_PLAY_PAUSE_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                views.setImageViewBitmap(R.id.ivPlayPauseBtn, BitmapFactory.decodeResource(context.getResources(), R.drawable.baseline_restore_black_36));
                views.setOnClickPendingIntent(R.id.ivPlayPauseBtn, stopServicePendingIntent);
            } else {
                if (PlayerService.isTrackPlayNow) {
                    //Pause service player
                    Intent serviceIntent = new Intent(context, PlayerService.class);
                    serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_PAUSE);
                    PendingIntent stopServicePendingIntent = PendingIntent.getService(context, PENDING_PLAY_PAUSE_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        stopServicePendingIntent = PendingIntent.getForegroundService(context, PENDING_PLAY_PAUSE_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }
                    views.setImageViewBitmap(R.id.ivPlayPauseBtn, BitmapFactory.decodeResource(context.getResources(), R.drawable.baseline_pause_black_36));
                    views.setOnClickPendingIntent(R.id.ivPlayPauseBtn, stopServicePendingIntent);
                } else {
                    //Play service player
                    Intent serviceIntent = new Intent(context, PlayerService.class);
                    serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_PLAY);
                    PendingIntent stopServicePendingIntent = PendingIntent.getService(context, PENDING_PLAY_PAUSE_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        stopServicePendingIntent = PendingIntent.getForegroundService(context, PENDING_PLAY_PAUSE_REQUEST_CODE, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }
                    views.setImageViewBitmap(R.id.ivPlayPauseBtn, BitmapFactory.decodeResource(context.getResources(), R.drawable.baseline_play_arrow_black_36));
                    views.setOnClickPendingIntent(R.id.ivPlayPauseBtn, stopServicePendingIntent);
                }
            }
        } else {
            views.setTextViewText(R.id.tvTitle, context.getString(R.string.error_continue_last_track));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

