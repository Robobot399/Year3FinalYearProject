package com.example.finalprojectgymapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.finalprojectgymapp.database.ExerciseDatabase;
import com.example.finalprojectgymapp.database.dao.WorkoutLogDao;
import com.example.finalprojectgymapp.model.WorkoutLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WorkoutLogRepository {
    private WorkoutLogDao workoutLogDao;
    private LiveData<List<WorkoutLog>> allWorkoutLogs;
    private ExecutorService executor;

    public WorkoutLogRepository(Context context) {
        ExerciseDatabase database = ExerciseDatabase.getInstance(context);
        workoutLogDao = database.workoutLogDao();
        allWorkoutLogs = workoutLogDao.getAllWorkoutLogs();
        executor = Executors.newSingleThreadExecutor();
    }

    public int insert(WorkoutLog workoutLog) {
        Future<Long> future = executor.submit(() -> workoutLogDao.insert(workoutLog));
        int rowId = -1;
        try {
            rowId = (int) future.get().longValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public void update(WorkoutLog workoutLog) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> workoutLogDao.update(workoutLog));
    }

    public void delete(WorkoutLog workoutLog) {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> workoutLogDao.delete(workoutLog));
    }

    public void deleteAllWorkoutLogs() {
        if (executor.isShutdown()) executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> workoutLogDao.deleteAllWorkoutLogs());
    }

    public LiveData<List<WorkoutLog>> getAllWorkoutLogs() {
        return allWorkoutLogs;
    }

    public LiveData<WorkoutLog> getWorkoutLogById(int workoutLogId) {
        return workoutLogDao.getWorkoutLogById(workoutLogId);
    }

    public LiveData<WorkoutLog> getWorkoutLogByDate(String dateString) {
        return workoutLogDao.getWorkoutLogByDate(dateString);
    }

    public LiveData<WorkoutLog> getWorkoutLogForToday() {
        String today = getCurrentDate();
        return workoutLogDao.getWorkoutLogForToday(today);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}