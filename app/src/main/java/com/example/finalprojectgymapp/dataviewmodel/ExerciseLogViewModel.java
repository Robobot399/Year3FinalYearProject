package com.example.finalprojectgymapp.dataviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.finalprojectgymapp.util.DateToStringConverter;
import com.example.finalprojectgymapp.model.ExerciseLog;
import com.example.finalprojectgymapp.model.WorkoutLog;
import com.example.finalprojectgymapp.repository.ExerciseLogRepository;
import com.example.finalprojectgymapp.repository.WorkoutLogRepository;

import java.util.Date;
import java.util.List;

public class ExerciseLogViewModel extends AndroidViewModel {

    private ExerciseLogRepository exerciseLogRepository;
    private WorkoutLogRepository workoutLogRepository;

    public ExerciseLogViewModel(@NonNull Application application) {
        super(application);
        exerciseLogRepository = new ExerciseLogRepository(application);
        workoutLogRepository = new WorkoutLogRepository(application);
    }

    public int insert(ExerciseLog exerciseLog) {
        return exerciseLogRepository.insert(exerciseLog);
    }

    public void update(ExerciseLog exerciseLog) {
        exerciseLogRepository.update(exerciseLog);
    }

    // Update ExerciseLog day of working out
    public void updateExerciseLogDate(int exerciseLogId, Date newDate) {
        exerciseLogRepository.getExerciseLogById(exerciseLogId).observeForever(new Observer<ExerciseLog>() {
            @Override
            public void onChanged(ExerciseLog exerciseLog) {
                if (exerciseLog != null) {
                    getOrCreateWorkoutLogIdForDate(newDate).observeForever(new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer workoutLogId) {
                            exerciseLog.setWorkoutLogId(workoutLogId);
                            exerciseLogRepository.update(exerciseLog);
                        }
                    });
                }
            }
        });
    }

    // Obtain existing WorkoutLog for given date, or create a new one
    public LiveData<Integer> getOrCreateWorkoutLogIdForDate(Date date) {
        String dateString = DateToStringConverter.convertDateToString(date);
        LiveData<WorkoutLog> workoutLogLiveData = workoutLogRepository.getWorkoutLogByDate(dateString);

        return Transformations.map(workoutLogLiveData, workoutLog -> {
            if (workoutLog == null) {
                int newWorkoutLogId = workoutLogRepository.insert(new WorkoutLog(dateString));
                return newWorkoutLogId;
            } else {
                return workoutLog.getId();
            }
        });
    }

    public void delete(ExerciseLog exerciseLog) {
        exerciseLogRepository.delete(exerciseLog);
    }

    public void deleteById(int exerciseLogId) {
        exerciseLogRepository.deleteById(exerciseLogId);
    }

    public void deleteAllExerciseLogs() {
        exerciseLogRepository.deleteAllExerciseLogs();
    }

    public LiveData<List<ExerciseLog>> getAllExerciseLogs() {
        return exerciseLogRepository.getAllExerciseLogs();
    }

    public LiveData<ExerciseLog> getExerciseLogById(int id) {
        return exerciseLogRepository.getExerciseLogById(id);
    }

    public LiveData<List<ExerciseLog>> getExerciseLogsByExerciseId(String exerciseId) {
        return exerciseLogRepository.getExerciseLogsByExerciseId(exerciseId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Shutdown executor when ViewModel is cleared
        exerciseLogRepository.getExecutor().shutdown();
    }
}
