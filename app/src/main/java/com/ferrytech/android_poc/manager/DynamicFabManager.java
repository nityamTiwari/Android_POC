package com.ferrytech.android_poc.manager;

import android.content.Context;
import android.widget.FrameLayout;

import com.ferrytech.android_poc.model.ButtonGroupModel;
import com.ferrytech.android_poc.model.FloatingButtonModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DynamicFabManager {
    private Context context;
    private FrameLayout rootLayout;
    private final int FAB_SIZE = 140;
    public DynamicFabManager(Context context, FrameLayout rootLayout) {
        this.context = context;
        this.rootLayout = rootLayout;
    }

    public void renderGroups(List<ButtonGroupModel> groups) {

        int screenWidth = rootLayout.getWidth();
        int screenHeight = rootLayout.getHeight();

        for (ButtonGroupModel group : groups) {

            renderGroup(
                    group,
                    screenWidth,
                    screenHeight
            );
        }
    }

    private void renderGroup(ButtonGroupModel group, int screenWidth, int screenHeight) {

        List<FloatingButtonModel> buttons = group.buttons;
        int maxButtons = Math.min(buttons.size(), 3);

        for (int i = 0; i < maxButtons; i++) {

            FloatingButtonModel button = buttons.get(i);

            FloatingActionButton fab = new FloatingActionButton(context);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FAB_SIZE,
                            FAB_SIZE );

            fab.setLayoutParams(params);

            fab.setImageResource(
                    android.R.drawable.ic_input_add
            );

            Position position = calculatePosition(
                            group,
                            i,
                            screenWidth,
                            screenHeight
                    );

            fab.setX(position.x);

            fab.setY(position.y);

            rootLayout.addView(fab);
        }
    }

    private Position calculatePosition(ButtonGroupModel group, int index, int screenWidth, int screenHeight) {
        float x = 0;
        float y = 0;
        int move = index * (FAB_SIZE + group.spacing);
        switch (group.anchor) {

            case "TOP_LEFT":

                if (group.direction.equals("HORIZONTAL")) {

                    x = group.startX + move;
                    y = group.startY;

                } else {

                    x = group.startX;
                    y = group.startY + move;
                }

                break;

            case "TOP_RIGHT":

                if (group.direction.equals("HORIZONTAL")) {

                    x = screenWidth - group.startX - FAB_SIZE - move;
                    y = group.startY;

                } else {

                    x = screenWidth - group.startX - FAB_SIZE;
                    y = group.startY + move;
                }

                break;

            case "BOTTOM_LEFT":

                if (group.direction.equals("HORIZONTAL")) {

                    x = group.startX + move;
                    y = screenHeight - group.startY - FAB_SIZE;

                } else {

                    x = group.startX;
                    y = screenHeight - group.startY - FAB_SIZE - move;
                }

                break;

            case "BOTTOM_RIGHT":

                if (group.direction.equals("HORIZONTAL")) {

                    x = screenWidth - group.startX - FAB_SIZE - move;
                    y = screenHeight - group.startY - FAB_SIZE;

                } else {

                    x = screenWidth - group.startX - FAB_SIZE;
                    y = screenHeight - group.startY - FAB_SIZE - move;
                }
                break;
        }

        return new Position(x, y);
    }

    private static class Position {
        float x;
        float y;
        public Position(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}