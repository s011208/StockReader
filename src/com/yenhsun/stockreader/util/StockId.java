
package com.yenhsun.stockreader.util;

public class StockId {
    private String mMarket, mId;

    public StockId(String m, String i) {
        mMarket = m;
        mId = i;
    }

    public String getMarket() {
        return mMarket;
    }

    public String getId() {
        return mId;
    }

    public String toString() {
        if (mMarket == null || mId == null)
            return null;
        return mMarket + ":" + mId;
    }

    /**
     * TODO Test case is needed
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof StockId == false)
            return false;
        StockId c = (StockId)obj;
        if (mMarket != null && mMarket.equalsIgnoreCase(c.mMarket) && mId != null
                && mId.equalsIgnoreCase(c.mId))
            return true;
        return false;
    }

    public int hashCode() {
        return mId.hashCode() + mMarket.hashCode();
    }
}
