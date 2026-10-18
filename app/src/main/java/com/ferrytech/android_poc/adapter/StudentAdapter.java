package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.callbacks.StudentInterface;
import com.ferrytech.android_poc.model.StudentModel;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StudentModel> arrContacts;
    private LayoutInflater inflater;
    private FragmentManager fm;
    private StudentInterface studentInterface;


    public StudentAdapter(Context context, ArrayList<StudentModel> arrContacts, FragmentManager fm, StudentInterface studentInterface) {
        System.out.println("StudentAdapter.StudentAdapter");
        this.context = context;
        this.arrContacts = arrContacts;
        this.inflater = LayoutInflater.from(context);
        this.fm = fm;
        this.studentInterface = studentInterface;
    }


    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_student, parent, false);
        return new StudentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        System.out.println("StudentAdapter.onBindViewHolder");
        StudentModel student = arrContacts.get(position);
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
        return arrContacts.size();
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
