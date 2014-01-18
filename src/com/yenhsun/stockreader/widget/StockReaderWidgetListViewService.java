
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

    private static final boolean DEBUG = true;
    private static final String TAG = "QQQQ";

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return super.onBind(arg0);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetFactory(getApplicationContext(), intent);
    }

    public static class MyWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ArrayList<StockData> mStockData;

        public MyWidgetFactory(Context context, Intent intent) {
            mContext = context;
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
                views.setTextColor(R.id.widget_list_stock_price, Color.RED);
                views.setTextColor(R.id.widget_list_stock_state, Color.RED);
            } else if (c < 0) {
                views.setTextColor(R.id.widget_list_stock_price, Color.GREEN);
                views.setTextColor(R.id.widget_list_stock_state, Color.GREEN);
            } else {
                views.setTextColor(R.id.widget_list_stock_price, Color.WHITE);
                views.setTextColor(R.id.widget_list_stock_state, Color.WHITE);
            }

            return views;
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
