package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.Interface.ApiInterface;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.model.Contact;
import com.ferrytech.android_poc.model.User;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter2 extends RecyclerView.Adapter<ContactAdapter2.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<User> arrContacts;
    private ApiInterface contactInterface;


    public ContactAdapter2(Context context, List<User> arrContacts) {
        this.context = context;
        this.arrContacts = arrContacts;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ContactAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contact2_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter2.ViewHolder holder, int position) {
        User model = arrContacts.get(position);
        System.out.println("model = " + model);
        holder.txtFstName.setText("First Name : " + model.getFirst_name());
        holder.txtLstName.setText("Second Name : " + model.getLast_name());
        holder.txtEmail.setText("Email : " + model.getEmail());

    }

    @Override
    public int getItemCount() {
        System.out.println("arrContacts = " + arrContacts.size());
        if (arrContacts == null){
            return 0;
        }else {
            return arrContacts.size();
        }
    }

    public void setItems(List<User> users) {
        this.arrContacts = users;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFstName, txtLstName, txtEmail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFstName = itemView.findViewById(R.id.txt_fst_name);
            txtLstName = itemView.findViewById(R.id.txt_Sec_name);
            txtEmail = itemView.findViewById(R.id.txt_email);

        }
    }
}
