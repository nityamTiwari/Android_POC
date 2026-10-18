package com.ferrytech.android_poc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.Client.RetrofitClient;
import com.ferrytech.android_poc.Interface.ApiInterface;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.MyApiAdapter;
import com.ferrytech.android_poc.callbacks.Callback;
import com.ferrytech.android_poc.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyApiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MyApiAdapter adapter;
    private ArrayList<User> arrContacts = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_api);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;
        recyclerView = findViewById(R.id.api_rv);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Callback callback= new Callback() {
            @Override
            public void onClick(User user) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FirstName", user.getFirst_name());
                bundle.putString("LastName", user.getLast_name());
                bundle.putString("email", user.getEmail());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };


        adapter = new MyApiAdapter(context, arrContacts);
        adapter.setListner(callback);
        recyclerView.setAdapter(adapter);

        fetchUsers();

    }

    private void fetchUsers() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        long start = System.currentTimeMillis();

        Retrofit retrofit = RetrofitClient.getInstance();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<User>> call = apiInterface.getUsers();

        call.enqueue(new retrofit2.Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
//                System.out.println("Retrofit.currentThread().getName() = " + Thread.currentThread().getName());
                if (response.isSuccessful() && response.body() != null) {
                    long end = System.currentTimeMillis();
                    arrContacts.clear();
                    arrContacts.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    System.out.println("end-start = " + (end - start));
                } else {
                    Toast.makeText(context, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
//                Log.e("MyApiActivity", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
