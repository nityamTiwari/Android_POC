package com.ferrytech.android_poc.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.Mvvm.IntentLauncher;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.MyAdapter;
import com.ferrytech.android_poc.model.AndroidModel;
import com.ferrytech.android_poc.model.BaseAndroidModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<BaseAndroidModel> arrContacts = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;

        arrContacts = createContactList();

        adapter = new MyAdapter(context, arrContacts);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<BaseAndroidModel> createContactList() {
        ArrayList<BaseAndroidModel> arrayContacts = new ArrayList<>();
        arrayContacts.add(new AndroidModel("Recycler View", ContactActivity.class));
        arrayContacts.add(new AndroidModel("Grid-Recycler-View", GridRvActivity.class));
        arrayContacts.add(new AndroidModel("Notification", NotificationActivity.class));
        arrayContacts.add(new AndroidModel(" Retrofit Api calling", MyApiActivity.class));
        arrayContacts.add(new AndroidModel(" OkHTTP Api calling", MyApiActivity.class));
        arrayContacts.add(new AndroidModel(" Fragment + Data passing", FragmentActivity.class));
        arrayContacts.add(new AndroidModel(" Fragment + callback", CallbackActivity.class));
        arrayContacts.add(new AndroidModel(" Permissions ", PermissionActivity.class));
        arrayContacts.add(new AndroidModel(" Student Mvc", StudentActivity.class));
        arrayContacts.add(new AndroidModel(" MVVM", SampleMVVM.class));
        arrayContacts.add(new AndroidModel(" MVVM + RETROFIT", ContactMVVMActivity.class));
        arrayContacts.add(new AndroidModel(" Room + APi calling", UserDetails.class));
        arrayContacts.add(new AndroidModel(" Http + API Calling", HttpConnectionActivity.class));
        arrayContacts.add(new AndroidModel(" Custom Font POC", FontActivity.class));
        arrayContacts.add(new AndroidModel(" Custom Font MVVM", FontActivity2.class));
        arrayContacts.add(new AndroidModel(" Dynamic Font-Download", DynamicFontActivity.class));
        arrayContacts.add(new BaseAndroidModel(" LOGIN-SIGNUP", new IntentLauncher(context, LoginActivity.class)));


        arrayContacts.add(new AndroidModel("Banner Using API + Containers + Dynamic", BannerActivity.class));
        arrayContacts.add(new AndroidModel(" Custom aspect Ratio control ON Camera 2", CameraActivity.class));
        arrayContacts.add(new AndroidModel(" Capture image in different Ratio", PIctureActivity.class));
        arrayContacts.add(new AndroidModel(" Dynamic Floating Action Button", FloatingActionButton.class));
        arrayContacts.add(new AndroidModel(" Dynamic Floating Button - Array ", DynamicFloatingButton.class));
        arrayContacts.add(new AndroidModel("New Jetpack Compose Feature", NewFeatureActivity.class));
        arrayContacts.add(new AndroidModel("Flip Book Poc", FlipBookActivity.class));


//      arrayContacts.add(new AndroidModel("Banner Using API", CallbackActivity.class));


        arrayContacts.add(new BaseAndroidModel("Say Hello", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Say Hello", Toast.LENGTH_SHORT).show();
            }
        }));

        return arrayContacts;
    }
}