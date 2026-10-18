package com.ferrytech.android_poc.Mvvm.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ferrytech.android_poc.ENTITY.UserEntity;
import com.ferrytech.android_poc.Mvvm.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<UserEntity>> users;

    public UserViewModel(@NonNull Application application) {
        super(application);

        repository = new UserRepository(application);
        users = repository.getUsers();
    }

    public LiveData<List<UserEntity>> getUsers(){
        return users;
    }
}