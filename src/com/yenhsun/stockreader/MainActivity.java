
package com.yenhsun.stockreader;

import java.util.ArrayList;

import com.yenhsun.stockreader.loader.StockDataLoaderService;
import com.yenhsun.stockreader.storage.StockDataPreference;
import com.yenhsun.stockreader.util.StockId;
import com.yenhsun.stockreader.util.UrlStringComposer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

    private Button mBtnAdd, mBtnEdit, mBtnOk, mBtnCancel;

    private ListView mStockList;

    private StockDataPreference mStockDataPreference;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initComponents();
        startService(new Intent(MainActivity.this,
                StockDataLoaderService.class));
    }

    private void init() {
        mContext = this;
        mStockDataPreference = new StockDataPreference(this);
    }

    private void initComponents() {
        mBtnAdd = (Button) findViewById(R.id.btn_add_new);
        mBtnEdit = (Button) findViewById(R.id.btn_edit);
        mBtnOk = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel = (Button) findViewById(R.id.btn_ok);
        initStockList();

    }

    private void initStockList() {
        mStockList = (ListView) findViewById(R.id.stock_data_list);
        ArrayList<StockId> data = mStockDataPreference.retriveData();
        if (data == null)
            data = new ArrayList<StockId>();
        StockListAdapter adapter = new StockListAdapter(mContext, data);
        mStockList.setAdapter(adapter);
    }
}
