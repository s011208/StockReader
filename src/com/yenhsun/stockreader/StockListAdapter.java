
package com.yenhsun.stockreader;

import java.util.ArrayList;

import com.yenhsun.stockreader.util.StockId;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StockListAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mInflater;

    private ArrayList<StockId> mData;

    public StockListAdapter(Context c, ArrayList<StockId> data) {
        mContext = c;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = mInflater.inflate(R.layout.stock_adapter, null);
        StockId data = mData.get(position);
        TextView t1 = (TextView)convertView.findViewById(R.id.adapter_txt1);
        t1.setText(data.getMarket());
        TextView t2 = (TextView)convertView.findViewById(R.id.adapter_txt2);
        t2.setText(data.getId());
        return convertView;
    }

}
