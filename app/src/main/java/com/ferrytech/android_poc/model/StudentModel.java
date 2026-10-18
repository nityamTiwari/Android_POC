package com.ferrytech.android_poc.model;

public class StudentModel {
    private String name;
    private String rollno;
    private String std;
    private int image;
    private String fatherName;
    private String address;

    public StudentModel(String name, String rollno, String std, int image, String fatherName, String address) {
        this.name = name;
        this.rollno = rollno;
        this.std = std;
        this.image = image;
        this.fatherName = fatherName;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
