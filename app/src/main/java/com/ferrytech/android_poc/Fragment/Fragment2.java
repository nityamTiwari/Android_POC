package com.ferrytech.android_poc.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.callbacks.OnUserClickCallback;
import com.ferrytech.android_poc.model.User;


public class Fragment2 extends Fragment {

    EditText edt_name, edt_age, edt_city;
    String name, age, city;
    Button btn_save_profile;
    private OnUserClickCallback callback;


    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_2, container, false);
        edt_name = view.findViewById(R.id.edt_name);
        edt_age = view.findViewById(R.id.edt_age);
        edt_city = view.findViewById(R.id.edt_city);
        btn_save_profile = view.findViewById(R.id.btn_save_profile);


        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            age = bundle.getString("age");
            city = bundle.getString("city");
            edt_name.setText(name);
            edt_age.setText(age);
            edt_city.setText(city);
        }

        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = edt_name.getText().toString();
                age = edt_age.getText().toString();
                city = edt_city.getText().toString();


                callback.onUserUpdate(name, age, city);
                requireActivity().getSupportFragmentManager().popBackStack();

            }
        });

        return view;
    }

    public void setCallback(OnUserClickCallback callback) {
        this.callback = callback;
    }
}