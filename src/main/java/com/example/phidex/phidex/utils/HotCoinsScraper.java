package com.example.phidex.phidex.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.phidex.phidex.activities.HotCoinsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;


public class HotCoinsScraper {

    private static final String TAG = "HotCoinsScraper";
    private static final String BASE_URL = "https://stokz.com/";
    private RecyclerView.Adapter adapterToNotify;
    private static AsyncHttpClient client;

    public HotCoinsScraper(RecyclerView.Adapter adapterToNotify) {
        this.adapterToNotify = adapterToNotify;
        this.client = new AsyncHttpClient();
        client.setEnableRedirects(true);
    }

    public void getHotCoins(String request, final ArrayList<HotCoinsActivity.HotCoin> mCoins) {
        request = "cryptocurrencies/trending";

        try {
            get(request, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(TAG, "scraping failed");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    try {
                        Document doc = Jsoup.parse(responseString);
                        Elements rows = doc.getElementById("appMarketCapList-1")
                                .getElementsByTag("tbody").first()
                                .getElementsByTag("tr");

                        int i = 1;
                        for (Element r : rows) {
                            Log.d(TAG, r.toString());
                            String data = r.toString();
                            Pattern pattern = Pattern.compile("<img src=\"([A-Za-z0-9-._~:\\/?#\\[\\]@!$&'()*+,;=]+?)\".*?<strong>([0-9A-Za-z ]+) *[\\[]*.*?<\\/strong> <br> (\\w+) <"); //https://regex101.com/
                            Matcher matcher = pattern.matcher(data);
                            if (matcher.find()) {
                                Log.d(TAG, String.format("%s %s %s", matcher.group(1), matcher.group(3), matcher.group(2)));
                                mCoins.add(new HotCoinsActivity.HotCoin(
                                        Integer.toString(i),
                                        matcher.group(1),
                                        matcher.group(3),
                                        matcher.group(2).trim(),
                                        "1231",
                                        "#12.34",
                                        "0.04%"
                                ));
                            } else {
                                mCoins.add(new HotCoinsActivity.HotCoin(
                                        Integer.toString(i),
                                        "",
                                        "BRK",
                                        "broken link",
                                        "1231",
                                        "$12.34",
                                        "0.04%"
                                ));
                            }
                            i++;
                        }

                        adapterToNotify.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e(TAG, e.toString(), e);
                    }

                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }
}
