package com.ferrytech.android_poc.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.Fragment.StudentDialogFragment;
import com.ferrytech.android_poc.Mvvm.repository.StudentRepository;
import com.ferrytech.android_poc.Mvvm.viewmodels.StudentViewmodel;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.StudentAdapter2;
import com.ferrytech.android_poc.callbacks.StudentInterface;
import com.ferrytech.android_poc.model.StudentModel;

import java.util.List;

public class SampleMVVM extends AppCompatActivity {

    private StudentRepository repository;
    private RecyclerView recyclerView;
    private StudentAdapter2 adapter;
    private StudentViewmodel studentViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.student_rv);
        adapter = new StudentAdapter2(this, new StudentInterface() {

            @Override
            public void itemClick(StudentModel studentModel) {
                DialogFragment dialogFragment = new StudentDialogFragment(studentModel);
                dialogFragment.show(getSupportFragmentManager(), "StudentDialogFragment");
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        repository = new StudentRepository();
        setupViewModel();
        studentViewmodel.fetch();
    }

    private void setupViewModel() {
        studentViewmodel = new StudentViewmodel(repository);
        studentViewmodel.studentList.observe(this, new Observer<List<StudentModel>>() {
            @Override
            public void onChanged(List<StudentModel> studentModels) {
                adapter.setItems(studentModels);
            }
        });
    }
}