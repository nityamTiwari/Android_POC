package com.ferrytech.android_poc.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferrytech.android_poc.R;

public class PermissionActivity extends AppCompatActivity {


    private Button btn_camera;
    private Button btn_location;
    private ActivityResultLauncher<String> commonLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_permission);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_camera = findViewById(R.id.btn_camera);
        btn_location = findViewById(R.id.btn_location);
        initRequestLauncher(RequestType.CAMERA);
        initRequestLauncher(RequestType.LOCATION);


        btn_camera.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                commonLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        btn_location.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                openLocation();
            } else {
                commonLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });


    }

    private void initRequestLauncher(RequestType type) {
        commonLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                switch (type) {
                    case CAMERA:
                        openCamera();
                        break;
                    case LOCATION:
                        openLocation();
                        break;
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openLocation() {
        Toast.makeText(this, "Open Google Map", Toast.LENGTH_SHORT).show();
    }

    private void openCamera() {
        Toast.makeText(this, "Camera Opened", Toast.LENGTH_SHORT).show();
    }

}

