
package com.yenhsun.stockreader;

import com.yenhsun.stockreader.loader.StockDataLoaderService;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends FragmentActivity {
    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        startService(new Intent(MainActivity.this,
                StockDataLoaderService.class));
        initTabs();
    }

    private void initTabs() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("Observer list")
                .setIndicator(getBaseContext().getResources().getString(R.string.main_watch_list)),
                MainObserverList.class,
                null);
        mTabHost.addTab(mTabHost.newTabSpec("Settings")
                .setIndicator(getBaseContext().getResources().getString(R.string.action_settings)),
                MainSettings.class,
                null);
    }
}
