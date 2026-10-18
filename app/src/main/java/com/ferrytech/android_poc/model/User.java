package com.ferrytech.android_poc.model;

import android.os.Parcel;

import java.io.Serializable;

public class User  {

    private String first_name;
    private String last_name;
    private String email;

    // Constructor used for un-marshalling (reading from Parcel)
    protected User(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    // In the vast majority of cases, you can simply return 0
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    // This is where you write the object's properties to the Parcel
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        // The order here must match the order in the protected constructor
//        dest.writeString(first_name);
//        dest.writeString(last_name);
//        dest.writeString(email);
//    }
//
//    // The static CREATOR field is required for the system to create instances of your Parcelable class
//    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };
}
