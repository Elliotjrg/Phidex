package com.example.phidex.phidex.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Transaction;
import com.example.phidex.phidex.adapters.TransactionAdapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private RecyclerView  recyclerView;
    private TransactionAdapter adapter;
    private AppDatabase ad;
    private List<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ad = AppDatabase.getAppDatabase(getApplicationContext());
        recyclerView = findViewById(R.id.recycler_view_transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateActivity();
    }

    // Used to refresh the transactions shown on the transactions page
    public void updateActivity() {
        transactions = ad.transactionDao().getAllTransactions();
        adapter = new TransactionAdapter(transactions, ad, this);
        recyclerView.setAdapter(adapter);
        if (transactions.size() > 0) {
            findViewById(R.id.no_transactions_message).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_transactions_message).setVisibility(View.VISIBLE);
        }
    }
}
