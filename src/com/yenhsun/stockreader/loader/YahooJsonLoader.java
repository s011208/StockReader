
package com.yenhsun.stockreader.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yenhsun.stockreader.util.StockData;
import com.yenhsun.stockreader.util.StockId;
import com.yenhsun.stockreader.util.UrlStringComposer;

public class YahooJsonLoader extends JsonLoader {

    static String getDis(String s1, String s2) {
        String rtn = null;
        Double dis = (Double.parseDouble(s1) - Double
                .parseDouble(s2));
        dis = new BigDecimal(dis)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        rtn = String.valueOf(dis);
        return rtn;
    }

    @Override
    public ArrayList<JSONObject> parse(ArrayList<StockId> data) {
        ArrayList<JSONObject> rtn = new ArrayList<JSONObject>();
        for (StockId d : data) {
            ArrayList<String> rawData = readHttpData(UrlStringComposer.retriveYahooUrl(d),
                    d.getId());
            if (rawData != null) {
                JSONObject json = new JSONObject();
                try {
                    json.put(StockData.id, d.getId());
                    json.put(StockData.t, d.getId());
                    json.put(StockData.e, "TPE");
                    json.put(StockData.l, rawData.get(1));
                    json.put(StockData.l_fix, rawData.get(1));
                    json.put(StockData.l_cur, rawData.get(1));
                    json.put(StockData.s, 0);
                    json.put(StockData.ltt, 0);
                    json.put(StockData.lt, 0);
                    json.put(StockData.c, getDis(rawData.get(1), rawData.get(6)));
                    json.put(StockData.c_fix, 0);
                    rtn.add(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return rtn;
    }

    public static boolean isDataValid(ArrayList<StockId> data) {
        ArrayList<JSONObject> rtn = new ArrayList<JSONObject>();
        for (StockId d : data) {
            ArrayList<String> rawData = readHttpData(UrlStringComposer.retriveYahooUrl(d),
                    d.getId());
            if (rawData != null) {
                JSONObject json = new JSONObject();
                try {
                    json.put(StockData.id, d.getId());
                    json.put(StockData.t, d.getId());
                    json.put(StockData.e, "TPE");
                    json.put(StockData.l, rawData.get(1));
                    json.put(StockData.l_fix, rawData.get(1));
                    json.put(StockData.l_cur, rawData.get(1));
                    json.put(StockData.s, 0);
                    json.put(StockData.ltt, 0);
                    json.put(StockData.lt, 0);
                    json.put(StockData.c, getDis(rawData.get(1), rawData.get(6)));
                    json.put(StockData.c_fix, 0);
                    rtn.add(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (rtn.size() > 0)
            return true;
        return false;
    }
}
