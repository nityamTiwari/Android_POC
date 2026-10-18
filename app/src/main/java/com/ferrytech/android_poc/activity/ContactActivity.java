package com.ferrytech.android_poc.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.ContactAdapter;
import com.ferrytech.android_poc.model.Contact;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {


    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<Contact> arrContacts = new ArrayList<>();
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        System.out.println("ContactActivity.onCreate");
        setContentView(R.layout.activity_my_recycle_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        context = this;
        arrContacts = createContactList();
        adapter = new ContactAdapter(context, arrContacts);
        recyclerView = findViewById(R.id.contact_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);


    }

    private ArrayList<Contact> createContactList() {
        ArrayList<Contact> arrayContacts = new ArrayList<>();
        arrayContacts.add(new Contact("ayush", "25"));
        arrayContacts.add(new Contact("ayush", "25"));
        arrayContacts.add(new Contact("ayush", "25"));
        arrayContacts.add(new Contact("ayush", "25"));
        arrayContacts.add(new Contact("ayush", "25"));
        arrayContacts.add(new Contact("ayush", "25"));
        arrayContacts.add(new Contact("ayush", "25"));
        return arrayContacts;

    }
}