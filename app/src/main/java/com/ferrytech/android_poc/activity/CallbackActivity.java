package com.ferrytech.android_poc.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ferrytech.android_poc.Fragment.Fragment1;
import com.ferrytech.android_poc.R;

public class CallbackActivity extends AppCompatActivity {

    private FragmentManager fm;
    private String name = "Ayush";
    private String age = "21";
    private String city = "Meerut";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_callback);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("age",age);
        bundle.putString("city",city);

        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment1 fragment1 = new Fragment1();
            fragment1.setArguments(bundle);
            transaction.add(R.id.fragment_container_callback, fragment1);
            transaction.commit();

        }

    }
}