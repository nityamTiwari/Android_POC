package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.model.GridModel;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<GridModel> arrayList;

    public GridAdapter(Context context, ArrayList<GridModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_grid_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GridModel model = arrayList.get(position);
        System.out.println(" = " + model);
        holder.img_grid.setImageResource(model.getImg());
        holder.img_grid.setScaleType(model.getScaleType());
        holder.txt_title.setText(model.getTitle());
        holder.txt_scale_type.setText(model.getScaleType().name());

        holder.img_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, model.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_grid;
        TextView txt_title;
        TextView txt_scale_type;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_grid = itemView.findViewById(R.id.img_grid);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_scale_type = itemView.findViewById(R.id.img_scale_type);
        }
    }
}
