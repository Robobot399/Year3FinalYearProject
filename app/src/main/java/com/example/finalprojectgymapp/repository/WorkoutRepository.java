package com.example.finalprojectgymapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.finalprojectgymapp.database.ExerciseDatabase;
import com.example.finalprojectgymapp.database.dao.WorkoutDao;
import com.example.finalprojectgymapp.model.Workout;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkoutRepository {
    private WorkoutDao workoutDao;
    private LiveData<List<Workout>> allWorkouts;
    private Executor executor;

    public WorkoutRepository(Context context) {
        ExerciseDatabase database = ExerciseDatabase.getInstance(context);
        workoutDao = database.workoutDao();
        allWorkouts = workoutDao.getAllWorkouts();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(Workout workout) {
        executor.execute(() -> workoutDao.insert(workout));
    }

    public void update(Workout workout) {
        executor.execute(() -> workoutDao.update(workout));
    }

    public void delete(Workout workout) {
        executor.execute(() -> workoutDao.delete(workout));
    }

    public void deleteAllWorkouts() {
        executor.execute(() -> workoutDao.deleteAllWorkouts());
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }
}