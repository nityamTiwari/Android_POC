package com.ferrytech.android_poc.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.Utils.FontCache;

public class FontActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_font1,btn_font2,btn_font3,btn_font4,btn_font5,btn_font6;
    TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_font);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        initViews();
        setClickListner();

//        btn_font1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Typeface typeface = ResourcesCompat.getFont(FontActivity.this, R.font.font1);
//                tv_text.setTypeface(typeface);
//                tv_text.setTypeface(null,Typeface.BOLD);
//
//            }
//        });
//        btn_font2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Typeface typeface = ResourcesCompat.getFont(FontActivity.this, R.font.font2);
//                tv_text.setTypeface(typeface,Typeface.ITALIC);
//            }
//        });
//        btn_font3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Typeface typeface = ResourcesCompat.getFont(FontActivity.this, R.font.font3);
//                tv_text.setTypeface(typeface,Typeface.BOLD_ITALIC);
//
//            }
//        });
//        btn_font4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Typeface typeface = ResourcesCompat.getFont(FontActivity.this, R.font.font4);
//                tv_text.setTypeface(typeface);
//
//            }
//        });
//        btn_font5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Typeface typeface = getTypeface(R.font.font5);
//                tv_text.setTypeface(typeface,Typeface.NORMAL);
//
//            }
//        });
//        btn_font6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Typeface typeface = getTypeface(R.font.font6);
//                tv_text.setTypeface(typeface,Typeface.ITALIC);
//
//            }
//        });

    }

    private Typeface getTypeface(int fontId) {
        return FontCache.get(this,fontId);
    }


    public void initViews(){
        btn_font1 = findViewById(R.id.btn_font1);
        btn_font2 = findViewById(R.id.btn_font2);
        btn_font3 = findViewById(R.id.btn_font3);
        btn_font4 = findViewById(R.id.btn_font4);
        btn_font5 = findViewById(R.id.btn_font5);
        btn_font6 = findViewById(R.id.btn_font6);
        tv_text = findViewById(R.id.tv_text);
    }
    private void setClickListner(){
        btn_font1.setOnClickListener(this);
        btn_font2.setOnClickListener(this);
        btn_font3.setOnClickListener(this);
        btn_font4.setOnClickListener(this);
        btn_font5.setOnClickListener(this);
        btn_font6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Typeface typeface = null;
        int style = Typeface.NORMAL;

        if (v.getId() == R.id.btn_font1) {
            typeface = getTypeface(R.font.font1);
            style = Typeface.BOLD;
        } else if (v.getId() == R.id.btn_font2) {
            typeface = getTypeface(R.font.font2);
            style = Typeface.ITALIC;
        } else if (v.getId() == R.id.btn_font3) {
            typeface = getTypeface(R.font.font3);
            style = Typeface.BOLD_ITALIC;
        } else if (v.getId() == R.id.btn_font4) {
            typeface = getTypeface(R.font.font4);
        } else if (v.getId() == R.id.btn_font5) {
            typeface = getTypeface(R.font.font5);
        } else if (v.getId() == R.id.btn_font6) {
            typeface = getTypeface(R.font.font6);
            style = Typeface.ITALIC;
        }

        if (typeface != null) {
            tv_text.setTypeface(typeface, style);
        }
    }


    }
