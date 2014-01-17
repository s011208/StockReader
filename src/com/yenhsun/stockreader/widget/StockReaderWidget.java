
package com.yenhsun.stockreader.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.yenhsun.stockreader.R;

public class StockReaderWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("QQQQ", "StockReaderWidget onupdate, length of appWidgetIds: " + appWidgetIds.length);
        performUpdate(context, appWidgetManager, appWidgetIds, null);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void performUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds,
            long[] changedEvents) {
        for (int appWidgetId : appWidgetIds) {
            Log.e("QQQQ", "StockReaderWidget performUpdate");
            Intent intent = new Intent(context, StockReaderWidgetUpdateService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews updateViews = new RemoteViews(context.getPackageName(),
                    R.layout.stock_reader_widget);
            updateViews.setRemoteAdapter(R.id.widget_stock_data, intent);

            awm.updateAppWidget(appWidgetId, updateViews);
            awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_stock_data);
        }
    }
}
