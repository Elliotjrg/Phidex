package com.example.phidex.phidex.utils;

import android.util.Log;

import com.example.phidex.phidex.activities.ListActivity;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import com.example.phidex.phidex.RoomDatabase.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used to call CoinMarketCap's API.
 *
 * This class makes use of the async http library to do all http requests asynchronously outside of the UI thread.
 */
public class CoinDataUtil {

    private static final String BASE_URL = "https://api.coinmarketcap.com/v1/";

    private static AppDatabase ad;
    private ListActivity currentActivity;
    private static AsyncHttpClient client;

    public CoinDataUtil(AppDatabase ad, ListActivity currentActivity) {
        this.ad = ad;
        this.currentActivity = currentActivity;
        client = new AsyncHttpClient();
    }

    public void addCoinToWatchlist(String coinId) {
        int rank = ad.coinDao().getCoinWatchlistRankGivenId(coinId);
        if (rank < 0) {
            ad.coinDao().setCoinWatchlistRank(ad.coinDao().getCoinNamesInWatchlist().size(), coinId);
        }
    }

    /**
     * Gets data from API for top 100 coins and stores in database
     */
    public void addTopCoinsToDatabase(final Map<String, String> names) {
        RequestParams params = new RequestParams();
        params.put("convert", "AUD");
        try {
            get("ticker/", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray coinDataArray) {
                    insertOrUpdateCoins(coinDataArray);
                    currentActivity.updateActivity();
                    populateMapOfCoinNames(names);
                }
            });
        } catch (Exception e) {
            //API call failed
        }
    }

    /**
     * Increases holding value for a single coin.
     *
     * @param coinId this is the id of the coin as shown on coinmarketcap's API. e.g. Bitcoin's one is "bitcoin".
     * @throws JSONException
     */
    public void increaseCoinHolding(String coinId, final float holding) {

        // If it is a new coin, we need to give it a portfolio ranking
        int rank = ad.coinDao().getCoinPortfolioRankGivenId(coinId);
        if (rank < 0) {
            ad.coinDao().setCoinPortfolioRank(ad.coinDao().getNumberOfCoinsInPortfolio(), coinId);
        }

        if (holding != 0) {
            ad.coinDao().addToCoinHolding(coinId, holding);
        }
        currentActivity.updateActivity();
    }

    /**
     * Creates map of coinId to coinName for all coins stored in the database
     * @param names the map we store the new map in
     */

    public void populateMapOfCoinNames(final Map<String, String> names) {
        List<Coin> coins = ad.coinDao().getCoins();

        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            names.put(coin.getCoinName(), coin.getCoinId());
        }
    }

    /** Either inserts new coins into Coin table, or updates coins if it already exists
     *
     * @param coinDataArray The array of coins to be inserted or updated
     * @return a list of coinIds that were updated or inserted
     */
    private ArrayList insertOrUpdateCoins(JSONArray coinDataArray) {

        ArrayList coinIds = new ArrayList<String>();

        for (int i = 0; i < coinDataArray.length(); i++) {
            JSONObject data = null;

            try {
                data = coinDataArray.getJSONObject(i);
            } catch (Exception e) {
                // Could not parse JSON
            }

            if (data != null) {
                Coin coin = createCoinFromJsonData(data);
                String coinId = coin.getCoinId();
                try {
                    ad.coinDao().insertAll(coin);
                } catch (Exception e) {
                    // If it can't insert, it already exists in the database and so we must update
                    updateCoinData(coin);
                }

                coinIds.add(coinId);
            }
        }
        return coinIds;
    }

    /**
     * Given coin, updates its values in database.
     */
    private void updateCoinData(Coin coin) {
        ad.coinDao().updateCoinData(
                coin.getCoinName(),
                coin.getPrice(),
                coin.getVolume24hr(),
                coin.getMarketCap(),
                coin.getAvailableSupply(),
                coin.getPercentChange());
    }

    /**
     * Creates a Coin object given a JSONObject.
     * Returns null if an exception is thrown.
     */
    private Coin createCoinFromJsonData(JSONObject data) {
        try {
            //Checkout the results of the API call in Logcat! (View > Tool Windows > Logcat)
            Log.d("HttpUtil", data.toString());

            return new Coin(
                    data.getString("id"),
                    data.getString("name"),
                    data.getString("symbol"),
                    "",
                    parseFloat(data.getString("price_aud")),
                    -1, //portfolioRank
                    -1, //watchlistRank
                    0, //holding
                    parseFloat(data.getString("24h_volume_aud")),
                    parseDouble(data.getString("market_cap_aud")),
                    parseDouble(data.getString("available_supply")),
                    parseDouble(data.getString("total_supply")),
                    parseFloat(data.getString("percent_change_24h"))
            );

        } catch (JSONException e) {
            Log.e("HttpUtil", e.toString());
            return null;
        }
    }

    private float parseFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return 0f;
        }
    }

    private double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0d;
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
}

