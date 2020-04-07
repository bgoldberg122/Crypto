package com.example.cryptocoinview;

import com.example.cryptocoinview.entities.CoinLoreResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoinService  {
    @GET("/api/tickers/")
    Call<CoinLoreResponse> getCoins();
}
