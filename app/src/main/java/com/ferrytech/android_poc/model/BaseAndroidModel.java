package com.ferrytech.android_poc.model;

import android.view.View;

public class BaseAndroidModel {
    public final String name;
    public final View.OnClickListener listener;

    public BaseAndroidModel(String name, View.OnClickListener listener) {
        this.name = name;
        this.listener = listener;
    }

    public void onClick(View v) {
        if (null != listener) {
            listener.onClick(v);
        }
    }
}
