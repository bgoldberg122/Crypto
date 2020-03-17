package com.example.cryptocoinview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int pos = intent.getIntExtra(MainActivity.EXTRA_MESSAGE,0);

        updateDetailFragment(pos);

    }

    public void updateDetailFragment(int pos) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", pos);
        fragment.setArguments(bundle);
        transaction.replace(R.id.scroll_view, fragment);
        transaction.commit();
    }
}
