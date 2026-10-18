package com.ferrytech.android_poc.Mvvm.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.ferrytech.android_poc.Client.ApiClientProvider;
import com.ferrytech.android_poc.DAO.UserDao;
import com.ferrytech.android_poc.ENTITY.UserEntity;
import com.ferrytech.android_poc.Utils.NetworkUtils;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<UserEntity>> users;
    private Context context;

    public UserRepository(Context context){
        this.context = context;

        this.context = context;

        com.ferrytech.android_poc.DATABASE.AppDatabase db = com.ferrytech.android_poc.DATABASE.AppDatabase.getInstance(context);
        userDao = db.userDao();

        // Room LiveData
        users = userDao.getUsers();
    }

    public LiveData<List<UserEntity>> getUsers(){

        if(NetworkUtils.isInternetAvailable(context)){

            ApiClientProvider.getApi().getUsers().enqueue(new Callback<List<UserEntity>>() {
                @Override
                public void onResponse(Call<List<UserEntity>> call, Response<List<UserEntity>> response) {

                    if(response.isSuccessful() && response.body()!=null){

                        Executors.newSingleThreadExecutor().execute(() -> {
                            userDao.insertUsers(response.body());
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<UserEntity>> call, Throwable t) {

                }
            });
        }

        return users;
    }
}