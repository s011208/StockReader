
package com.yenhsun.stockreader.backuphelper;

import java.io.File;
import java.io.IOException;

import com.yenhsun.stockreader.storage.StockDataPreference;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class StockDataBackHelper extends BackupAgentHelper {
    private static final boolean DEBUG = true;

    private static final String TAG = "QQQQ";

    @Override
    public void onCreate() {
        if (DEBUG)
            Log.e(TAG, "StockDataBackHelper oncreate");
        String sharPath = null;
        File sharfDirectory = new File(getFilesDir(), "../shared_prefs");
        if (sharfDirectory != null && sharfDirectory.isDirectory()) {
            sharPath = sharfDirectory.getAbsolutePath() + "/"
                    + StockDataPreference.SHARED_PREFS_FILE;
            FileBackupHelper helper = new FileBackupHelper(this, sharPath);
            if (DEBUG)
                Log.e(TAG, "sharf path: " + sharPath);
            addHelper("stock_list_data", helper);
        }

    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
            ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper performs backup
        if (DEBUG)
            Log.e(TAG, "StockDataBackHelper backup");
        // synchronized (MainActivity.sDataLock) {
        super.onBackup(oldState, data, newState);
        // }
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState)
            throws IOException {
        if (DEBUG)
            Log.e(TAG, "StockDataBackHelper restore");
        // Hold the lock while the FileBackupHelper restores the file
        // synchronized (MainActivity.sDataLock) {
        super.onRestore(data, appVersionCode, newState);
        // }
    }
}
