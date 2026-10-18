package com.ferrytech.android_poc.model;

public class Contact {
    public String naame;
    public String age;

    public Contact(String naame, String age) {
        this.naame = naame;
        this.age = age;
    }

    public String getNaame() {
        return naame;
    }

    public void setNaame() {
        this.naame = naame;
    }

    public String getAge() {
        return age;
    }

    public void setAge() {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "naame='" + naame + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
