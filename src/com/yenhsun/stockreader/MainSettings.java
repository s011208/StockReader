
package com.yenhsun.stockreader;

import com.yenhsun.stockreader.storage.MainAppSettingsPreference;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class MainSettings extends Fragment {
    private static final boolean DEBUG = true;
    private static final String TAG = "QQQQ";
    public static final String[] TIME_PERIOD_SPINNER_ADAPTER = new String[] {
            "10 sec", "20 sec", "30 sec", "1 min", "2 min", "3 min", "5 min"
    };
    private Activity mMainActivity;
    private Context mContext;
    private Spinner mUpdatePeriodSpinner;
    private MainAppSettingsPreference mMainAppSettingsPreference;
    private RadioGroup mEnableGroup, mEnableWhenScreenOff, mEnableWhenBootUp;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = activity;
        mContext = activity;
        mMainAppSettingsPreference = StockReaderApplicaion.getMainAppSettingsPreference(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        mUpdatePeriodSpinner = (Spinner) getView()
                .findViewById(R.id.settings_update_period_spinner);
        ArrayAdapter<String> marketAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, TIME_PERIOD_SPINNER_ADAPTER);
        mUpdatePeriodSpinner.setAdapter(marketAdapter);
        mUpdatePeriodSpinner.setSelection(getSpinnerDefaultSelectionPosition());
        mUpdatePeriodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int time = 10000;
                switch (arg2) {
                    case 0:
                        time = 10000;
                        break;
                    case 1:
                        time = 20000;
                        break;
                    case 2:
                        time = 30000;
                        break;
                    case 3:
                        time = 60000;
                        break;
                    case 4:
                        time = 120000;
                        break;
                    case 5:
                        time = 180000;
                        break;
                    case 6:
                        time = 300000;
                        break;
                }
                mMainAppSettingsPreference.setUpdatingPeriodTime(time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mEnableGroup = (RadioGroup) getView().findViewById(R.id.enable_group);
        mEnableGroup
                .check(mMainAppSettingsPreference.getEnableUpdatingService() ? R.id.settings_enable
                        : R.id.settings_disable);
        mEnableGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settings_enable:
                        mMainAppSettingsPreference.setEnableUpdatingService(true);
                        break;
                    case R.id.settings_disable:
                        mMainAppSettingsPreference.setEnableUpdatingService(false);
                        break;
                }
            }
        });
        mEnableWhenScreenOff = (RadioGroup) getView().findViewById(
                R.id.enable_when_screen_off_group);
        mEnableWhenScreenOff
                .check(mMainAppSettingsPreference.getEnableUpdatingWhenScreenOff() ? R.id.settings_enable_when_screen_off
                        : R.id.settings_disable_when_screen_off);
        mEnableWhenScreenOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settings_enable_when_screen_off:
                        mMainAppSettingsPreference.setEnableUpdatingWhenScreenOff(true);
                        break;
                    case R.id.settings_disable_when_screen_off:
                        mMainAppSettingsPreference.setEnableUpdatingWhenScreenOff(false);
                        break;
                }
            }
        });
        mEnableWhenBootUp = (RadioGroup) getView().findViewById(R.id.enable_start_when_boot_up);
        mEnableWhenBootUp
                .check(mMainAppSettingsPreference.getEnableUpdatingWhenBootUp() ? R.id.settings_enable_start_when_boot_up
                        : R.id.settings_disable_when_screen_off);
        mEnableWhenBootUp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settings_enable_start_when_boot_up:
                        mMainAppSettingsPreference.setEnableUpdatingWhenBootUp(true);
                        break;
                    case R.id.settings_disable_when_screen_off:
                        mMainAppSettingsPreference.setEnableUpdatingWhenBootUp(false);
                        break;
                }
            }
        });
    }

    private int getSpinnerDefaultSelectionPosition() {
        int defalutUpdatePeriodSetting = mMainAppSettingsPreference.getUpdatingPeriodTime();
        int rtn = 0;
        switch (defalutUpdatePeriodSetting) {
            case 10000:
                rtn = 0;
                break;
            case 20000:
                rtn = 1;
                break;
            case 30000:
                rtn = 2;
                break;
            case 60000:
                rtn = 3;
                break;
            case 120000:
                rtn = 4;
                break;
            case 180000:
                rtn = 5;
                break;
            case 300000:
                rtn = 6;
                break;
        }
        return rtn;
    }
}
