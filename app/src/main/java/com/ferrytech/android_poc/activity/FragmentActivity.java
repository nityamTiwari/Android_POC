package com.ferrytech.android_poc.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ferrytech.android_poc.Fragment.InputFragment;
import com.ferrytech.android_poc.R;

public class FragmentActivity extends AppCompatActivity {

    private FragmentManager fm;
    private String name = "Nityam";
    private String age = "21";
    private String city = "Noida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        System.out.println("FragmentActivity.onCreate");

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("age", age);
        bundle.putString("city",city);

        if (savedInstanceState == null) {
            System.out.println("FragmentActivity.onCreate.savedInstanceState == null");
            fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            InputFragment inputFragment = new InputFragment();
            inputFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, inputFragment);
//          transaction.addToBackStack(null);
            transaction.commit();
//            System.out.println("Already Created");
        }

    }
}