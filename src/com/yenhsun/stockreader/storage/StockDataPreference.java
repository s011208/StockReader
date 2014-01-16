
package com.yenhsun.stockreader.storage;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yenhsun.stockreader.util.StockId;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StockDataPreference {

    private Context mContext;

    private SharedPreferences mPrefs;

    private static final String SHARED_PREFS_FILE = "stock_data_prefs";

    private static final String STOCK_LIST = "stock_list";

    public StockDataPreference(Context c) {
        mContext = c;
        mPrefs = mContext.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
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
