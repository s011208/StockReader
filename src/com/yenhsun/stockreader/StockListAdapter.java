
package com.yenhsun.stockreader;

import java.util.ArrayList;

import com.yenhsun.stockreader.loader.StockDataLoaderService;
import com.yenhsun.stockreader.storage.StockDataPreference;
import com.yenhsun.stockreader.util.StockId;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class StockListAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater;

    private ArrayList<StockId> mData;

    private boolean mIsDeleteMode;

    private StockDataPreference mStockDataPreference;

    public static int sDraggingPosition;

    public StockListAdapter(Context c, StockDataPreference s) {
        mContext = c;
        mStockDataPreference = s;
        notifyDataChanged();
        mInflater = LayoutInflater.from(mContext);
    }

    public void notifyDataChanged() {
        mData = mStockDataPreference.retriveData();
    }

    public void setEditMode(boolean m) {
        mIsDeleteMode = m;
    }

    public boolean isEditMode() {
        return mIsDeleteMode;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.stock_adapter, null);
        final StockId data = mData.get(position);
        TextView t1 = (TextView) convertView.findViewById(R.id.main_observe_list_market);
        t1.setText(data.getMarket());
        TextView t2 = (TextView) convertView.findViewById(R.id.main_observe_list_id);
        t2.setText(data.getId());
        final Button btn = (Button) convertView.findViewById(R.id.man_observe_list_delete);
        btn.setVisibility(mIsDeleteMode ? View.VISIBLE : View.GONE);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mStockDataPreference.removeData(data);
                notifyDataChanged();
                notifyDataSetChanged();
                mContext.startService(new Intent(mContext,
                        StockDataLoaderService.class));
            }
        });
        convertView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (mIsDeleteMode) {
                    sDraggingPosition = position;
                    v.setVisibility(View.GONE);
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
        return convertView;
    }

}
