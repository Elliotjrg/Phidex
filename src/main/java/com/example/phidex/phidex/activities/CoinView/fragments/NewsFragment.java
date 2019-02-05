package com.example.phidex.phidex.activities.CoinView.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.activities.CoinView.Post;
import com.example.phidex.phidex.adapters.NewsTabAdapter;
import com.example.phidex.phidex.utils.NewsApi;

import java.util.ArrayList;

public class NewsFragment extends Fragment{
    private static final String TAG = "NewsFragment";
    private ArrayList<Post> mPosts = new ArrayList<>();
    private NewsApi api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateView: started.");

        View rootView = inflater.inflate(R.layout.news_fragment, container, false);
        RecyclerView rcv = rootView.findViewById(R.id.news_recycler);
        String coinId = getActivity().getIntent().getStringExtra("coinId");
        RecyclerView.Adapter adapter = new NewsTabAdapter(this.getContext(), mPosts);

        api = new NewsApi(adapter);
        initPosts(coinId);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return rootView;
    }

    private void initPosts(String coinId) {
        Log.d(TAG, "initPosts: preparing posts.");
        api.getHotPosts(coinId, mPosts);
        Log.d(TAG, mPosts.toString());
    }
}