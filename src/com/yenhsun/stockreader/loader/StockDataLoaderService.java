
package com.yenhsun.stockreader.loader;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.yenhsun.stockreader.MainActivity;
import com.yenhsun.stockreader.storage.StockDataPreference;
import com.yenhsun.stockreader.util.StockData;
import com.yenhsun.stockreader.util.UrlStringComposer;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class StockDataLoaderService extends Service implements StockLoaderCallback {
    private static final boolean DEBUG = true;

    private static final String TAG = "QQQQ";

    private static final ArrayList<StockData> mStockData = new ArrayList<StockData>();

    private LoaderTask mLoader;

    private String mParsingString;

    private StockDataPreference mStockDataPreference;

    public void onCreate() {
        super.onCreate();
        mStockDataPreference = new StockDataPreference(this);
    }

    public static ArrayList<StockData> retriveStockData() {
        synchronized (mStockData) {
            return mStockData;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mParsingString = UrlStringComposer.retriveGoogleUrl(mStockDataPreference.retriveData());
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    parse(mParsingString);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return Service.START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        // release loader
        if (mLoader != null) {
            if (mLoader.isLoading()) {
                mLoader.forceCancel();
            }
            mLoader.setCallback(null);
        }
    }

    private void parse(String url) {
        if (mLoader != null) {
            // still loading
            return;
        }
        mLoader = new LoaderTask();
        mLoader.setUrl(url);
        mLoader.setCallback(this);
        // mLoader.setUrl("http://finance.google.com/finance/info?client=ig&q=NASDAQ:GOOG,NASDAQ:YHOO");
        mLoader.start();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private static final void showErrorMsg(Exception e) {
        if (DEBUG)
            Log.e(TAG, "", e);
    }

    public static final void dumpStockData(ArrayList<StockData> data) {
        if (DEBUG)
            for (StockData s : data)
                Log.e(TAG, s.toString());
    }

    @Override
    public void setData(ArrayList<JSONObject> d) {
        synchronized (mStockData) {
            mStockData.clear();
            ArrayList<JSONObject> data = d;
            if (data.size() > 0) {
                for (JSONObject j : data) {
                    try {
                        StockData sd = new StockData(j);
                        mStockData.add(sd);
                    } catch (JSONException e) {
                        showErrorMsg(e);
                    }
                }
                // dumpStockData(mStockData);
                // sendBroadcast();
            }
            mLoader = null;
        }
    }
}
