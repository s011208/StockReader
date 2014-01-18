
package com.yenhsun.stockreader.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class StockData implements Parcelable {
    public static final String id = "id";
    public static final String t = "t";
    public static final String e = "e";
    public static final String l = "l";
    public static final String l_fix = "l_fix";
    public static final String l_cur = "l_cur";
    public static final String s = "s";
    public static final String ltt = "ltt";
    public static final String lt = "lt";
    public static final String c = "c";
    public static final String c_fix = "c_fix";
    private HashMap<String, String> mData = new HashMap<String, String>();

    public StockData(Parcel source) {
        final int N = source.readInt();
        for (int i = 0; i < N; i++) {
            String key = source.readString();
            String value = source.readString();
            mData.put(key, value);
        }
    }

    public StockData(Map<String, String> data) {
        mData.putAll(data);
    }

    public StockData(JSONObject j) throws JSONException {
        mData.put(id, j.getString(id));
        mData.put(t, j.getString(t));
        mData.put(e, j.getString(e));
        mData.put(l, j.getString(l));
        mData.put(l_fix, j.getString(l_fix));
        mData.put(l_cur, j.getString(l_cur));
        mData.put(s, j.getString(s));
        mData.put(ltt, j.getString(ltt));
        mData.put(lt, j.getString(lt));
        mData.put(c, j.getString(c));
        mData.put(c_fix, j.getString(c_fix));
    }

    public StockData(String id1, String t1, String e1, String l1, String l_fix1, String l_cur1,
            String s1, String ltt1, String lt1, String c1, String c_fix1) {
        mData.put(id, id1);
        mData.put(t, t1);
        mData.put(e, e1);
        mData.put(l, l1);
        mData.put(l_fix, l_fix1);
        mData.put(l_cur, l_cur1);
        mData.put(s, s1);
        mData.put(ltt, ltt1);
        mData.put(lt, lt1);
        mData.put(c, c1);
        mData.put(c_fix, c_fix1);
    }

    public String getId() {
        return mData.get(id);
    }

    public String getT() {
        return mData.get(t);
    }

    public String getE() {
        return mData.get(e);
    }

    public String getL() {
        return mData.get(l);
    }

    public String getL_FIX() {
        return mData.get(l_fix);
    }

    public String getL_CUR() {
        return mData.get(l_cur);
    }

    public String getS() {
        return mData.get(s);
    }

    public String getLTT() {
        return mData.get(ltt);
    }

    public String getLT() {
        return mData.get(lt);
    }

    public String getC() {
        return mData.get(c);
    }

    public String getC_FIX() {
        return mData.get(c_fix);
    }

    public String toString() {
        return "id: " + getId() + ", t: " + getT() + ", e: " + getE() + ", l: " + getL()
                + ", getL_FIX: " + getL_FIX()
                + ", getL_CUR: " + getL_CUR() + ", s: " + getS() + ", ltt: " + getLTT() + ", lt: "
                + getLT() + ", c: " + getC()
                + ", c_fix: " + getC_FIX();
    }

    public static final Parcelable.Creator<StockData> CREATOR = new Creator<StockData>() {

        @Override
        public StockData createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new StockData(source);
        }

        @Override
        public StockData[] newArray(int size) {
            // TODO Auto-generated method stub
            return new StockData[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        final int N = mData.size();
        dest.writeInt(N);
        if (N > 0) {
            for (Map.Entry<String, String> entry : mData.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }
    }
}
