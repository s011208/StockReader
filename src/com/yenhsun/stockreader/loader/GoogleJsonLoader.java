
package com.yenhsun.stockreader.loader;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yenhsun.stockreader.util.StockId;
import com.yenhsun.stockreader.util.UrlStringComposer;

public class GoogleJsonLoader extends JsonLoader {

    @Override
    public ArrayList<JSONObject> parse(ArrayList<StockId> data) {
        ArrayList<JSONObject> rtn = new ArrayList<JSONObject>();
        String url = UrlStringComposer.retriveGoogleUrl(data);
        if (url == null)
            return rtn;
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

    public static boolean isDataValid(ArrayList<StockId> data) {
        ArrayList<JSONObject> rtn = new ArrayList<JSONObject>();
        String url = UrlStringComposer.retriveGoogleUrl(data);
        if (url == null)
            return false;
        String rawData = readHttpData(url);
        if (rawData == null)
            return false;
        try {
            JSONArray jArray = new JSONArray(rawData);
            for (int i = 0; i < jArray.length(); i++) {
                rtn.add(jArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            showErrorMsg(e);
        }
        if (rtn.size() > 0)
            return true;
        return false;
    }
}
