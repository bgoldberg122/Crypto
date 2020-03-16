package com.example.cryptocoinview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ArrayList<Coin> coins = Coin.getCoins();

        Intent intent = getIntent();
//        String coinSymbol = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        int pos = intent.getIntExtra(MainActivity.EXTRA_MESSAGE,0);

        valueLabel = findViewById(R.id.valueView);
        hourChangeLabel = findViewById(R.id.hourChangeView);
        dayChangeLabel = findViewById(R.id.dayChangeView);
        weekChangeLabel = findViewById(R.id.weekChangeView);
        marketcapLabel = findViewById(R.id.marketcapView);
        volumeLabel = findViewById(R.id.volumeView);
        image = findViewById(R.id.imageView);
        searchImage = findViewById(R.id.imageView2);
        nameLabel = findViewById(R.id.nameView);
        abreviationLabel = findViewById(R.id.abrvView);


        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final Coin coin = coins.get(pos);

        nameLabel.setText(coin.getName());
        valueLabel.setText(formatter.format(coin.getValue()));
        hourChangeLabel.setText(coin.getChange1h() + "%");
        dayChangeLabel.setText(coin.getChange24h() + "%");
        weekChangeLabel.setText(coin.getChange7d() + "%");
        marketcapLabel.setText(formatter.format(coin.getMarketcap()));
        volumeLabel.setText(formatter.format(coin.getVolume()));
        abreviationLabel.setText(coin.getSymbol());
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                searchCoin(coin.getName());
            }
        });
    }

    public void searchCoin (String coinName){
        String url = GOOGLE_DOMAIN + coinName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
