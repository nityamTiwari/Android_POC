package com.ferrytech.android_poc.Mvvm.viewmodels;

import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ferrytech.android_poc.Client.RetrofitClient;
import com.ferrytech.android_poc.Interface.ApiInterface;
import com.ferrytech.android_poc.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ContactViewModel extends ViewModel {
    private ApiInterface apiInterface;

    private MutableLiveData<List<User>> contactList = new MutableLiveData<>();
    private LiveData<List<User>> contactListLiveData = contactList;

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    public ContactViewModel(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }


    public LiveData<List<User>> getContactList() {
        return contactListLiveData;
    }


    public void apiCall() {
        _isLoading.setValue(true);
        if (apiInterface == null) {
            apiInterface = RetrofitClient.getInstance().create(ApiInterface.class);
        }
        
        Call<List<User>> call = apiInterface.getUsers();
        call.enqueue(new retrofit2.Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    contactList.postValue(response.body());
                } else {
                    contactList.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                _isLoading.setValue(true);
                contactList.postValue(null);
                System.out.println("onFailure: " + t.getMessage());
            }
        });
    }
}
