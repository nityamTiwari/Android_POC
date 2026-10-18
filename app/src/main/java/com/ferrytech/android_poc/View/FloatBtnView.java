package com.ferrytech.android_poc.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.constant.FloatingPosition;
import com.ferrytech.android_poc.model.FloatingButtonItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("ViewConstructor")
public class FloatBtnView extends FrameLayout {

    private final FloatingPosition position;

    private final List<FloatingButtonItem> horizontalButtons;
    private final List<FloatingButtonItem> verticalButtons;

    private final int pL;
    private final int pT;
    private final int pR;
    private final int pB;
    private final int spacing;

    private final int buttonWidth = dpToPx(60);
    private final int buttonHeight = dpToPx(60);

    public FloatBtnView(
            Context context,
            FloatingPosition position,
            List<FloatingButtonItem> horizontalButtons,
            List<FloatingButtonItem> verticalButtons,
            int pL,
            int pT,
            int pR,
            int pB,
            int spacing
    ) {
        super(context);

        this.position = position;

        this.horizontalButtons = horizontalButtons;
        this.verticalButtons = verticalButtons;

        this.pL = pL;
        this.pT = pT;
        this.pR = pR;
        this.pB = pB;

        this.spacing = spacing;

        setupView();
    }

    private void setupView() {

        FrameLayout parentLayout = new FrameLayout(getContext());

        parentLayout.setClipChildren(false);
        parentLayout.setClipToPadding(false);

        parentLayout.setPadding(
                dpToPx(pL),
                dpToPx(pT),
                dpToPx(pR),
                dpToPx(pB)
        );

        List<FloatingButtonItem> finalHorizontalButtons = getFinalHorizontalButtons();

        List<FloatingButtonItem> finalVerticalButtons = getFinalVerticalButtons();

        // Horizontal Section
        if (!finalHorizontalButtons.isEmpty()) {

            LinearLayout horizontal = createHorizontalSection(finalHorizontalButtons);

            FrameLayout.LayoutParams hParams = new FrameLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    );

            hParams.gravity = getOverlayGravity();

            parentLayout.addView(horizontal, hParams);
        }

        // Vertical Section
        if (!finalVerticalButtons.isEmpty()) {

            LinearLayout vertical = createVerticalSection(finalVerticalButtons);

            FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    );

            vParams.gravity = getOverlayGravity();

            // spacing between horizontal and vertical
            if (!finalHorizontalButtons.isEmpty()) {

                if (isBottom()) {
                    vParams.bottomMargin = dpToPx(80);
                } else {
                    vParams.topMargin = dpToPx(80);
                }
            }

            parentLayout.addView(vertical, vParams);
        }

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );

        addView(parentLayout, params);
    }

    // Horizontal Section
    private LinearLayout createHorizontalSection(List<FloatingButtonItem> buttons) {

        LinearLayout layout = new LinearLayout(getContext());

//      layout.setBackgroundColor(Color.YELLOW);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(getInnerGravity());

        layout.setClipToPadding(false);
        layout.setClipChildren(false);

        List<FloatingButtonItem> renderButtons = getHorizontalButtonsToRender(buttons);

        for (int i = 0; i < renderButtons.size(); i++) {

            View button = createButton(renderButtons.get(i));

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            buttonWidth,
                            buttonHeight
                    );

            if (i != renderButtons.size() - 1) {
                params.setMarginEnd(dpToPx(spacing));
            }

            layout.addView(button, params);
        }

        return layout;
    }

    // Vertical Section
    private LinearLayout createVerticalSection(List<FloatingButtonItem> buttons) {

        LinearLayout layout = new LinearLayout(getContext());
//        layout.setBackgroundColor(Color.RED);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.setGravity(getInnerGravity());
        layout.setClipToPadding(false);
        layout.setClipChildren(false);

        List<FloatingButtonItem> renderButtons = getVerticalButtonsToRender(buttons);

        for (int i = 0; i < renderButtons.size(); i++) {

            View button = createButton(renderButtons.get(i));

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            buttonWidth,
                            buttonHeight
                    );

            if (i != renderButtons.size() - 1) {
                params.bottomMargin = dpToPx(spacing);
            }

            layout.addView(button, params);
        }

        return layout;
    }

    // Button
    private View createButton(FloatingButtonItem item) {

        FrameLayout container = new FrameLayout(getContext());

        container.setClipChildren(false);
        container.setClipToPadding(false);

        container.setForegroundGravity(Gravity.CENTER);

        LinearLayout.LayoutParams containerParams =
                new LinearLayout.LayoutParams(
                        dpToPx(80),
                        dpToPx(80)
                );

        container.setLayoutParams(containerParams);

        // =========================================
        // GIF BACKGROUND
        // =========================================

        AppCompatImageView gifView = new AppCompatImageView(getContext());

        FrameLayout.LayoutParams gifParams = new FrameLayout.LayoutParams(
                dpToPx(82),
                dpToPx(82)
        );

        gifParams.gravity = Gravity.CENTER;
        gifView.setLayoutParams(gifParams);

        gifView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(getContext())
                .asGif()
                .load(R.drawable.a)
                .into(gifView);

        // =========================================
        // MAIN BUTTON ICON
        // =========================================

        AppCompatImageView iconView = new AppCompatImageView(getContext());

        FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(
                dpToPx(52),
                dpToPx(52)
        );

        iconParams.gravity = Gravity.CENTER;

        iconView.setLayoutParams(iconParams);

        iconView.setImageResource(R.drawable.img);

        iconView.setPadding(
                dpToPx(10),
                dpToPx(10),
                dpToPx(10),
                dpToPx(10)
        );

        // =========================================
        // PINK CIRCLE BACKGROUND
        // =========================================

        GradientDrawable drawable = new GradientDrawable();

        drawable.setCornerRadius(dpToPx(50));
//        drawable.setColor(Color.parseColor("#E91E63"));
        drawable.setColor(Color.BLACK);

        iconView.setBackground(drawable);

        iconView.setElevation(dpToPx(8));



        // =========================================
        // CLICK
        // =========================================

        container.setOnClickListener(v -> {

            if (item.getAction() != null) {
                item.getAction().run();
            }
        });

        container.addView(gifView);

        container.addView(iconView);

        return container;
    }

    // Rules
    private List<FloatingButtonItem> getFinalHorizontalButtons() {

        return horizontalButtons.subList(
                0,
                Math.min(horizontalButtons.size(), 4)
        );
    }

    private List<FloatingButtonItem> getFinalVerticalButtons() {

        int max = horizontalButtons.isEmpty() ? 4 : 3;

        return verticalButtons.subList(
                0,
                Math.min(verticalButtons.size(), max)
        );
    }

    //  Priority Logic
    private List<FloatingButtonItem> getHorizontalButtonsToRender(List<FloatingButtonItem> buttons) {

        List<FloatingButtonItem> list = new ArrayList<>(buttons);

        if (isTrailing()) {
            Collections.reverse(list);
        }

        return list;
    }

    private List<FloatingButtonItem> getVerticalButtonsToRender(List<FloatingButtonItem> buttons) {

        List<FloatingButtonItem> list = new ArrayList<>(buttons);

        if (isBottom()) {
            Collections.reverse(list);
        }

        return list;
    }

    // Helpers
    private boolean isBottom() {

        return position == FloatingPosition.BOTTOM_LEFT ||
                position == FloatingPosition.BOTTOM_RIGHT;
    }

    private boolean isTrailing() {

        return position == FloatingPosition.TOP_RIGHT ||
                position == FloatingPosition.BOTTOM_RIGHT;
    }

    private int getOverlayGravity() {

        switch (position) {

            case TOP_LEFT:
                return Gravity.TOP | Gravity.START;

            case TOP_RIGHT:
                return Gravity.TOP | Gravity.END;

            case BOTTOM_LEFT:
                return Gravity.BOTTOM | Gravity.START;

            default:
                return Gravity.BOTTOM | Gravity.END;
        }
    }

    private int getInnerGravity() {

        switch (position) {

            case TOP_RIGHT:
                return Gravity.TOP | Gravity.END;

            case BOTTOM_LEFT:
                return Gravity.BOTTOM | Gravity.START;

            case BOTTOM_RIGHT:
                return Gravity.BOTTOM | Gravity.END;

            default:
                return Gravity.TOP | Gravity.START;
        }
    }

    private int dpToPx(int dp) {

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
