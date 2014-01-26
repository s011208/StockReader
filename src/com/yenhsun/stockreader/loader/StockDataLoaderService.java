
package com.yenhsun.stockreader.loader;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.yenhsun.stockreader.StockReaderApplicaion;
import com.yenhsun.stockreader.storage.MainAppSettingsPreference;
import com.yenhsun.stockreader.storage.StockDataPreference;
import com.yenhsun.stockreader.util.StockData;
import com.yenhsun.stockreader.util.StockId;
import com.yenhsun.stockreader.util.UrlStringComposer;
import com.yenhsun.stockreader.widget.StockReaderWidget;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class StockDataLoaderService extends Service implements StockLoaderCallback {
    private static final boolean DEBUG = true;

    private static final String TAG = "StockDataLoaderService";

    private static final ArrayList<StockData> mStockData = new ArrayList<StockData>();

    private static boolean sThreadStopSignal = false;

    private LoaderTask mLoader;

    private StockDataPreference mStockDataPreference;

    private MainAppSettingsPreference mMainAppSettingsPreference;

    private int mUpdatingTimePeriod;

    private BroadcastReceiver mReceiver;

    private boolean misForcedPauseUpdating = false;

    private ArrayList<StockId> mStockList;

    public void onCreate() {
        super.onCreate();
        mStockDataPreference = new StockDataPreference(this);
        mMainAppSettingsPreference = StockReaderApplicaion.getMainAppSettingsPreference(this);
        mUpdatingTimePeriod = mMainAppSettingsPreference.getUpdatingPeriodTime();
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                String action = arg1.getAction();
                if (MainAppSettingsPreference.INTENT_CHANGE_UPDATING_PERIOD_TIME.equals(action)) {
                    mUpdatingTimePeriod = arg1.getIntExtra(
                            MainAppSettingsPreference.INTENT_CHANGE_UPDATING_PERIOD_TIME,
                            mUpdatingTimePeriod);
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    if (mMainAppSettingsPreference.getEnableUpdatingWhenScreenOff() == false)
                        misForcedPauseUpdating = true;
                    else
                        misForcedPauseUpdating = false;
                } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    misForcedPauseUpdating = false;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainAppSettingsPreference.INTENT_CHANGE_UPDATING_PERIOD_TIME);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver, filter);
    }

    public static ArrayList<StockData> retriveStockData() {
        synchronized (mStockData) {
            return mStockData;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG)
            Log.e(TAG, "StockDataLoaderService onStartCommand");
        sThreadStopSignal = false;
        mStockList = mStockDataPreference.retriveData();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true && !sThreadStopSignal) {
                    if (misForcedPauseUpdating == false) {
                        if (DEBUG)
                            Log.d(TAG, "Stock reader is going to query data!");
                        parse();
                    } else {
                        if (DEBUG)
                            Log.w(TAG, "skip update, misForcedPauseUpdating: "
                                    + misForcedPauseUpdating);
                    }
                    try {
                        Thread.sleep(mUpdatingTimePeriod);
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
        sThreadStopSignal = true;
        if (mLoader != null) {
            if (mLoader.isLoading()) {
                mLoader.forceCancel();
            }
            mLoader.setCallback(null);
        }
        unregisterReceiver(mReceiver);
    }

    private void parse() {
        if (mLoader != null) {
            // still loading
            return;
        }
        mLoader = new LoaderTask(mStockList, this);
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
            Log.w(TAG, "", e);
    }

    public static final void dumpStockData(ArrayList<StockData> data) {
        if (DEBUG)
            for (StockData s : data)
                Log.v(TAG, s.toString());
    }

    @Override
    public void setData(ArrayList<JSONObject> d) {
        synchronized (mStockData) {
            if (DEBUG) {
                Log.d(TAG, "get callback");
            }
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
            }
            mLoader = null;
            if (DEBUG) {
                Log.i(TAG, "trying to update widget");
            }
            StockReaderWidget.performUpdate(this);
        }
    }
}
