package com.ferrytech.android_poc.Interface;

import com.ferrytech.android_poc.ENTITY.UserEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("users.json?key=556000b0")
    Call<List<UserEntity>> getUsers();
}