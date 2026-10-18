package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.callbacks.Callback;
import com.ferrytech.android_poc.model.User;

import java.util.ArrayList;

public class MyApiAdapter extends RecyclerView.Adapter<MyApiAdapter.RecyclerViewHolder> {
    private Context context;
    private ArrayList<User> arrContacts;
    private LayoutInflater inflater;
    private Callback callback;

    public MyApiAdapter(Context context, ArrayList<User> arrContacts) {
        this.context = context;
        this.arrContacts = arrContacts;
        this.inflater = LayoutInflater.from(context);
    }

    public void setListner(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyApiAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_api_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyApiAdapter.RecyclerViewHolder holder, int position) {

        User model = arrContacts.get(position);
        holder.txtFstName.setText("First Name : " + model.getFirst_name());
        holder.txtLstName.setText("Second Name : " + model.getLast_name());
        holder.txtEmail.setText("Email : " + model.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != callback) {
                    callback.onClick(model);
                }
            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, UserDetailActivity.class);
//
//                intent.putExtra("user", model);
//                intent.putExtra("FirstName", model.getFirst_name());
//                intent.putExtra("LastName", model.getLast_name());
//                intent.putExtra("email", model.getEmail());
//
//                Bundle bundle = new Bundle();
//                bundle.putString("FirstName", model.getFirst_name());
//                bundle.putString("LastName", model.getLast_name());
//                bundle.putString("email", model.getEmail());
//
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return arrContacts.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtFstName, txtLstName, txtEmail;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFstName = itemView.findViewById(R.id.txt_fst_name);
            txtLstName = itemView.findViewById(R.id.txt_Sec_name);
            txtEmail = itemView.findViewById(R.id.txt_email);
        }
    }
}
