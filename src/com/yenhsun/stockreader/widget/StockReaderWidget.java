
package com.yenhsun.stockreader.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.yenhsun.stockreader.R;
import com.yenhsun.stockreader.StockReaderApplicaion;

public class StockReaderWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        performUpdate(context, appWidgetManager);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
    }

    public static void performUpdate(Context context) {
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        performUpdate(context, awm);
    }

    public static void performUpdate(Context context, AppWidgetManager awm) {
        int widgetId[] = awm.getAppWidgetIds(new ComponentName(context, StockReaderWidget.class));
        for (int appWidgetId : widgetId) {
            Intent intent = new Intent(context, StockReaderWidgetListViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews updateViews = new RemoteViews(context.getPackageName(),
                    R.layout.stock_reader_widget);
            updateViews.setRemoteAdapter(R.id.widget_stock_data, intent);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            String formatted = format1.format(cal.getTime());
            updateViews.setTextViewText(R.id.widget_update_time,
                    context.getResources().getString(R.string.last_update_time) + "   " + formatted);
            awm.updateAppWidget(appWidgetId, updateViews);
            awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_stock_data);
        }
    }
}
