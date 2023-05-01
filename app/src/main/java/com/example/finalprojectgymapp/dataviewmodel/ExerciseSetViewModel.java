package com.example.finalprojectgymapp.dataviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finalprojectgymapp.model.ExerciseSet;
import com.example.finalprojectgymapp.repository.ExerciseSetRepository;

import java.util.List;

public class ExerciseSetViewModel extends AndroidViewModel {
    private ExerciseSetRepository repository;

    public ExerciseSetViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseSetRepository(application);
    }

    public void insert(ExerciseSet exerciseSet) {
        repository.insert(exerciseSet);
    }

    public void update(ExerciseSet exerciseSet) {
        repository.update(exerciseSet);
    }

    public void delete(ExerciseSet exerciseSet) {
        repository.delete(exerciseSet);
    }

    public void deleteAllExerciseSets() {
        repository.deleteAllExerciseSets();
    }

    public LiveData<List<ExerciseSet>> getAllExerciseSets() {
        return repository.getAllExerciseSets();
    }

    public LiveData<List<ExerciseSet>> getExerciseSetsByExerciseLogId(int exerciseLogId) {
        return repository.getExerciseSetsByExerciseLogId(exerciseLogId);
    }

    public List<ExerciseSet> getListExerciseSetsByExerciseLogId(int exerciseLogId) {
        return repository.getListExerciseSetsByExerciseLogId(exerciseLogId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Shutdown executor when ViewModel is cleared
        repository.getExecutor().shutdown();
    }
}