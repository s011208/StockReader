
package com.yenhsun.stockreader;

import com.yenhsun.stockreader.storage.MainAppSettingsPreference;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class StockReaderApplicaion extends Application {
    private static MainAppSettingsPreference sSettings;

    public void onCreate() {
        super.onCreate();
    }

    public static synchronized MainAppSettingsPreference getMainAppSettingsPreference(Context context) {
        if (sSettings == null) {
            sSettings = new MainAppSettingsPreference(context);
        }
        return sSettings;
    }
}
