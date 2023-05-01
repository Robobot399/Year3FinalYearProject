package com.example.finalprojectgymapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalprojectgymapp.model.ExerciseLog;

import java.util.List;

@Dao
public interface ExerciseLogDao {

    @Insert
    long insert(ExerciseLog exerciseLog); // retrieve ID back upon success

    @Update
    void update(ExerciseLog exerciseLog);

    @Delete
    void delete(ExerciseLog exerciseLog);

    @Query("DELETE FROM exercise_log WHERE id=:exerciseLogId")
    void deleteById(int exerciseLogId);

    @Query("DELETE FROM exercise_log")
    void deleteAllExerciseLogs();

    @Query("SELECT * FROM exercise_log WHERE id=:id")
    LiveData<ExerciseLog> getExerciseLogById(int id);

    @Query("SELECT * FROM exercise_log")
    LiveData<List<ExerciseLog>> getAllExerciseLogs();

    @Query("SELECT * FROM exercise_log WHERE exercise_id = :exerciseId")
    LiveData<List<ExerciseLog>> getExerciseLogsByExerciseId(String exerciseId);
}
