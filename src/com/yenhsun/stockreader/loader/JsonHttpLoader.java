
package com.yenhsun.stockreader.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonHttpLoader {
    // "http://finance.google.com/finance/info?client=ig&q=NASDAQ:GOOG,NASDAQ:YHOO"
    private static final boolean DEBUG = true;
    private static final String TAG = "QQQQ";

    public JsonHttpLoader() {
    }

    private static String readHttpData(String url) {
        String rtn = null;
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            rtn = sb.substring(3).toString();
        } catch (MalformedURLException e) {
            showErrorMsg(e);
        } catch (IOException e) {
            showErrorMsg(e);
        }
        return rtn;
    }

    public ArrayList<JSONObject> parse(String url) {
        ArrayList<JSONObject> rtn = new ArrayList<JSONObject>();
        String rawData = readHttpData(url);
        if (rawData == null)
            return rtn;
        try {
            JSONArray jArray = new JSONArray(rawData);
            for (int i = 0; i < jArray.length(); i++) {
                rtn.add(jArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            showErrorMsg(e);
        }
        return rtn;
    }

    private static void showErrorMsg(Exception e) {
        if (DEBUG)
            Log.e(TAG, "", e);
    }
}
