package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.callbacks.StudentInterface;
import com.ferrytech.android_poc.model.StudentModel;

import java.util.List;

public class StudentAdapter2 extends RecyclerView.Adapter<StudentAdapter2.ViewHolder> {
    private Context context;
    private List<StudentModel> studentModels;
    private LayoutInflater inflater;
    private StudentInterface studentInterface;


    public StudentAdapter2(Context context, StudentInterface studentInterface) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.studentInterface = studentInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("StudentAdapter.onBindViewHolder");
        StudentModel student = studentModels.get(position);
        holder.tvName.setText(student.getName());
        holder.tvRollNo.setText(student.getRollno());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentInterface.itemClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentModels.size();
    }

    public void setItems(List<StudentModel> studentModels) {
        this.studentModels = studentModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRollNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRollNo = itemView.findViewById(R.id.tvRollNo);
        }
    }


}
