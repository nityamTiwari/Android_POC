package com.ferrytech.android_poc.model;

import java.util.List;

public class ButtonGroupModel {

    public String groupId;

    public String anchor;
    // TOP_LEFT
    // TOP_RIGHT
    // BOTTOM_LEFT
    // BOTTOM_RIGHT

    public String direction;
    // HORIZONTAL
    // VERTICAL

    public int startX;

    public int startY;

    public int spacing;

    public List<FloatingButtonModel> buttons;
}