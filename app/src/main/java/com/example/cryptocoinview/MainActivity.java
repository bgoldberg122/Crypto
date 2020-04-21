package com.example.cryptocoinview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.cryptocoinview.entities.Coin;
import com.example.cryptocoinview.entities.CoinLoreResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    boolean inWide;

    private RecyclerView recyclerView;
    private CoinAdapter coinAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final String EXTRA_MESSAGE = "com.example.cryptocoinview.MESSAGE";
    private String TAG = "MainActivity";
    private CoinDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inWide = findViewById(R.id.scroll_view) != null;

        recyclerView = findViewById(R.id.coinList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        coinAdapter = new CoinAdapter(this, new ArrayList<Coin>(), inWide);
        recyclerView.setAdapter(coinAdapter);

        //create Db
        mDb = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "coin-database").build();

        //
        // execute Async task
        new GetCoinDbTask().execute();
        new GetCoinTask().execute();

////        Gson gson = new Gson();
////        CoinLoreResponse response = gson.fromJson(CoinLoreResponse.json, CoinLoreResponse.class);
////        List<Coin> coins = response.getData();
//
////        try {         //don't need for async
//
//            //create retrofit instance with Gson converter and parse retrieved JSON using GSON deserializer
//            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coinlore.net/")
//                    .addConverterFactory(GsonConverterFactory.create()).build();
//
//            //get the service and the 'Call' object for the request
//            CoinService coinService = retrofit.create(CoinService.class);
//            Call<CoinLoreResponse> coinsCall = coinService.getCoins();
//
//            //execute request
////          Response<CoinLoreResponse> coinsResponse = coinCall.execute(); //synchronous call --> bad bc it waits for the call to be completed and freezes the UI
////            List<Coin> coins = coinsResponse.body().getData();
//
//            //make async call instead
//            coinsCall.enqueue(new Callback<CoinLoreResponse>() {
//                @Override
//                public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
//                    if (response.isSuccessful()) {
//                        Log.d(TAG, "onResponse: SUCCESS");
//                        List<Coin> coins = response.body().getData();
//                        //Connect recyclerView adapter with the retrieved data
//                        coinAdapter.setCoins(coins);
//                    }
//                   else {
//                       Log.d(TAG, "onResponse: ERROR IS: " + response.errorBody());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CoinLoreResponse> call, Throwable t) {
//                    Log.d(TAG, "onFailure: FAILURE IS: " + t.getLocalizedMessage());
//                }
//            });
//
////        } catch (IOException e) { //         dont need for asynch
////            e.printStackTrace();
////        }

    }

    private class GetCoinTask extends AsyncTask<Void, Void, List<Coin>> {
        //try the api call again, but with .execute(), since this is already an Asynchronous Task
        //from the main thread. Throw an exception on failure.
        @Override
        protected List<Coin> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground: SUCCESS");

                //Build retrofit object with the coinlore base url and gson
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coinlore.net/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                //get the service and the 'Call' object for the request
                CoinService coinService = retrofit.create(CoinService.class);
                Call<CoinLoreResponse> coinsCall = coinService.getCoins();

                //execute the request nd get back a List of Coins
                Response<CoinLoreResponse> coinsResponse = coinsCall.execute(); //synchronous call
                List<Coin> coins = coinsResponse.body().getData();

                //1. delete all entries from the database
                //2. insertAll new set of data from results
                mDb.coinDao().deleteAll(mDb.coinDao().getCoins().toArray(new Coin[mDb.coinDao().getCoins().size()]));
                mDb.coinDao().insertAll(coins.toArray(new Coin[coins.size()]));

                return coins;

            } catch (IOException e) {
                Log.d(TAG, "onFailure: FAILURE");
                e.printStackTrace();
                return null;
            }
        }

        //Override the onPostExecute method to run when the background thread has completed (when the coins have been retrieved)
        @Override
        protected void onPostExecute(List<Coin> coins) {
            //call the setCoins method to set the returned data to the coins list/coinAdapter
            coinAdapter.setCoins(coins);
        }
    }

    private class GetCoinDbTask extends AsyncTask<Void, Void, List<Coin>>{

        @Override
        protected List<Coin> doInBackground(Void... voids) {
            return mDb.coinDao().getCoins();
        }

        @Override
        protected void onPostExecute(List<Coin> coins){
            coinAdapter.setCoins(coins);
        }
    }

}

