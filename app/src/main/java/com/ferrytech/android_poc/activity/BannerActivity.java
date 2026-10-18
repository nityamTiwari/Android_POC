package com.ferrytech.android_poc.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.BannerAdapter;
import com.ferrytech.android_poc.model.BannerModel;
import com.ferrytech.android_poc.model.ButtonStyle;

import java.util.ArrayList;

public class BannerActivity extends AppCompatActivity {


    Context context;
    RecyclerView rvBanner;
    ArrayList<BannerModel> arrBanner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_banner);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         context = this;
         arrBanner = ListData();
        rvBanner = findViewById(R.id.rv_banner);
        rvBanner.setLayoutManager(new LinearLayoutManager(this));

        ButtonStyle style = new ButtonStyle(
                Color.parseColor("#4E8D9C"), // bgColor
                Color.parseColor("#4B2E2B"), // borderColor
                5,                           // borderWidth
                1,                          // radius
                Color.parseColor("#F9F6C4")  // hoverColor
        );


        BannerAdapter adapter = new BannerAdapter(context,arrBanner,style);
        rvBanner.setAdapter(adapter);

    }

    private ArrayList<BannerModel> ListData() {
         ArrayList<BannerModel> arrBanners = new ArrayList<>();
        arrBanners.add(new BannerModel(
                "Shoes Sale",
                "50% off today",
                "Buy Now",
                "Details"
        ));
        arrBanners.add(new BannerModel(
                "New Collection",
                "Summer arrivals",
                "Shop",
                "View"
        ));

        return arrBanners;
    }
}