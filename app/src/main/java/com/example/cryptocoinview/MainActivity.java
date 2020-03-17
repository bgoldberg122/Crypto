package com.example.cryptocoinview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Boolean inWide;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final String EXTRA_MESSAGE = "com.example.cryptocoinview.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.coinList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        CoinAdapter.RecyclerViewClickListener listener = new CoinAdapter.RecyclerViewClickListener(){
            @Override
            public void onClick(View v, int position) {
//                launchDetailActivity(position);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("inWide", inWide);
                fragment.setArguments(bundle);
                transaction.replace(R.id.scrollView, fragment);
                transaction.commit();
            }
        };

        adapter = new CoinAdapter(Coin.getCoins(), listener);
        recyclerView.setAdapter(adapter);

    }

//    public void launchDetailActivity(int position) {
//        Intent intent = new Intent(this, DetailActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, position);
//        startActivity(intent);
//    }
}

