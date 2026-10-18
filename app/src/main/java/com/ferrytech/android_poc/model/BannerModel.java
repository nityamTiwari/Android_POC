package com.ferrytech.android_poc.model;

public class BannerModel {
    public String title;
    public String subtitle;
    public  String button1Title;
    public  String button2Title;

    public BannerModel(String title, String subtitle, String button1Title, String button2Title){
        this.title = title;
        this.subtitle = subtitle;
        this.button1Title = button1Title;
        this.button2Title = button2Title;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getButton1Title() {
        return button1Title;
    }

    public void setButton1Title(String button1Title) {
        this.button1Title = button1Title;
    }

    public String getButton2Title() {
        return button2Title;
    }

    public void setButton2Title(String button2Title) {
        this.button2Title = button2Title;
    }
}
