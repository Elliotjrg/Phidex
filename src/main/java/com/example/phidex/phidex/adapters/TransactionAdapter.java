package com.example.phidex.phidex.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.RoomDatabase.Transaction;
import com.example.phidex.phidex.activities.TransactionsActivity;
import com.example.phidex.phidex.activities.CoinView.ViewCoinActivity;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolders> {

    private List<Transaction> transactions;
    private static AppDatabase ad;
    private static Context mContext;

    public TransactionAdapter(List<Transaction> transactions, AppDatabase ad, Context context) {
        this.transactions = transactions;
        this.ad = ad;
        mContext = context;
    }

    @Override
    public TransactionAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_transaction_row, parent, false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(final TransactionAdapter.ViewHolders holder, int position) {
        final Transaction thisTransaction = transactions.get(position);
        final Coin thisCoin = ad.coinDao().getCoinGivenId(thisTransaction.getCoinName());

        holder.transactionDate.setText(thisTransaction.getDate());
        holder.transaction.setText(String.format("%s %s (%s)", fmt(thisTransaction.getCoinAmount()), thisCoin.getCoinName(), thisCoin.getCoinCode()));
        holder.transactionPrice.setText(String.format(" for $%.2f", thisTransaction.getPricePaid()));
        holder.portfolioChange.setText(String.format("Portfolio delta: $%.2f", (thisTransaction.getPricePaid() * thisTransaction.getCoinAmount())));


        if(thisTransaction.getCoinAmount() > 0) {
            holder.portfolioChange.setTextColor(Color.GREEN);
            holder.arrow.setImageResource(R.drawable.arrow_up);
        } else {
            holder.portfolioChange.setTextColor(Color.RED);
            holder.arrow.setImageResource(R.drawable.arrow_down);
        }

        // Only set click listener if we are clicking on transaction from the transaction history page
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof TransactionsActivity) {
                    Intent intent = new Intent(mContext, ViewCoinActivity.class);
                    intent.putExtra("name", thisCoin.getCoinName());
                    intent.putExtra("coinId", thisCoin.getCoinId());
                    mContext.startActivity(intent);
                }
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final PopupMenu popup = new PopupMenu(view.getContext(), holder.itemView, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.transaction_delete_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Delete specified transaction and delete that amount from the coin's holding
                        ad.transactionDao().deleteTransactionFromId(thisTransaction.getTransactionId());
                        ad.coinDao().addToCoinHolding(thisCoin.getCoinId(), -thisTransaction.getCoinAmount());

                        // Erase coin's portfolio rank if there are no other transactions
                        if (ad.transactionDao().getTransactionsFromCoinId(thisCoin.getCoinId()).size() == 0) {
                            ad.coinDao().removeCoinFromPortfolio(thisCoin.getCoinId());
                        }

                        if(mContext instanceof TransactionsActivity) {
                            TransactionsActivity ta = (TransactionsActivity)mContext;
                            ta.updateActivity();
                        } else if (mContext instanceof ViewCoinActivity) {
                            ViewCoinActivity va = (ViewCoinActivity)mContext;
                            va.updateTransactionsFragment();
                        }

                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
    }

    public static String fmt(float f) {
        if (f == (int) f) {
            return String.format("%d", (int) f);
        } else {
            return String.format("%s", f);
        }
    }

    @Override
    public int getItemCount() {
        if (transactions == null) {
            System.out.println("No transactions!\n\n\n");
            return 0;
        }
        return transactions.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        private View itemView;
        private ConstraintLayout parentLayout;
        private TextView transactionDate;
        private TextView transaction;
        private TextView transactionPrice;
        private TextView portfolioChange;
        private ImageView arrow;

        private ViewHolders(View itemView) {
            super(itemView);

            this.itemView = itemView;
            parentLayout = itemView.findViewById(R.id.transaction_parent);

            transactionDate = itemView.findViewById(R.id.transaction_date);
            transaction = itemView.findViewById(R.id.transaction);
            transactionPrice = itemView.findViewById(R.id.transaction_price);
            portfolioChange = itemView.findViewById(R.id.transaction_total);
            arrow = itemView.findViewById(R.id.arrow);
        }
    }
}
