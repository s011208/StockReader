package com.yenhsun.stockreader.loader;

import java.util.ArrayList;

import org.json.JSONObject;

import com.yenhsun.stockreader.util.StockData;

public interface StockLoaderCallback {
    public void setData(ArrayList<JSONObject> d);
}
