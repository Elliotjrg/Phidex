package com.example.phidex.phidex.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phidex.phidex.R;
import com.example.phidex.phidex.activities.CoinView.Post;

import java.util.ArrayList;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class NewsTabAdapter extends RecyclerView.Adapter<NewsTabAdapter.ViewHolder> {

    private static final String TAG = "NewsTabAdapter";

    private Context mContext;
    private ArrayList<Post> mPosts;

    public NewsTabAdapter(Context mContext, ArrayList<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_tab_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final Post p = mPosts.get(position);

        String timeSince = getRelativeTimeSpanString(p.getTimeCreated()*1000).toString();

        holder.score.setText(p.getScore());
        holder.title.setText(p.getTitle());
        holder.post_details_line.setText(String.format("submitted %s by %s to r/%s", timeSince, p.getAuthor(), p.getSubreddit()));
        holder.comments_line.setText(String.format("%s comments", p.getNumComments()));

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an out link");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.getUrl()));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView score;
        TextView title;
        TextView post_details_line;
        TextView comments_line;
        CardView parent;

        public ViewHolder (View itemView) {
            super(itemView);

            score = itemView.findViewById(R.id.score);
            title = itemView.findViewById(R.id.title);
            post_details_line = itemView.findViewById(R.id.post_details_line);
            comments_line = itemView.findViewById(R.id.comments_line);
            parent = itemView.findViewById(R.id.card_view);
        }
    }
}
