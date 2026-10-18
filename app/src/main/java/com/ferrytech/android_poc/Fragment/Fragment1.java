package com.ferrytech.android_poc.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.callbacks.Callback;
import com.ferrytech.android_poc.callbacks.OnUserClickCallback;
import com.ferrytech.android_poc.model.User;

public class Fragment1 extends Fragment{

    TextView tv_name, tv_age, tv_city;
    String name, age, city;
    Button btn_edt_profile;
    Fragment2 fragment2;

    public Fragment1() {
        // Required empty public constructor
    }

    Callback callback = new Callback() {
        @Override
        public void onClick(User user) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Fragment1.onCreate");
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            age = bundle.getString("age");
            city = bundle.getString("city");

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("Fragment1.onViewCreated");
        System.out.println("name = " + name);
        System.out.println("age = " + age);
        System.out.println("city = " + city);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        tv_name = view.findViewById(R.id.tv_name);
        tv_age = view.findViewById(R.id.tv_age);
        tv_city = view.findViewById(R.id.tv_city);
        btn_edt_profile = view.findViewById(R.id.btn_edt_profile);
        System.out.println("Fragment1.onCreateView");

        tv_name.setText(name);
        tv_age.setText(age);
        tv_city.setText(city);

        fragment2 = new Fragment2();

        btn_edt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tv_name.getText().toString();
                String age = tv_age.getText().toString();
                String city = tv_city.getText().toString();

                Bundle bundle1 = new Bundle();
                bundle1.putString("name", name);
                bundle1.putString("age", age);
                bundle1.putString("city", city);
                fragment2.setArguments(bundle1);

                FragmentManager fm = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container_callback, fragment2);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Fragment1.onResume");
        fragment2.setCallback(new OnUserClickCallback() {
            @Override
            public void onUserUpdate(String name, String age, String city) {
                Fragment1.this.name = name;
                Fragment1.this.age = age;
                Fragment1.this.city = city;
            }
        });
    }
}