
package com.yenhsun.stockreader.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yenhsun.stockreader.util.StockId;

import android.util.Log;

public abstract class JsonLoader {
    // "http://finance.google.com/finance/info?client=ig&q=NASDAQ:GOOG,NASDAQ:YHOO"
    // "http://tw.stock.yahoo.com/q/q?s=6298"
    static final boolean DEBUG = false;
    static final String TAG = "JsonHttpLoader";

    public JsonLoader() {
    }

    /**
     * @param url
     * @return String data from url
     */
    static String readHttpData(String url) {
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

    static ArrayList<String> readHttpData(String url, String id) {
        ArrayList<String> rtn = new ArrayList<String>();
        try {
            InputStream is = new URL(url)
                    .openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName("UTF-8")));
            String s;

            int j = 0;
            boolean start = false;
            do {
                s = rd.readLine();
                if (s != null) {
                    if (start) {
                        rtn.add(s.replaceAll("<[^>]*>", "").trim());
                        j++;
                        if (j >= 10) {
                            start = false;
                            break;
                        }
                    }
                    if (s.contains("/q/bc?s=" + id)) {
                        start = true;
                    }
                } else {
                    break;
                }
            } while (true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public abstract ArrayList<JSONObject> parse(ArrayList<StockId> data);

    static void showErrorMsg(Exception e) {
        if (DEBUG)
            Log.w(TAG, "", e);
    }
}
