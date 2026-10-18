package com.ferrytech.android_poc.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.model.StudentModel;

public class StudentDialogFragment extends DialogFragment {
    StudentModel student;


    public StudentDialogFragment(StudentModel student) {
        this.student = student;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_layout, null);

        Button btnClose = dialog.findViewById(R.id.btn_close);
        TextView tvName = dialog.findViewById(R.id.tv_student_name);
        tvName.setText(student.getName());
        TextView tvRollNo = dialog.findViewById(R.id.tv_student_rollno);
        tvRollNo.setText(student.getRollno());
        TextView tvStudentFatherName = dialog.findViewById(R.id.tv_father_name);
        tvStudentFatherName.setText(student.getFatherName());
        TextView tvStudentClass = dialog.findViewById(R.id.tv_student_class);
        tvStudentClass.setText(student.getStd());
        TextView tvAddress = dialog.findViewById(R.id.tv_student_address);
        tvAddress.setText(student.getAddress());


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(dialog);
        return builder.create();
    }

}
