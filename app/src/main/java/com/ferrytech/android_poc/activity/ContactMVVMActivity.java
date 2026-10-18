package com.ferrytech.android_poc.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.Interface.ApiInterface;
import com.ferrytech.android_poc.Mvvm.viewmodels.ContactViewModel;
import com.ferrytech.android_poc.Mvvm.viewmodels.StudentViewmodel;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.ContactAdapter2;
import com.ferrytech.android_poc.model.StudentModel;
import com.ferrytech.android_poc.model.User;

import java.util.ArrayList;
import java.util.List;

public class ContactMVVMActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ContactAdapter2 adapter;
    private ContactViewModel contactViewModel;
    private ApiInterface apiRepository;
    private List<User> arrContacts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sample_mvvmretrofit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.contact_rv);
         progressBar = findViewById(R.id.progress_bar);
        adapter = new ContactAdapter2(this, arrContacts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupViewModel();
        contactViewModel.apiCall();
    }
    private void setupViewModel() {

        contactViewModel = new ContactViewModel(apiRepository);
        contactViewModel.getContactList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setItems(users);
            }
        });
        contactViewModel.isLoading.observe(this, isLoading -> {

            if (isLoading != null && isLoading) {
                progressBar.setVisibility(VISIBLE);
                recyclerView.setVisibility(GONE);
            } else {
                progressBar.setVisibility(GONE);
                recyclerView.setVisibility(VISIBLE);
            }
        });
    }

    }
