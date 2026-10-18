package com.ferrytech.android_poc.model;

public class FloatingButtonItem {
    private int color;
    private Runnable action;

    public FloatingButtonItem(int color, Runnable action) {
        this.color = color;
        this.action = action;
    }

    public int getColor() {
        return color;
    }

    public Runnable getAction() {
        return action;
    }
}