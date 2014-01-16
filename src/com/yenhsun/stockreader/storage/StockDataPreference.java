
package com.yenhsun.stockreader.storage;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yenhsun.stockreader.util.StockId;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StockDataPreference {

    private Context mContext;

    private SharedPreferences mPrefs;

    public static final String SHARED_PREFS_FILE = "stock_data_prefs";

    private static final String STOCK_LIST = "stock_list";

    private BackupManager mBackupManager;

    public StockDataPreference(Context c) {
        mContext = c;
        mPrefs = mContext.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        mBackupManager = new BackupManager(mContext);
//        createFakeData();
    }

    private void createFakeData() {
        ArrayList<StockId> data = new ArrayList<StockId>();
        StockId s1 = new StockId("TPE", "2002");
        StockId s2 = new StockId("TPE", "6226");
        StockId s3 = new StockId("TPE", "2357");
        StockId s4 = new StockId("TPE", "2498");
        data.add(s1);
        data.add(s2);
        data.add(s3);
        data.add(s4);
        saveData(data);
    }

    public void removeData(StockId data) {
        ArrayList<StockId> rtn = retriveData();
        rtn.remove(data);
        saveData(rtn);
    }

    public void addData(StockId data) {
        ArrayList<StockId> rtn = retriveData();
        rtn.add(data);
        saveData(rtn);
    }

    public void saveData(ArrayList<StockId> data) {
        synchronized (this) {
            Editor editor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(data);
            editor.putString(STOCK_LIST, json);
            editor.commit();
            mBackupManager.dataChanged();
        }
    }

    /**
     * TODO Test case is needed
     */
    public ArrayList<StockId> retriveData() {
        synchronized (this) {
            ArrayList<StockId> rtn = null;
            Gson gson = new Gson();
            String json = mPrefs.getString(STOCK_LIST, "");
            rtn = (ArrayList<StockId>)gson.fromJson(json, new TypeToken<ArrayList<StockId>>() {
            }.getType());
            return rtn;
        }
    }
}
