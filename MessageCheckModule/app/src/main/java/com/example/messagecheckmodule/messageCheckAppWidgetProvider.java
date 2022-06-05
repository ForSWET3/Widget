package com.example.messagecheckmodule;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class messageCheckAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v("test", "onUpdate()");

        for(int appWidgetID : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);//MainActivity로 연결
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);//flag 사용 안함
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_main);
            views.setOnClickPendingIntent(R.id.btn_ms_widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetID, views);
        }


    }

    public void onDeleted (Context context, int[] appWidgetIds)
    {

    }

    public void onDisabled (Context context)
    {

    }
}