package com.example.finalprojectgymapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.finalprojectgymapp.database.ExerciseDatabase;
import com.example.finalprojectgymapp.database.dao.ExerciseSetDao;
import com.example.finalprojectgymapp.model.ExerciseSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExerciseSetRepository {
    private ExerciseSetDao exerciseSetDao;
    private LiveData<List<ExerciseSet>> allExerciseSets;
    private ExecutorService executor;

    public ExerciseSetRepository(Context context) {
        ExerciseDatabase database = ExerciseDatabase.getInstance(context);
        exerciseSetDao = database.exerciseSetDao();
        allExerciseSets = exerciseSetDao.getAllExerciseSets();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(ExerciseSet exerciseSet) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseSetDao.insert(exerciseSet));
    }

    public void update(ExerciseSet exerciseSet) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseSetDao.update(exerciseSet));
    }

    public void delete(ExerciseSet exerciseSet) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseSetDao.delete(exerciseSet));
    }

    public void deleteAllExerciseSets() {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseSetDao.deleteAllExerciseSets());
    }

    public LiveData<List<ExerciseSet>> getAllExerciseSets() {
        return allExerciseSets;
    }

    public LiveData<List<ExerciseSet>> getExerciseSetsByExerciseLogId(int exerciseLogId) {
        return exerciseSetDao.getExerciseSetsByExerciseLogId(exerciseLogId);
    }

    public List<ExerciseSet> getListExerciseSetsByExerciseLogId(int exerciseLogId) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        // Retrieve return from the executor
        Future<List<ExerciseSet>> future = executor.submit(() -> exerciseSetDao.getListExerciseSetsByExerciseLogId(exerciseLogId));

        List<ExerciseSet> exerciseSets = new ArrayList<>();
        try {
            exerciseSets = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return exerciseSets;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
