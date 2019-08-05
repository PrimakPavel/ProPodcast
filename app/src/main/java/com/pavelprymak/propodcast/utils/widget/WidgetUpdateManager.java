package com.pavelprymak.propodcast.utils.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.pavelprymak.propodcast.widget.PlayerAppWidget;


public class WidgetUpdateManager {
    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, PlayerAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PlayerAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}
