package com.example.finalprojectgymapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalprojectgymapp.model.WorkoutLog;

import java.util.List;

@Dao
public interface WorkoutLogDao {
    @Insert
    long insert(WorkoutLog workoutLog);

    @Update
    void update(WorkoutLog workoutLog);

    @Delete
    void delete(WorkoutLog workoutLog);

    @Query("DELETE FROM workout_log_table")
    void deleteAllWorkoutLogs();

    // Remove WorkoutLog when no exercise is logged for a certain date
    @Query("DELETE FROM workout_log_table where id NOT IN (SELECT DISTINCT workout_log_id FROM exercise_log WHERE workout_log_id IS NOT NULL)")
    void deleteWorkoutLogsWithNoExerciseLogs();

    @Query("SELECT * FROM workout_log_table")
    LiveData<List<WorkoutLog>> getAllWorkoutLogs();

    @Query("SELECT * FROM workout_log_table WHERE id = :workoutLogId")
    LiveData<WorkoutLog> getWorkoutLogById(int workoutLogId);

    @Query("SELECT * FROM workout_log_table WHERE workout_date =:date")
    LiveData<WorkoutLog> getWorkoutLogByDate(String date); // pass string instead of Date

    @Query("SELECT * FROM workout_log_table WHERE workout_date = :today")
    LiveData<WorkoutLog> getWorkoutLogForToday(String today);
}
