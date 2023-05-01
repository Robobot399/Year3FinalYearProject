package com.example.finalprojectgymapp.dataviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ExerciseDbViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public ExerciseDbViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ExerciseDbViewModel.class)) {
            return (T) new ExerciseDbViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}