package com.example.finalprojectgymapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalprojectgymapp.model.ExerciseLog;

import java.util.ArrayList;
import java.util.List;

public class TrackWorkoutViewModel extends ViewModel {
    private MutableLiveData<List<ExerciseLog>> exerciseLogs;
    private final MutableLiveData<Boolean> stopWorkout = new MutableLiveData<>();

    public TrackWorkoutViewModel() {
        exerciseLogs = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<ExerciseLog>> getExerciseLogs() {
        return exerciseLogs;
    }

    public void addExerciseLog(ExerciseLog exerciseLog) {
        List<ExerciseLog> logs = exerciseLogs.getValue();
        logs.add(exerciseLog);
        exerciseLogs.setValue(logs);
    }

    public void clearExerciseLogs() {
        exerciseLogs.setValue(new ArrayList<>());
    }

    public void requestStopWorkout() {
        stopWorkout.setValue(true);
    }

    public LiveData<Boolean> getStopWorkout() {
        return stopWorkout;
    }

    public void clearStopWorkout() {
        stopWorkout.setValue(false);
    }

}
