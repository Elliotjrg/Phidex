package com.example.phidex.phidex.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phidex.phidex.activities.PortfolioActivity;
import com.example.phidex.phidex.R;
import com.example.phidex.phidex.RoomDatabase.AppDatabase;
import com.example.phidex.phidex.RoomDatabase.Coin;
import com.example.phidex.phidex.activities.CoinView.ViewCoinActivity;

import java.util.Comparator;
import java.util.List;

public class SearchCoinAdapter extends RecyclerView.Adapter<SearchCoinAdapter.ViewHolders> {

    private AppDatabase ad;
    private Context mContext;
    private final Comparator<Coin> mComparator;
    Resources res;
    public float coinHolding;

    public SearchCoinAdapter(AppDatabase ad, Context context, Comparator<Coin> comparator) {
        this.ad = ad;
        this.res = context.getResources();
        mComparator = comparator;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(SearchCoinAdapter.ViewHolders holder, int position) {

        final Coin thisCoin = mSortedCoinList.get(position);

        holder.coinName.setText(thisCoin.getCoinName());
        holder.price.setText("Price: $" + Float.toString(thisCoin.getPrice()));
        holder.percentChange.setText(Float.toString(thisCoin.getPercentChange()));

        // dynamic image change based on Coin name
        String imageString = thisCoin.getCoinName().toLowerCase().replaceAll("\\s","");
        int resId = res.getIdentifier(imageString, "drawable", PortfolioActivity.PACKAGE_NAME);
        holder.coinIcon.setImageResource(resId);

        if (thisCoin.getPercentChange() > 0) {
            holder.percentChange.setTextColor(Color.GREEN);
            holder.arrow.setImageResource(R.drawable.arrow_up);
        } else {
            holder.percentChange.setTextColor(Color.RED);
            holder.arrow.setImageResource(R.drawable.arrow_down);
        }

        holder.holding.setText("Holding: " + thisCoin.getHolding() + " " + thisCoin.getCoinCode() + ": $" + Float.toString(thisCoin.getPrice() * thisCoin.getHolding()));
        coinHolding = thisCoin.getPrice();

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewCoinActivity.class);
                intent.putExtra("name", thisCoin.getCoinName());
                intent.putExtra("coinId", thisCoin.getCoinId());
                mContext.startActivity(intent);
            }
        });
    }

    public void add(Coin model) {
        mSortedCoinList.add(model);
    }

    public void addAll(List<Coin> models) {
        mSortedCoinList.addAll(models);
    }

    public void replaceAll(List<Coin> models) {
        for (int i = mSortedCoinList.size() - 1; i >= 0; i--) {
            final Coin model = mSortedCoinList.get(i);
            if (!models.contains(model)) {
                mSortedCoinList.remove(model);
            }
        }
        mSortedCoinList.addAll(models);
    }

    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_coin_row, parent, false);
        this.mContext = parent.getContext();
        return new ViewHolders(view);
    }

    @Override
    public int getItemCount() {
        if (mSortedCoinList == null) {
            System.out.println("No Coins!\n\n\n");
            return 0;
        }
        return mSortedCoinList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        public LinearLayout parentLayout;
        public TextView coinName;
        public TextView price;
        public TextView percentChange;
        public TextView holding;
        public ImageView arrow;
        public ImageView coinIcon;

        public ViewHolders(View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.portfolio_parent);

            coinName = itemView.findViewById(R.id.coin_name);
            price = itemView.findViewById(R.id.price);
            percentChange = itemView.findViewById(R.id.percent_change);
            holding = itemView.findViewById(R.id.holding);
            arrow = itemView.findViewById(R.id.arrow);
            coinIcon = itemView.findViewById(R.id.coin_icon);
        }
    }

    private final SortedList<Coin> mSortedCoinList = new SortedList<>(Coin.class, new SortedList.Callback<Coin>() {

        @Override
        public void onInserted(int position, int count){
            notifyItemRangeInserted(position, count);
        }
        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(Coin a, Coin b) {
            return mComparator.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(Coin oldItem, Coin newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Coin item1, Coin item2) {
            return item1.getCoinId() == item2.getCoinId();
        }
    });
}