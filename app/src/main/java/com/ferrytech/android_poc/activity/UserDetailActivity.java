package com.ferrytech.android_poc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferrytech.android_poc.R;

public class UserDetailActivity extends AppCompatActivity {

    private TextView txt_name, txt_age, txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_name = findViewById(R.id.txt_name);
        txt_age = findViewById(R.id.txt_age);
        txt_email = findViewById(R.id.txt_email);

        Intent intent = getIntent();

//      User user = intent.getSerializableExtra("user", User.class);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            txt_name.setText("FirstName : " + bundle.getString("FirstName"));
            txt_age.setText("LastName : " + bundle.getString("LastName"));
            txt_email.setText("Email : " + bundle.getString("email"));
        }

//        String firstName = intent.getStringExtra("FirstName");
//        String lastName = intent.getStringExtra("LastName");
//        String email = intent.getStringExtra("email");

       /// for serilizable
//        txt_name.setText("FirstName : " + user.getFirst_name());
//        txt_age.setText("LastName : " + user.getLast_name());
//        txt_email.setText("Email : " + user.getEmail());

    }
}