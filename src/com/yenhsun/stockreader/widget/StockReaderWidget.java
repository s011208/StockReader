
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
        setAlarmManagerIntent(context, StockReaderApplicaion.getMainAppSettingsPreference(context)
                .getUpdatingPeriodTime());
        performUpdate(context, appWidgetManager);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        cancelAlarmManagerIntent(context);
    }

    private static PendingIntent getAlarmManagerIntent(Context context) {
        final Intent i = new Intent(context, StockReaderWidgetUpdateService.class);
        PendingIntent updateService = PendingIntent.getService(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        return updateService;
    }

    public static void setAlarmManagerIntent(Context context) {
        setAlarmManagerIntent(context, 1000 * 10);
    }

    public static void setAlarmManagerIntent(Context context, int updateTime) {
        int widgetId[] = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, StockReaderWidget.class));
        if (widgetId != null && widgetId.length != 0) {
            final AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            final Calendar time = Calendar.getInstance();
            time.set(Calendar.MINUTE, 0);
            time.set(Calendar.SECOND, 0);
            time.set(Calendar.MILLISECOND, 0);
            PendingIntent updateService = getAlarmManagerIntent(context);
            alarmManager.cancel(updateService);
            alarmManager.setRepeating(AlarmManager.RTC, time.getTime().getTime(), updateTime,
                    updateService);
        }
    }

    public static void cancelAlarmManagerIntent(Context context) {
        final AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getAlarmManagerIntent(context));
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
