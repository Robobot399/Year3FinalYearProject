package com.example.finalprojectgymapp.dataviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.finalprojectgymapp.model.Workout;
import com.example.finalprojectgymapp.util.DateToStringConverter;
import com.example.finalprojectgymapp.model.WorkoutLog;
import com.example.finalprojectgymapp.repository.WorkoutLogRepository;

import java.util.List;

public class WorkoutLogViewModel extends AndroidViewModel {

    private WorkoutLogRepository repository;

    public WorkoutLogViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutLogRepository(application);
    }

    public void insert(WorkoutLog workoutLog) {
        repository.insert(workoutLog);
    }

    public void update(WorkoutLog workoutLog) {
        repository.update(workoutLog);
    }

    public void delete(WorkoutLog workoutLog) {
        repository.delete(workoutLog);
    }

    public LiveData<List<WorkoutLog>> getAllWorkoutLogs() {
        return repository.getAllWorkoutLogs();
    }



    public LiveData<WorkoutLog> getWorkoutLogById(int workoutLogId) {
        return repository.getWorkoutLogById(workoutLogId);
    }

    public LiveData<Integer> getOrCreateWorkoutLogIdForToday() {
        LiveData<WorkoutLog> workoutLogLiveData = repository.getWorkoutLogForToday();

        return Transformations.map(workoutLogLiveData, workoutLog -> {
            if (workoutLog == null) {
                String dateString = DateToStringConverter.convertDateToString(DateToStringConverter.getCurrentDate());
                int newWorkoutLogId = repository.insert(new WorkoutLog(dateString));
                return newWorkoutLogId;
            } else {
                return workoutLog.getId();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Shutdown executor when ViewModel is cleared
        repository.getExecutor().shutdown();
    }
}
