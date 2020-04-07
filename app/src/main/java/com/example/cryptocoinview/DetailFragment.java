package com.example.cryptocoinview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.List;
import com.example.cryptocoinview.entities.Coin;
import com.example.cryptocoinview.entities.CoinLoreResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailFragment extends Fragment {
    private TextView valueLabel;
    private TextView hourChangeLabel;
    private TextView dayChangeLabel;
    private TextView weekChangeLabel;
    private TextView marketcapLabel;
    private TextView volumeLabel;
    private ImageView image;
    private ImageView searchImage;
    private TextView nameLabel;
    private TextView abreviationLabel;
    public final String GOOGLE_DOMAIN = "https://www.google.com/search?q=";
    public static final String ARG_ITEM_ID = "item_id";
    private String TAG = "DetailFragment";


    private Coin coin;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            Gson gson = new Gson();
//            CoinLoreResponse response = gson.fromJson(CoinLoreResponse.json, CoinLoreResponse.class);
//            List<Coin> coins = response.getData();
                //create retrofit instance with Gson converter
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coinlore.net/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                //get the service and the 'Call' object for the request
                CoinService service = retrofit.create(CoinService.class);
                Call<CoinLoreResponse> coinsCall = service.getCoins();

                coinsCall.enqueue(new Callback<CoinLoreResponse>() {
                    @Override
                    public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: SUCCESS");
                            List<Coin> coins = response.body().getData();
                            for (Coin c : coins) {
                                if (c.getId().equals(getArguments().getString(ARG_ITEM_ID))) {
                                    coin = c;
                                    break;
                                }
                            }
                        }
                        else {
                            Log.d(TAG, "onResponse: ERROR IS: " + response.errorBody());
                        }
                        updateUi();
                    }

                    @Override
                    public void onFailure(Call<CoinLoreResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: FAILURE");
                        t.printStackTrace();
                    }
                });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        int pos = getArguments().getInt("position");
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        updateUi();
        return v;

    }


    private void updateUi() {
        View v = getView();
        if (coin != null) {
            valueLabel = v.findViewById(R.id.valueView);
            hourChangeLabel = v.findViewById(R.id.hourChangeView);
            dayChangeLabel = v.findViewById(R.id.dayChangeView);
            weekChangeLabel = v.findViewById(R.id.weekChangeView);
            marketcapLabel = v.findViewById(R.id.marketcapView);
            volumeLabel = v.findViewById(R.id.volumeView);
            image = v.findViewById(R.id.imageView);
            searchImage = v.findViewById(R.id.imageView2);
            nameLabel = v.findViewById(R.id.nameView);
            abreviationLabel = v.findViewById(R.id.abrvView);


            NumberFormat formatter = NumberFormat.getCurrencyInstance();

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
}
