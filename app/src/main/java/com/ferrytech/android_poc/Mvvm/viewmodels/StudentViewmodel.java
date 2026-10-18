package com.ferrytech.android_poc.Mvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ferrytech.android_poc.Mvvm.repository.StudentRepository;
import com.ferrytech.android_poc.model.StudentModel;

import java.util.List;

public class StudentViewmodel extends ViewModel {
    private StudentRepository studentRepository;

    private MutableLiveData<List<StudentModel>> _studentList = new MutableLiveData<>();

    public LiveData<List<StudentModel>> studentList = _studentList;

    public StudentViewmodel(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }


    public void fetch(){
        List<StudentModel> studentModels = studentRepository.fetchList();
        _studentList.setValue(studentModels);
    }


}
