
package com.yenhsun.stockreader.loader;

import java.util.ArrayList;

import org.json.JSONObject;

import com.yenhsun.stockreader.util.StockId;

import android.util.Log;

public class LoaderTask extends Thread {

    private static final boolean DEBUG = false;
    private static final String TAG = "LoaderTask";
    private static final int MAX_RECONNECTION_COUNT = 60;

    private static final int MAX_INTERVAL = 30000;
    private int mReconnectionCount = 10;

    private int mInterval = 2000;

    private boolean mIsLoading = false;

    private final JsonLoader mHttpLoader = new YahooJsonLoader();

    private StockLoaderCallback mCallback;

    private boolean mCancel = false;

    private ArrayList<StockId> mData;

    public LoaderTask(ArrayList<StockId> data, StockLoaderCallback c) {
        super();
        mCallback = c;
        mData = data;
    }

    public void forceCancel() {
        mCancel = true;
    }

    public void setInterval(int t) {
        if (t > MAX_INTERVAL)
            t = MAX_INTERVAL;
        else if (t < 0)
            t = 0;
        mInterval = t;
    }

    public void setReconnectionCount(int t) {
        if (t > MAX_RECONNECTION_COUNT)
            t = MAX_RECONNECTION_COUNT;
        else if (t < 0)
            t = 0;
        mReconnectionCount = t;
    }

    public void setCallback(StockLoaderCallback c) {
        mCallback = c;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void run() {
        mIsLoading = mCancel = false;
        dumpStates();
        mIsLoading = true;
        for (int i = 0; i < mReconnectionCount; i++) {
            if (mCancel)
                return;
            ArrayList<JSONObject> data = mHttpLoader
                    .parse(mData);
            if (data == null) {
                try {
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    Log.w(TAG, "", e);
                }
            } else {
                if (DEBUG)
                    Log.d(TAG, "send callback: " + (mCallback != null));
                if (mCallback != null) {
                    mCallback.setData(data);
                }
                break;
            }
        }
        mIsLoading = false;
    }

    private void dumpStates() {
        if (DEBUG)
            Log.i(TAG, "mReconnectionCount: " + mReconnectionCount
                    + ", mInterval: " + mInterval);
    }
}
