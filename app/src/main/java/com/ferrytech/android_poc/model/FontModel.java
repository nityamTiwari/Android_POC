package com.ferrytech.android_poc.model;

public class FontModel {
    public int fontRes;
    public int style;

    public FontModel(int fontRes, int style) {
        this.fontRes = fontRes;
        this.style = style;
    }

    public int getFontRes() {
        return fontRes;
    }

    public void setFontRes(int fontRes) {
        this.fontRes = fontRes;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
