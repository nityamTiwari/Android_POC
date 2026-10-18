package com.ferrytech.android_poc.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.Fragment.StudentDialogFragment;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.StudentAdapter;
import com.ferrytech.android_poc.callbacks.StudentInterface;
import com.ferrytech.android_poc.model.StudentModel;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<StudentModel> arrContacts = new ArrayList<>();

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
        arrContacts = createContactList();

        adapter = new StudentAdapter(this, arrContacts, getSupportFragmentManager(), new StudentInterface() {
            @Override
            public void itemClick(StudentModel studentModel) {
                DialogFragment dialogFragment = new StudentDialogFragment(studentModel);
                dialogFragment.show(getSupportFragmentManager(), "StudentDialogFragment");

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<StudentModel> createContactList() {
        ArrayList<StudentModel> arrayList = new ArrayList<>();
        arrayList.add(new StudentModel("Name 1", "Roll No 1", "Class 1", R.drawable.ic_launcher_background, "Father Name 1", "Address 1"));
        arrayList.add(new StudentModel("Name 2", "Roll No 2", "Class 2", R.drawable.ic_launcher_background, "Father Name 2", "Address 2"));
        arrayList.add(new StudentModel("Name 3", "Roll No 3", "Class 3", R.drawable.ic_launcher_background, "Father Name 3", "Address 3"));
        arrayList.add(new StudentModel("Name 4", "Roll No 4", "Class 4", R.drawable.ic_launcher_background, "Father Name 4", "Address 4"));
        arrayList.add(new StudentModel("Name 5", "Roll No 5", "Class 5", R.drawable.ic_launcher_background, "Father Name 5", "Address 5"));
        arrayList.add(new StudentModel("Name 6", "Roll No 6", "Class 6", R.drawable.ic_launcher_background, "Father Name 6", "Address 6"));
        arrayList.add(new StudentModel("Name 7", "Roll No 7", "Class 7", R.drawable.ic_launcher_background, "Father Name 7", "Address 7"));
        arrayList.add(new StudentModel("Name 8", "Roll No 8", "Class 8", R.drawable.ic_launcher_background, "Father Name 8", "Address 8"));
        arrayList.add(new StudentModel("Name 9", "Roll No 9", "Class 9", R.drawable.ic_launcher_background, "Father Name 9", "Address 9"));
        arrayList.add(new StudentModel("Name 10", "Roll No 10", "Class 10", R.drawable.ic_launcher_background, "Father Name 10", "Address 10"));

        return arrayList;
    }
}