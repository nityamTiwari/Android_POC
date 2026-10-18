package com.ferrytech.android_poc.Interface;

import com.ferrytech.android_poc.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    //https://my.api.mockaroo.com/users.json?key=556000b0
    @GET("users.json?key=556000b0")
    Call<List<User>> getUsers();

}
