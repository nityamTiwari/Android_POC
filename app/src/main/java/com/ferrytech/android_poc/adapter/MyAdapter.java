package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.model.BaseAndroidModel;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerViewHolder> {

    private final Context context;
    private final ArrayList<BaseAndroidModel> list;
    private final LayoutInflater inflater;

    public MyAdapter(Context context, ArrayList<BaseAndroidModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.RecyclerViewHolder holder, int position) {
        BaseAndroidModel model = list.get(position);

        holder.txtItem.setText(model.name);

        holder.txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtItem;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItem = itemView.findViewById(R.id.txt_item);
        }
    }
}

