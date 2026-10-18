package com.ferrytech.android_poc.model;

import android.widget.ImageView;

public class GridModel {
    String title;
    int Img;
    ImageView.ScaleType scaleType;

    public GridModel(String title, int img, ImageView.ScaleType scaleType) {
        this.title = title;
        this.Img = img;
        this.scaleType = scaleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return Img;
    }

    public void setImg(int img) {
        Img = img;
    }

    @Override
    public String toString() {
        return "GridModel{" +
                "title='" + title + '\'' +
                ", Img=" + Img +
                '}';
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;

    }
}
