package com.ferrytech.android_poc.manager;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class FlipTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setCameraDistance(20000); // Adds depth to the 3D rotation

        if (position < -1) { // Page is way off-screen to the left
            page.setAlpha(0f);
        } else if (position <= 0) { // Flipping out to the left
            page.setAlpha(1f);
            page.setPivotX(page.getWidth());
            page.setRotationY(-90 * Math.abs(position));
        } else if (position <= 1) { // Flipping in from the right
            page.setAlpha(1f);
            page.setPivotX(0);
            page.setRotationY(90 * Math.abs(position));
        } else { // Page is way off-screen to the right
            page.setAlpha(0f);
        }
    }
}