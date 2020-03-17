package com.example.cryptocoinview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

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
//    View.OnClickListener listener;
    public final String GOOGLE_DOMAIN = "https://www.google.com/search?q=";


    public DetailFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment DetailFragment.
//     */
// //    TODO: Rename and change types and number of parameters
//    public static DetailFragment newInstance(String param1, String param2) {
//        Fragment fragment = new DetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("inWide", inWide);
//        bundle.putInt("position", position);
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onAttach (Context context) {
//        super.onAttach(context);
//        try {
//            listener = (View.OnClickListener) context;
//            }
//        catch (ClassCastException e) {
//            throw new ClassCastException(context.toString());
//        }
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int pos = getArguments().getInt("pos");
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ArrayList<Coin> coins = Coin.getCoins();

        valueLabel =  v.findViewById(R.id.valueView);
        hourChangeLabel =  v.findViewById(R.id.hourChangeView);
        dayChangeLabel =  v.findViewById(R.id.dayChangeView);
        weekChangeLabel =  v.findViewById(R.id.weekChangeView);
        marketcapLabel =  v.findViewById(R.id.marketcapView);
        volumeLabel =  v.findViewById(R.id.volumeView);
        image =  v.findViewById(R.id.imageView);
        searchImage =  v.findViewById(R.id.imageView2);
        nameLabel =  v.findViewById(R.id.nameView);
        abreviationLabel = v.findViewById(R.id.abrvView);

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

        return v;
    }

    public void searchCoin (String coinName){
        String url = GOOGLE_DOMAIN + coinName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
