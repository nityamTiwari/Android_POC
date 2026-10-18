package com.ferrytech.android_poc.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class AndroidModel extends BaseAndroidModel {
    public Class<?> className;

    public AndroidModel(String name, Class<?> className) {
        super(name, null);
        this.className = className;
    }

    @Override
    public void onClick(View v) {
//        super.onClick(v);
        if (null == v) {
            System.out.println("AndroidModel.onClick v = " + v);
            return;
        }
        Context context = v.getContext();
        if (null == context) {
            System.out.println("AndroidModel.onClick context = " + context);
            return;
        }
        Intent intent = new Intent(context, this.className);
        context.startActivity(intent);
    }
}
