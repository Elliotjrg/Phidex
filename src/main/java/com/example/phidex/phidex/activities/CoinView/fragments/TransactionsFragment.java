package com.example.phidex.phidex.activities.CoinView.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Transaction;
import com.example.phidex.phidex.adapters.TransactionAdapter;

import java.util.List;

public class TransactionsFragment extends Fragment {

    String coinId;
    private List<Transaction> transactions;
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private AppDatabase ad;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.transactions_fragment, container, false);
        coinId = this.getArguments().getString("coinId");
        mRecyclerView = view.findViewById(R.id.recycler_view_transactions_tab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ad = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        updateFragment();
        return view;
    }

    public void updateFragment() {
        transactions = ad.transactionDao().getTransactionsFromCoinId(coinId);
        adapter = new TransactionAdapter(transactions, ad, this.getContext());
        mRecyclerView.setAdapter(adapter);
        if (transactions.size() > 0) {
            view.findViewById(R.id.no_transactions_message).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.no_transactions_message).setVisibility(View.VISIBLE);
        }
    }

}
