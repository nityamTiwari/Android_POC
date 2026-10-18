package com.ferrytech.android_poc.Client;

import com.ferrytech.android_poc.Interface.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientProvider {

    private static final String BASE_URL = "https://my.api.mockaroo.com/";
    private static Retrofit retrofit;

    public static ApiService getApi(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
