
package com.yenhsun.stockreader.util;

import java.util.ArrayList;

import android.util.Log;

public class UrlStringComposer {
    private static final String TAG = "QQQQ";

    private static final boolean DEBUG = true;

    public static String retriveGoogleUrl(ArrayList<StockId> data) {
        StringBuilder sb = new StringBuilder("http://finance.google.com/finance/info?client=ig&q=");
        for (StockId d : data) {
            sb.append(d.getMarket() + ":" + d.getId() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        if (DEBUG)
            Log.e(TAG, sb.toString());
        return sb.toString();
    }
}
