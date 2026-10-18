package com.ferrytech.android_poc.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.Utils.JsonUtils;
import com.ferrytech.android_poc.manager.DynamicFabManager;
import com.ferrytech.android_poc.model.ButtonGroupModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class FloatingActionButton extends AppCompatActivity {

    FrameLayout rootLayout;
    public FloatingActionButton() {
        // Required empty public constructor for Android
    }
    public FloatingActionButton(Context context) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_floating_action_button);

        rootLayout = findViewById(R.id.rootLayout);  // add framelyout as rootlayout
        String json = JsonUtils.getJsonFromAssets(this, "button.json");  // read json from assets
        Type type = new TypeToken<List<ButtonGroupModel>>(){}.getType();  // tell gson to read take json using token converter... means json now easily find the list is of floatingbuttonmodel

        List<ButtonGroupModel> buttons = new Gson().fromJson(json,type); // here cpnvert json to java object

        rootLayout.post(() -> {

            DynamicFabManager manager = new DynamicFabManager(this, rootLayout);

            manager.renderGroups(buttons);
        });
    }
}