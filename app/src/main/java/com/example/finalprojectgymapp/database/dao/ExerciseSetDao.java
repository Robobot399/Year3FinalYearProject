package com.example.finalprojectgymapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalprojectgymapp.model.ExerciseSet;

import java.util.List;

@Dao
public interface ExerciseSetDao {
    @Insert
    void insert(ExerciseSet exerciseSet);

    @Update
    void update(ExerciseSet exerciseSet);

    @Delete
    void delete(ExerciseSet exerciseSet);

    @Query("DELETE FROM set_table")
    void deleteAllExerciseSets();

    @Query("SELECT * FROM set_table")
    LiveData<List<ExerciseSet>> getAllExerciseSets();

    @Query("SELECT * FROM set_table WHERE exercise_log_id = :exerciseLogId")
    LiveData<List<ExerciseSet>> getExerciseSetsByExerciseLogId(int exerciseLogId);

    @Query("SELECT * FROM set_table WHERE exercise_log_id = :exerciseLogId")
    List<ExerciseSet> getListExerciseSetsByExerciseLogId(int exerciseLogId);
}
