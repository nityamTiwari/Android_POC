package com.ferrytech.android_poc.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.adapter.GridAdapter;
import com.ferrytech.android_poc.model.GridModel;

import java.util.ArrayList;

public class GridRvActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private GridAdapter adapter;
    private ArrayList<GridModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grid_rv);
        System.out.println("GridRvActivity.onCreate");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rv_grid);
        context = this;
        arrayList = createGridList();
        adapter = new GridAdapter(context, arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<GridModel> createGridList() {
        ArrayList<GridModel> arrList = new ArrayList<>();
        arrList.add(new GridModel("Title 1", R.drawable.ic_launcher_background, ImageView.ScaleType.FIT_CENTER));
        arrList.add(new GridModel("Title 2", R.drawable.ic_launcher_background, ImageView.ScaleType.FIT_START));
        arrList.add(new GridModel("Title 3", R.drawable.ic_launcher_background, ImageView.ScaleType.FIT_END));
        arrList.add(new GridModel("Title 4", R.drawable.ic_launcher_background, ImageView.ScaleType.FIT_XY));
//        arrList.add(new GridModel("Title 5", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 6", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 7", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 8", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 9", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 10", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 11", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 12", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 13", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 14", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 15", R.drawable.ic_launcher_background));
//        arrList.add(new GridModel("Title 16", R.drawable.ic_launcher_background));
        return arrList;

    }

}