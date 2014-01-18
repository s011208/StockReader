
package com.yenhsun.stockreader.widget;

import java.util.Date;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

public class StockReaderWidgetUpdateService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildUpdate();
        return super.onStartCommand(intent, flags, startId);
    }

    private void buildUpdate() {
        AppWidgetManager awm = AppWidgetManager.getInstance(this);
        StockReaderWidget.performUpdate(this, awm);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
