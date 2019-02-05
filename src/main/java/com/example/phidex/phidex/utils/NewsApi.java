package com.example.phidex.phidex.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.phidex.phidex.activities.CoinView.Post;
import com.example.phidex.phidex.activities.CoinView.Subreddit;
import com.loopj.android.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;

public class NewsApi {

    private static final String BASE_URL = "https://www.reddit.com/";
    private static final String TAG = "NewsApi";

    private RecyclerView.Adapter adapterToNotify;
    private static AsyncHttpClient client;

    public NewsApi(RecyclerView.Adapter adapterToNotify) {
        this.adapterToNotify = adapterToNotify;
        this.client = new AsyncHttpClient();
        client.setEnableRedirects(true);
    }

    public void getHotPosts(final String coinId, final ArrayList<Post> mPosts) {
        RequestParams params = new RequestParams();
        String subreddit = Subreddit.getSubreddit(coinId);

        try {
            get("r/" + subreddit + "/hot.json", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject data) {
                    try {
                        JSONArray posts = data.getJSONObject("data").getJSONArray("children");
                        Log.d(TAG, Integer.toString(posts.length()) + " posts to prepare.");
                        for (int n = 0; n < posts.length(); n++) {
                            JSONObject post = posts.getJSONObject(n).getJSONObject("data");
                            Log.d(TAG, "preparing post " + n);
                            Post p = new Post(
                                    post.getString("score"),
                                    post.getString("title"),
                                    post.getLong("created_utc"),
                                    post.getString("author"),
                                    post.getString("subreddit"),
                                    post.getString("num_comments"),
                                    post.getString("permalink")
                            );
                            mPosts.add(p);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString(), e);
                    }

                    adapterToNotify.notifyDataSetChanged();
                    Log.d(TAG, "notified.");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject data) {
                    Log.e(TAG, String.format("statusCode %s", statusCode));
                    Log.e(TAG, "headers:");
                    for (Header h : headers) {
                        Log.e(TAG, h.toString());
                    }
                    Log.e(TAG, String.format("data %s", data.toString()));
                    Log.e(TAG, e.toString(), e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d(TAG, "doing request " + getAbsoluteUrl(url) + " with params " + params.toString());
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
}

