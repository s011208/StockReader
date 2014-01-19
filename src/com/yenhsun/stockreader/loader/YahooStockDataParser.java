
package com.yenhsun.stockreader.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class YahooStockDataParser {

    public void parse(String url) {
        try {
            InputStream is = new URL("http://tw.stock.yahoo.com/q/q?s=5491")
                    .openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName("UTF-8")));
            String s;
            int j = 0;
            boolean start = false;
            do {
                s = rd.readLine();
                if (s != null) {
                    if (start) {
                        System.out.println(s.replaceAll("<[^>]*>", "").trim());
                        j++;
                        if (j >= 10) {
                            start = false;
                            break;
                        }
                    }
                    if (s.contains("/q/bc?s=5491")) {
                        start = true;
                    }
                }
            } while (true);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
