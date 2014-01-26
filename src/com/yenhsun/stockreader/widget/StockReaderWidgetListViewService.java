
package com.yenhsun.stockreader.widget;

import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.yenhsun.stockreader.MainActivity;
import com.yenhsun.stockreader.R;
import com.yenhsun.stockreader.loader.StockDataLoaderService;
import com.yenhsun.stockreader.util.StockData;

public class StockReaderWidgetListViewService extends RemoteViewsService {

    private static final boolean DEBUG = false;
    private static final String TAG = "StockReaderWidgetListViewService";

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return super.onBind(arg0);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetFactory(getApplicationContext(), intent);
    }

    public static class MyWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ArrayList<StockData> mStockData;
        private int mBullColor;
        private int mBearColor;
        public MyWidgetFactory(Context context, Intent intent) {
            mContext = context;
            mBullColor = getBullTextColor();
            mBearColor = getBearTextColor();
        }

        @Override
        public int getCount() {
            return mStockData.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.stock_reader_widget_item);
            StockData sd = mStockData.get(position);
            float c = 0;
            try {
                c = Float.parseFloat(sd.getC());
            } catch (Exception e) {
            }
            views.setTextViewText(R.id.widget_list_stock_id, sd.getE() + "  :  " + sd.getT());
            views.setTextViewText(R.id.widget_list_stock_price, sd.getL());
            views.setTextViewText(R.id.widget_list_stock_state, sd.getC());
            if (c > 0) {
                views.setTextColor(R.id.widget_list_stock_price, mBullColor);
                views.setTextColor(R.id.widget_list_stock_state, mBullColor);
            } else if (c < 0) {
                views.setTextColor(R.id.widget_list_stock_price, mBearColor);
                views.setTextColor(R.id.widget_list_stock_state, mBearColor);
            } else {
                views.setTextColor(R.id.widget_list_stock_price, Color.WHITE);
                views.setTextColor(R.id.widget_list_stock_state, Color.WHITE);
            }

            return views;
        }

        private int getBullTextColor() {
            return "zh_TW".equalsIgnoreCase(mContext.getResources().getConfiguration().locale
                    .toString()) ? Color.RED : Color.GREEN;
        }

        private int getBearTextColor() {
            return "zh_TW".equalsIgnoreCase(mContext.getResources().getConfiguration().locale
                    .toString()) ? Color.GREEN : Color.RED;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            mStockData = StockDataLoaderService.retriveStockData();
        }

        @Override
        public void onDestroy() {
        }
    }
}
