package com.example.finalprojectgymapp.dataviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finalprojectgymapp.model.ExerciseItem;
import com.example.finalprojectgymapp.repository.ExerciseItemRepository;

import java.util.List;

public class ExerciseItemViewModel extends AndroidViewModel {
    private ExerciseItemRepository repository;
    private LiveData<List<ExerciseItem>> allExerciseItems;

    public ExerciseItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseItemRepository(application);
        allExerciseItems = repository.getAllExerciseItems();
    }

    public void insert(ExerciseItem exerciseItem) {
        repository.insert(exerciseItem);
    }

    public void update(ExerciseItem exerciseItem) {
        repository.update(exerciseItem);
    }

    public void delete(ExerciseItem exerciseItem) {
        repository.delete(exerciseItem);
    }

    public void deleteAllExerciseItems() {
        repository.deleteAllExerciseItems();
    }

    public LiveData<List<ExerciseItem>> getAllExerciseItems() {
        return allExerciseItems;
    }

    public LiveData<List<ExerciseItem>> getExerciseItemsByWorkoutId(int workoutId) {
        return repository.getExerciseItemsByWorkoutId(workoutId);
    }
}
