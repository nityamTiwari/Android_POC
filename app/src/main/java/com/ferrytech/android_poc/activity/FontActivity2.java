package com.ferrytech.android_poc.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ferrytech.android_poc.Mvvm.viewmodels.FontViewmodel;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.Utils.FontCache;


public class FontActivity2 extends AppCompatActivity {

    private TextView tv_text;
    private FontViewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_font);

        tv_text = findViewById(R.id.tv_text);

        viewModel = new ViewModelProvider(this).get(FontViewmodel.class);

        observeFontChanges();
        setupClicks();
    }

    private void setupClicks() {

        findViewById(R.id.btn_font1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFontSelected(v.getId());
            }
        });

        findViewById(R.id.btn_font2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFontSelected(v.getId());
            }
        });

        findViewById(R.id.btn_font3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFontSelected(v.getId());
            }
        });

        findViewById(R.id.btn_font4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFontSelected(v.getId());
            }
        });

        findViewById(R.id.btn_font5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFontSelected(v.getId());
            }
        });

        findViewById(R.id.btn_font6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFontSelected(v.getId());
            }
        });
    }

    private void observeFontChanges() {

        viewModel.getFont().observe(this, state -> {

            if (state != null) {

                Typeface tf = FontCache.get(this, state.fontRes);

                tv_text.setTypeface(tf, state.style);
            }
        });
    }
}