package com.example.finalprojectgymapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.finalprojectgymapp.database.ExerciseDatabase;
import com.example.finalprojectgymapp.database.dao.ExerciseItemDao;
import com.example.finalprojectgymapp.database.dao.WorkoutLogDao;
import com.example.finalprojectgymapp.model.ExerciseItem;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExerciseItemRepository {
    private ExerciseItemDao exerciseItemDao;
    private WorkoutLogDao workoutLogDao;

    private LiveData<List<ExerciseItem>> allExerciseItems;
    private Executor executor;

    public ExerciseItemRepository(Context context) {
        ExerciseDatabase database = ExerciseDatabase.getInstance(context);
        workoutLogDao = database.workoutLogDao();
        exerciseItemDao = database.exerciseItemDao();

        allExerciseItems = exerciseItemDao.getAllExerciseItems();
        executor = Executors.newSingleThreadExecutor();
    }


    public void insert(ExerciseItem exerciseItem) {
        executor.execute(() -> exerciseItemDao.insert(exerciseItem));
    }

    public void update(ExerciseItem exerciseItem) {
        executor.execute(() -> exerciseItemDao.update(exerciseItem));
    }

    public void delete(ExerciseItem exerciseItem) {
        executor.execute(() -> {
            exerciseItemDao.delete(exerciseItem);
            //workoutLogDao.deleteWorkoutLogsWithNoExerciseLogs(); // no longer required
        });
    }

    public void deleteAllExerciseItems() {
        executor.execute(() -> {
            exerciseItemDao.deleteAllExerciseItems();
            //workoutLogDao.deleteAllWorkoutLogs(); // no longer required
        });
    }

    public LiveData<List<ExerciseItem>> getAllExerciseItems() {
        return allExerciseItems;
    }

    public LiveData<List<ExerciseItem>> getExerciseItemsByWorkoutId(int workoutId) {
        return exerciseItemDao.getExerciseItemsByWorkoutId(workoutId);
    }
}
