package com.example.finalprojectgymapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.finalprojectgymapp.database.ExerciseDatabase;
import com.example.finalprojectgymapp.database.dao.ExerciseLogDao;
import com.example.finalprojectgymapp.model.ExerciseLog;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExerciseLogRepository {
    private ExerciseLogDao exerciseLogDao;
    private LiveData<List<ExerciseLog>> allExerciseLogs;
    private ExecutorService executor;

    public ExerciseLogRepository(Context context) {
        ExerciseDatabase database = ExerciseDatabase.getInstance(context);
        exerciseLogDao = database.exerciseLogDao();
        allExerciseLogs = exerciseLogDao.getAllExerciseLogs();
        executor = Executors.newSingleThreadExecutor();
    }

    public int insert(ExerciseLog exerciseLog) {
        // Retrieve return from the executor
        Future<Long> future = executor.submit(() -> exerciseLogDao.insert(exerciseLog));
        int rowId = -1;
        try {
            rowId = (int) future.get().longValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public void update(ExerciseLog exerciseLog) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseLogDao.update(exerciseLog));
    }

    public void delete(ExerciseLog exerciseLog) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseLogDao.delete(exerciseLog));
    }

    public void deleteById(int exerciseLogId) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseLogDao.deleteById(exerciseLogId));
    }

    public void deleteAllExerciseLogs() {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> exerciseLogDao.deleteAllExerciseLogs());
    }

    public LiveData<ExerciseLog> getExerciseLogById(int id) {
        return exerciseLogDao.getExerciseLogById(id);
    }

    public LiveData<List<ExerciseLog>> getAllExerciseLogs() {
        return allExerciseLogs;
    }

    public LiveData<List<ExerciseLog>> getExerciseLogsByExerciseId(String exerciseId) {
        return exerciseLogDao.getExerciseLogsByExerciseId(exerciseId);
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
