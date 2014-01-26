
package com.yenhsun.stockreader;

import com.yenhsun.stockreader.loader.StockDataLoaderService;
import com.yenhsun.stockreader.storage.MainAppSettingsPreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null)
            return;
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            MainAppSettingsPreference setting = StockReaderApplicaion
                    .getMainAppSettingsPreference(context);
            if (setting.getEnableUpdatingWhenBootUp()) {
                context.startService(new Intent(context,
                        StockDataLoaderService.class));
            } else {
                // do nothing
            }
        }

    }

}
