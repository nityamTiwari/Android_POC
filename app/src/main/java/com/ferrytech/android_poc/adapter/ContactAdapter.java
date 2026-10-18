package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Contact> arrContacts;

    public ContactAdapter(Context context, ArrayList<Contact> arrContacts) {
        this.context = context;
        this.arrContacts = arrContacts;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        Contact model = arrContacts.get(position);
        System.out.println("model = " + model);
        holder.txt_name.setText(model.naame);
        holder.txt_age.setText(model.age);

    }

    @Override
    public int getItemCount() {
        System.out.println("arrContacts = " + arrContacts.size());
        return arrContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_employee;
        TextView txt_name;
        TextView txt_age;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_employee = itemView.findViewById(R.id.img_employee);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_age = itemView.findViewById(R.id.txt_age);
        }
    }
}
