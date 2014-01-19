
package com.yenhsun.stockreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.yenhsun.stockreader.loader.StockDataLoaderService;
import com.yenhsun.stockreader.storage.MainAppSettingsPreference;
import com.yenhsun.stockreader.storage.StockDataPreference;
import com.yenhsun.stockreader.util.StockId;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainObserverList extends Fragment implements OnDragListener {
    public static final String[] SPINNER_ADAPTER = new String[] {
            "TPE", "NASDAQ"
    };
    private static final int SWITCH_MODE_ANIMATION_DURATION = 200;
    private Button mBtnAdd, mBtnEdit, mBtnOk;

    private ListView mStockList;
    private LinearLayout mNormalMode, mEditMode;
    private StockDataPreference mStockDataPreference;
    private MainAppSettingsPreference mMainAppSettingsPreference;
    private StockListAdapter mStockListAdapter;
    private Context mContext;
    private Activity mMainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_observer_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initComponents();
    }

    private void init() {
        mContext = mMainActivity;
        mStockDataPreference = new StockDataPreference(mMainActivity);
        mMainAppSettingsPreference = StockReaderApplicaion.getMainAppSettingsPreference(mContext);
    }

    private void initComponents() {
        mNormalMode = (LinearLayout) getView().findViewById(R.id.layout_normal_mode);
        mEditMode = (LinearLayout) getView().findViewById(R.id.layout_edit_mode);
        mBtnAdd = (Button) getView().findViewById(R.id.btn_add_new);
        mBtnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                createAddDataDialog();
            }
        });
        mBtnEdit = (Button) getView().findViewById(R.id.btn_edit);
        mBtnEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mStockListAdapter.setEditMode(true);
                mStockListAdapter.notifyDataSetChanged();
                switchObserverListMode(false);
            }
        });
        mBtnOk = (Button) getView().findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mStockListAdapter.setEditMode(false);
                mStockListAdapter.notifyDataSetChanged();
                switchObserverListMode(true);
            }
        });

        initStockList();

    }

    private void switchObserverListMode(boolean goingNormalMode) {
        if (goingNormalMode) {
            ObjectAnimator oa = ObjectAnimator.ofFloat(mEditMode, View.ALPHA, 1, 0);
            oa.addListener(new AnimatorListener() {

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // TODO Auto-generated method stub
                    mEditMode.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationStart(Animator animation) {
                    // TODO Auto-generated method stub
                    mNormalMode.setAlpha(0);
                    mNormalMode.setVisibility(View.VISIBLE);
                }
            });
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mNormalMode, View.ALPHA, 0, 1);
            oa.setDuration(SWITCH_MODE_ANIMATION_DURATION);
            oa1.setStartDelay(SWITCH_MODE_ANIMATION_DURATION);
            oa.start();
            oa1.start();
        } else {
            ObjectAnimator oa = ObjectAnimator.ofFloat(mNormalMode, View.ALPHA, 1, 0);
            oa.addListener(new AnimatorListener() {

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // TODO Auto-generated method stub
                    mNormalMode.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationStart(Animator animation) {
                    // TODO Auto-generated method stub
                    mEditMode.setAlpha(0);
                    mEditMode.setVisibility(View.VISIBLE);
                }
            });
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mEditMode, View.ALPHA, 0, 1);
            oa.setDuration(SWITCH_MODE_ANIMATION_DURATION);
            oa1.setStartDelay(SWITCH_MODE_ANIMATION_DURATION);
            oa.start();
            oa1.start();
        }
    }

    private void initStockList() {
        mStockList = (ListView) getView().findViewById(R.id.stock_data_list);
        ArrayList<StockId> data = mStockDataPreference.retriveData();
        if (data == null)
            data = new ArrayList<StockId>();
        mStockListAdapter = new StockListAdapter(mContext, mStockDataPreference);
        mStockList.setAdapter(mStockListAdapter);
        mStockList.setHapticFeedbackEnabled(true);
        mStockList.setOnDragListener(this);
    }

    private static int getListViewHeight(ListView parent) {
        if (parent == null)
            return 0;
        int childCount = parent.getChildCount();
        if (childCount > 0) {
            return parent.getChildAt(0).getHeight();
        }
        return 0;
    }

    private void createAddDataDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.main_add_data_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Spinner market = (Spinner) dialog.findViewById(R.id.main_add_dialog_spinner);
        ArrayAdapter<String> marketAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, SPINNER_ADAPTER);
        market.setAdapter(marketAdapter);
        final EditText id = (EditText) dialog.findViewById(R.id.main_add_dialog_stock_id);
        final Button ok = (Button) dialog.findViewById(R.id.main_add_dialog_ok);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                verifyDataExist(market.getSelectedItem().toString(), id.getText().toString());
                dialog.dismiss();
            }
        });
        final Button cancel = (Button) dialog.findViewById(R.id.main_add_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void verifyDataExist(final String market, final String id) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream is;
                boolean isValid = false;
                try {
                    is = new URL("http://finance.google.com/finance/info?client=ig&q=" + market
                            + ":" + id)
                            .openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    is.close();
                    isValid = true;
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (isValid) {
                    boolean updated = mStockDataPreference.addData(new StockId(market, id));
                    if (updated) {
                        mStockListAdapter.notifyDataChanged();
                        mStockListAdapter.notifyDataSetChanged();
                    }
                } else {
                    mMainActivity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(
                                    mContext,
                                    mContext.getString(R.string.stock_not_found) + ", "
                                            + mContext.getString(R.string.market) + ": " + market
                                            + ", " + mContext.getString(R.string.name) + ": " + id,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_LOCATION:
                break;
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                int y = (int) event.getY();
                mStockList.getX();
                int listviewStartY = (int) mStockList.getX();
                int listItemHeight = getListViewHeight(mStockList);
                int target = (y - listviewStartY) / listItemHeight;
                boolean isReorder = mStockDataPreference.reOrderData(
                        StockListAdapter.sDraggingPosition, target);
                if (isReorder) {
                    mStockListAdapter.notifyDataChanged();
                    mStockListAdapter.notifyDataSetChanged();
                    if (mMainAppSettingsPreference.getEnableUpdatingService())
                        // start service again to update service data
                        mContext.startService(new Intent(mContext,
                                StockDataLoaderService.class));
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                mStockListAdapter.notifyDataSetChanged();
            default:
                break;
        }
        return true;
    }
}
