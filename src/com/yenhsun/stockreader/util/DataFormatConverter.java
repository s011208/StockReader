
package com.yenhsun.stockreader.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;

public class DataFormatConverter {
    public static String convertFromCursorToJson(Cursor data) {
        JSONArray resultSet = new JSONArray();
        while (data.moveToNext()) {
            int totalColumn = data.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (data.getColumnName(i) != null) {
                    try {

                        if (data.getString(i) != null) {
                            rowObject.put(data.getColumnName(i), data.getString(i));
                        } else {
                            rowObject.put(data.getColumnName(i), "");
                        }
                    } catch (Exception e) {

                    }
                }
            }
            resultSet.put(rowObject);
        }
        data.close();
        return resultSet.toString();
    }

}
