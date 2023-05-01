package com.example.finalprojectgymapp.dataviewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.ExerciseItem;
import com.example.finalprojectgymapp.model.ExerciseItemWithExercise;
import com.example.finalprojectgymapp.repository.ExerciseDbRepository;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDbViewModel extends AndroidViewModel {

    private ExerciseDbRepository exerciseDbRepository;
    private static final String TAG = "ExerciseDbViewModel";

    public ExerciseDbViewModel(@NonNull Application application) {
        super(application);
        exerciseDbRepository = ExerciseDbRepository.getInstance(application);
    }

    public LiveData<List<Exercise>> getExercises() {
        return exerciseDbRepository.getExercises();
    }

    // obtain Exercises by ExerciseItem list
    private LiveData<List<Exercise>> getExercisesByExerciseItems(List<ExerciseItem> exerciseItems) {
        return exerciseDbRepository.getExercisesByExerciseItems(exerciseItems);
    }

    // returns list of exercises or returns a empty list if none found
    public LiveData<List<Exercise>> getExercisesByExerciseItemsLiveData(LiveData<List<ExerciseItem>> exerciseItemsLiveData) {
        return Transformations.switchMap(exerciseItemsLiveData, exerciseItems -> {
            if (exerciseItems != null) {
                return getExercisesByExerciseItems(exerciseItems);
            } else {
                MutableLiveData<List<Exercise>> emptyLiveData = new MutableLiveData<>();
                emptyLiveData.setValue(new ArrayList<>());
                return emptyLiveData;
            }
        });
    }

    // Obtained paired items ExerciseItem and Exercise
    public List<ExerciseItemWithExercise> getExerciseItemWithExercises(List<ExerciseItem> exerciseItems, List<Exercise> exercises) {
        List<ExerciseItemWithExercise> exerciseItemWithExercises = new ArrayList<>();

        for (ExerciseItem exerciseItem : exerciseItems) {
            for (Exercise exercise : exercises) {
                // Compare strings as Exercise ID is type string
                if (exerciseItem.getExerciseId().equals(exercise.getId())) {
                    exerciseItemWithExercises.add(new ExerciseItemWithExercise(exerciseItem, exercise));
                    break;
                }
            }
        }
        return exerciseItemWithExercises;
    }
}
