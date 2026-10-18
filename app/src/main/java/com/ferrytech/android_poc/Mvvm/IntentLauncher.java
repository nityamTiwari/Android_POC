package com.ferrytech.android_poc.Mvvm;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class IntentLauncher implements View.OnClickListener {
    private Context mContext;
    private Class className;

    public IntentLauncher(Context mContext, Class className) {
        this.mContext = mContext;
        this.className = className;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, className);
        mContext.startActivity(intent);
    }
}
