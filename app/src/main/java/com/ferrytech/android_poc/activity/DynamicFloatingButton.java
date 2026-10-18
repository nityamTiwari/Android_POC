package com.ferrytech.android_poc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ferrytech.android_poc.View.FloatBtnView;
import com.ferrytech.android_poc.constant.FloatingPosition;
import com.ferrytech.android_poc.model.FloatingButtonItem;

import java.util.ArrayList;
import java.util.List;

public class DynamicFloatingButton extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.YELLOW);

        setContentView(root);

        // TOP LEFT
        List<FloatingButtonItem> topLeftVertical = new ArrayList<>();

        topLeftVertical.add(new FloatingButtonItem(
                        Color.BLUE,
                        () -> System.out.println("TL 2")
                )
        );

        //top-left horizontal
        List<FloatingButtonItem> topLeftHorizontal =
                new ArrayList<>();

        topLeftHorizontal.add(
                new FloatingButtonItem(
                        Color.RED,
                        () -> System.out.println("TL H1")
                )
        );

        topLeftHorizontal.add(
                new FloatingButtonItem(
                        Color.BLUE,
                        () -> System.out.println("TL H2")
                )
        );


        FloatBtnView topLeft = new FloatBtnView(
                this,
                FloatingPosition.TOP_LEFT,
                topLeftHorizontal,
                topLeftVertical,
                24,
                48,
                24,
                24,
                12
        );

        root.addView(topLeft);
        //top-right

        List<FloatingButtonItem> topRightHorizontal = new ArrayList<>();

        topRightHorizontal.add(
                new FloatingButtonItem(
                        Color.RED,
                        () -> System.out.println("TR 1")
                )
        );

        topRightHorizontal.add(
                new FloatingButtonItem(
                        Color.YELLOW,
                        () -> System.out.println("TR 2")
                )
        );

        FloatBtnView topRight = new FloatBtnView(
                this,
                FloatingPosition.TOP_RIGHT,
                topRightHorizontal,
                new ArrayList<>(),
                24,
                48,
                24,
                24,
                12
        );

        root.addView(topRight);

        // BOTTOM RIGHT

        List<FloatingButtonItem> vertical2 =
                new ArrayList<>();

        vertical2.add(
                new FloatingButtonItem(
                        Color.BLUE,
                        () -> System.out.println("Bottom Button")
                )
        );

        FloatBtnView bottomRight = new FloatBtnView(
                this,
                FloatingPosition.BOTTOM_RIGHT,
                new ArrayList<>(),
                vertical2,
                24,
                24,
                24,
                72,
                12
        );

        root.addView(bottomRight);

        //bottom left
        List<FloatingButtonItem> bottomLeftVertical =
                new ArrayList<>();

        bottomLeftVertical.add(
                new FloatingButtonItem(
                        Color.CYAN,
                        () -> System.out.println("BL 1")
                )
        );

        FloatBtnView bottomLeft = new FloatBtnView(
                this,
                FloatingPosition.BOTTOM_LEFT,
                new ArrayList<>(),
                bottomLeftVertical,
                24,
                24,
                24,
                72,
                12
        );

        root.addView(bottomLeft);
    }
}
