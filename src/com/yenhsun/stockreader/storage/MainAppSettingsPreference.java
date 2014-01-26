
package com.yenhsun.stockreader.storage;

import com.yenhsun.stockreader.loader.StockDataLoaderService;
import com.yenhsun.stockreader.widget.StockReaderWidget;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainAppSettingsPreference {
    private Context mContext;

    private SharedPreferences mPrefs;

    public static final String SHARED_PREFS_FILE = "main_app_setting_prefs";

    public static final String SERVICE_UPDATING_PERIOD_TIME = "service_updating_period_time";

    public static final String INTENT_CHANGE_UPDATING_PERIOD_TIME = "com.yenhsun.stockreader.storage.update.period.time";

    public static final String ENABLE_SERVICE_UPDATE = "enable_service_update";
    public static final String INTENT_ENABLE_SERVICE_UPDATE = "com.yenhsun.stockreader.storage.enable.service.update";

    public static final String ENABLE_UPDATE_WHEN_SCREEN_OFF = "enable_update_when_screen_off";
    public static final String ENABLE_UPDATE_WHEN_BOOT_UP = "enable_update_when_boot_up";

    public MainAppSettingsPreference(Context c) {
        mContext = c;
        mPrefs = mContext.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }

    public void setEnableUpdatingWhenBootUp(boolean enable) {
        mPrefs.edit().putBoolean(ENABLE_UPDATE_WHEN_BOOT_UP, enable).commit();
    }

    public boolean getEnableUpdatingWhenBootUp() {
        return mPrefs.getBoolean(ENABLE_UPDATE_WHEN_BOOT_UP, true);
    }

    public void setEnableUpdatingWhenScreenOff(boolean enable) {
        mPrefs.edit().putBoolean(ENABLE_UPDATE_WHEN_SCREEN_OFF, enable).commit();
    }

    public boolean getEnableUpdatingWhenScreenOff() {
        return mPrefs.getBoolean(ENABLE_UPDATE_WHEN_SCREEN_OFF, true);
    }

    public void setEnableUpdatingService(boolean enable) {
        mPrefs.edit().putBoolean(ENABLE_SERVICE_UPDATE, enable).commit();
        if (enable) {
            mContext.startService(new Intent(mContext, StockDataLoaderService.class));
        } else {
            mContext.stopService(new Intent(mContext, StockDataLoaderService.class));
        }
    }

    public boolean getEnableUpdatingService() {
        return mPrefs.getBoolean(ENABLE_SERVICE_UPDATE, true);
    }

    public void setUpdatingPeriodTime(int t) {
        if (t == getUpdatingPeriodTime())
            return;
        mPrefs.edit().putInt(SERVICE_UPDATING_PERIOD_TIME, t).commit();
        mContext.sendBroadcast(new Intent(INTENT_CHANGE_UPDATING_PERIOD_TIME).putExtra(
                INTENT_CHANGE_UPDATING_PERIOD_TIME, t)); // update loader
                                                         // service
    }

    /**
     * default is 10 seconds
     * 
     * @return xxxx ms
     */
    public int getUpdatingPeriodTime() {
        return mPrefs.getInt(SERVICE_UPDATING_PERIOD_TIME, 10000);
    }
}
