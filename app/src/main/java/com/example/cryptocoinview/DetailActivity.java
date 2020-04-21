package com.example.cryptocoinview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            final String coinId = intent.getStringExtra(DetailFragment.ARG_ITEM_ID);

            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(DetailFragment.ARG_ITEM_ID, coinId);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.scroll_view, fragment).commit();
        }
    }

}
