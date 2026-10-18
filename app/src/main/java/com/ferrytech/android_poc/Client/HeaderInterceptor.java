package com.ferrytech.android_poc.Client;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request orignalRequest = chain.request();

        // add a hgeader
        Request newRequest = orignalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("App-Name", "AndroidPOC")
                .build();

        return chain.proceed(newRequest);
    }
}
