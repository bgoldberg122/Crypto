package com.example.cryptocoinview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.cryptocoinview.entities.Coin;
import com.example.cryptocoinview.entities.CoinLoreResponse;

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

//        Gson gson = new Gson();
//        CoinLoreResponse response = gson.fromJson(CoinLoreResponse.json, CoinLoreResponse.class);
//        List<Coin> coins = response.getData();

//        try {         //don't need for async

            //create retrofit instance with Gson converter and parse retrieved JSON using GSON deserializer
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coinlore.net/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            //get the service and the 'Call' object for the request
            CoinService coinService = retrofit.create(CoinService.class);
            Call<CoinLoreResponse> coinsCall = coinService.getCoins();

            //execute request
//          Response<CoinLoreResponse> coinsResponse = coinCall.execute(); //synchronous call --> bad bc it waits for the call to be completed and freezes the UI
//            List<Coin> coins = coinsResponse.body().getData();

            //make async call instead
            coinsCall.enqueue(new Callback<CoinLoreResponse>() {
                @Override
                public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: SUCCESS");
                        List<Coin> coins = response.body().getData();
                        //Connect recyclerView adapter with the retrieved data
                        coinAdapter.setCoins(coins);
                    }
                   else {
                       Log.d(TAG, "onResponse: ERROR IS: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<CoinLoreResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: FAILURE IS: " + t.getLocalizedMessage());
                }
            });

//        } catch (IOException e) { //         dont need for asynch
//            e.printStackTrace();
//        }

    }


}

