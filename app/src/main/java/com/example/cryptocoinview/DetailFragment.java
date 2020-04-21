package com.example.cryptocoinview;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.cryptocoinview.entities.Coin;

import java.text.NumberFormat;

public class DetailFragment extends Fragment {
    public final String GOOGLE_DOMAIN = "https://www.google.com/search?q=";
    public static final String ARG_ITEM_ID = "item_id";
    private String TAG = "DetailFragment";
    private CoinDatabase mDb;


    private Coin coin;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = Room.databaseBuilder(getContext(), CoinDatabase.class, "coin-database").build();

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            //use db to get coin
            new DetailFragment.GetCoinDbTask().execute(getArguments().getString(ARG_ITEM_ID));

            //create new GetCoinTask and execute to start Async Task
//            new GetCoinTask().execute();

////            Gson gson = new Gson();
////            CoinLoreResponse response = gson.fromJson(CoinLoreResponse.json, CoinLoreResponse.class);
////            List<Coin> coins = response.getData();
//                //create retrofit instance with Gson converter
//                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coinlore.net/")
//                        .addConverterFactory(GsonConverterFactory.create()).build();
//
//                //get the service and the 'Call' object for the request
//                CoinService service = retrofit.create(CoinService.class);
//                Call<CoinLoreResponse> coinsCall = service.getCoins();
//
//                coinsCall.enqueue(new Callback<CoinLoreResponse>() {
//                    @Override
//                    public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
//                        if (response.isSuccessful()) {
//                            Log.d(TAG, "onResponse: SUCCESS");
//                            List<Coin> coins = response.body().getData();
//                            for (Coin c : coins) {
//                                if (c.getId().equals(getArguments().getString(ARG_ITEM_ID))) {
//                                    coin = c;
//                                    break;
//                                }
//                            }
//                        }
//                        else {
//                            Log.d(TAG, "onResponse: ERROR IS: " + response.errorBody());
//                        }
//                        updateUi();
//                    }
//
//                    @Override
//                    public void onFailure(Call<CoinLoreResponse> call, Throwable t) {
//                        Log.d(TAG, "onFailure: FAILURE");
//                        t.printStackTrace();
//                    }
//                });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//         int pos = getArguments().getInt("position");
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        updateUi();
        return v;
    }

    //update the view
    private void updateUi() {
        View v = getView();
        //check for v being null to fix double click error
        if (v != null && coin != null) {

            //display icon for coin using glide
            ImageView coinIcon = v.findViewById(R.id.coinDetailIcon);
            Glide.with(this)
                    .load("https://www.coinlore.com/img/25x25/" + coin.getNameid() + ".png")
                    .fitCenter()
                    .into(coinIcon);

            TextView valueLabel = v.findViewById(R.id.valueView);
            TextView hourChangeLabel = v.findViewById(R.id.hourChangeView);
            TextView dayChangeLabel = v.findViewById(R.id.dayChangeView);
            TextView weekChangeLabel = v.findViewById(R.id.weekChangeView);
            TextView marketcapLabel = v.findViewById(R.id.marketcapView);
            TextView volumeLabel = v.findViewById(R.id.volumeView);
            ImageView searchImage = v.findViewById(R.id.imageView2);
            TextView nameLabel = v.findViewById(R.id.nameView);
            TextView abreviationLabel = v.findViewById(R.id.abrvView);

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            ((AppCompatActivity) v.getContext()).setTitle(coin.getName());

            nameLabel.setText(coin.getName());
            valueLabel.setText(formatter.format(Double.valueOf(coin.getPriceUsd())));
            hourChangeLabel.setText(coin.getPercentChange1h() + "%");
            dayChangeLabel.setText(coin.getPercentChange24h() + "%");
            weekChangeLabel.setText(coin.getPercentChange7d() + "%");
            marketcapLabel.setText(formatter.format(Double.valueOf(coin.getMarketCapUsd())));
            volumeLabel.setText(formatter.format(coin.getVolume24()));
            abreviationLabel.setText(coin.getSymbol());
            searchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchCoin(coin.getName());
                }
            });
        }
    }
    private void searchCoin (String coinName){
        String url = GOOGLE_DOMAIN + coinName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private class GetCoinDbTask extends AsyncTask<String, Void, Coin> {
        @Override
        protected Coin doInBackground(String... ids) {
            return mDb.coinDao().getCoin(ids[0]);
        } //order is doInBackground, Progress, onPostExecute
        @Override
        protected void onPostExecute(Coin c){
            coin = c;
            updateUi();
        }
    }
}
