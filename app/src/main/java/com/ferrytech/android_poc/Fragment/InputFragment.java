package com.ferrytech.android_poc.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.ferrytech.android_poc.R;

public class InputFragment extends Fragment {
    EditText tv_name, tv_age, tv_city;
    Button btn_submit;
    String name, age, city;


    public InputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        System.out.println("InputFragment.onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("InputFragment.onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        tv_name = view.findViewById(R.id.tv_name);
        tv_age = view.findViewById(R.id.tv_age);
        tv_city = view.findViewById(R.id.tv_city);

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            age = bundle.getString("age");
            city = bundle.getString("city");
            tv_name.setText(  name);
            tv_age.setText( age);
            tv_city.setText( city);
        }

//        edt_name = view.findViewById(R.id.edt_name);
//        edt_name.setText(name);
//        edt_age = view.findViewById(R.id.edt_age);
//        edt_city = view.findViewById(R.id.edt_city);
          btn_submit = view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(view1 -> {

            String name = tv_name.getText().toString();
            String age = tv_age.getText().toString();
            String city = tv_city.getText().toString();

            Bundle bundle1 = new Bundle();
            bundle1.putString("name", name);
            bundle1.putString("age", age);
            bundle1.putString("city", city);


            OutputFragment outputFragment = new OutputFragment();
            outputFragment.setArguments(bundle1);

            FragmentManager fm = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragment_container, outputFragment);
            transaction.addToBackStack(null);
            transaction.commit();


        });
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("InputFragment.onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("InputFragment.onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("InputFragment.onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("InputFragment.onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("InputFragment.onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("InputFragment.onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("InputFragment.onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("InputFragment.onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("InputFragment.onDetach");
    }
}
