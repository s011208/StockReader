
package com.yenhsun.stockreader.loader;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.yenhsun.stockreader.util.StockData;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StockDataLoaderService extends Service implements StockLoaderCallback {
    private static final boolean DEBUG = true;
    private static final String TAG = "QQQQ";

    private final ArrayList<StockData> mStockData = new ArrayList<StockData>();
    private final LoaderTask mLoader = new LoaderTask();
    private String mParsingString;

    public void onCreate() {
        super.onCreate();
        mLoader.setCallback(this);
        compositeParsingString();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
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

    private void compositeParsingString(){
//        mParsingString
    }
    
    private void parse(String url) {
        mLoader.setUrl("http://finance.google.com/finance/info?client=ig&q=NASDAQ:GOOG,NASDAQ:YHOO");
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

    private static final void dumpStockData(ArrayList<StockData> data) {
        if (DEBUG)
            for (StockData s : data)
                Log.e(TAG, s.toString());
    }

    @Override
    public void setData(ArrayList<JSONObject> d) {
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
            dumpStockData(mStockData);
        }
    }
}
