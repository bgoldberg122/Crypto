package com.example.cryptocoinview;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.cryptocoinview.entities.Coin;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {
    private List<Coin> mCoins;
    private boolean inWide;
    private MainActivity parentActivity;
    private View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coin coin = (Coin) v.getTag();
                if (inWide) {
                    Fragment fragment = new DetailFragment();
                    Bundle args = new Bundle();
                    args.putString(DetailFragment.ARG_ITEM_ID, coin.getId());
                    fragment.setArguments(args);
                    parentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.scroll_view, fragment).commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailFragment.ARG_ITEM_ID, coin.getId());
                    context.startActivity(intent);
                }
            }
        };

    public CoinAdapter(MainActivity activity, List<Coin> coins, boolean val) {
        mCoins = coins;
        inWide = val;
        parentActivity = activity;
    }

    public static class CoinViewHolder extends RecyclerView.ViewHolder {
        public TextView name, value, change;
        public ImageView coinIcon;

        public CoinViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.coinName);
            value = v.findViewById(R.id.value);
            change = v.findViewById(R.id.change);
            coinIcon = v.findViewById(R.id.rowImage);
        }

    }

    @Override
    public CoinAdapter.CoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_list_row, parent, false);
        return new CoinViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CoinViewHolder holder, int position) {
        Coin coin = mCoins.get(position);

        //Get image from coinlore.com and set the image using Glide
        Glide.with(holder.itemView)
                .load("https://www.coinlore.com/img/25x25/" + coin.getNameid() + ".png")
                .fitCenter()
                .into(holder.coinIcon);

        holder.name.setText(coin.getName());
        holder.value.setText(NumberFormat.getCurrencyInstance().format(Double.valueOf(coin.getPriceUsd())));

        //display text as green if change is pos and red if neg
        String change = coin.getPercentChange24h();
        holder.change.setText(change + " %");
        if(Double.valueOf(change) > 0){
            holder.change.setTextColor(Color.GREEN);
        } else {
            holder.change.setTextColor(Color.RED);
        }
        holder.itemView.setTag(coin);
        holder.itemView.setOnClickListener(mListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCoins.size();
    }

    public void setCoins(List<Coin> coins) {
        mCoins.clear();         //clear existing coins in the list
        mCoins.addAll(coins);   //add all the coins that are passed in to the list
        notifyDataSetChanged(); //notify the UI (view) about the changes
    }
}
