package com.ferrytech.android_poc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferrytech.android_poc.R;

public class CameraActivity extends AppCompatActivity {
    Button btn_horizontal;
    Button btn_vertical;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findId();
        clickListner();

    }

    public  void clickListner(){
        btn_horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera2("Horizontal");
            }
        });
      btn_vertical.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              openCamerax("Vertical");
          }
      });
    }
    public  void findId(){
        btn_horizontal = findViewById(R.id.btn_horizontal);
        btn_vertical = findViewById(R.id.btn_vertical);

    }

    public  void openCamera2(String Mode){
        Intent intent = new Intent(this, RecorderActivity.class);
        intent.putExtra("Mode",Mode);
        startActivity(intent);
    }
    public  void openCamerax(String Mode){
        Intent intent = new Intent(this, CameraXActivity.class);
        intent.putExtra("Mode",Mode);
        startActivity(intent);
    }
}