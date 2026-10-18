package com.ferrytech.android_poc.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ferrytech.android_poc.R;


public class OutputFragment extends Fragment {

    TextView tv_result;


    public OutputFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        System.out.println("OutputFragment.onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("OutputFragment.onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("OutputFragment.onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_output, container, false);

        tv_result = view.findViewById(R.id.tv_result);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            String age = bundle.getString("age");
            String city = bundle.getString("city");
            tv_result.setText("Name : " + name + "\nAge : " + age + "\nCity : " + city);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("OutputFragment.onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("OutputFragment.onActivityCreated");
    }
    @Override
    public void onStart() {
        super.onStart();
        System.out.println("OutputFragment.onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("OutputFragment.onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("OutputFragment.onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("OutputFragment.onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("OutputFragment.onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("OutputFragment.onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("OutputFragment.onDetach");
    }
}