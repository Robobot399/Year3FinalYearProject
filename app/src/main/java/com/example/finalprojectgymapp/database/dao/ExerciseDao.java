package com.example.finalprojectgymapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.finalprojectgymapp.model.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    // OnConflict, replace existing records with same primary key
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExercises(List<Exercise> exercises);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleExercise(Exercise exercise);

    @Query("SELECT * FROM exercise_table")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercise_table WHERE id=:id")
    LiveData<Exercise> getExerciseById(String id);

    // Obtain list of exercise linked to given list of ExerciseItems
    @Query("SELECT * FROM exercise_table WHERE id IN(:exerciseIds)")
    LiveData<List<Exercise>> getExercisesByIds(List<String> exerciseIds);

    // Obtain exercise list by name
    @Query("SELECT * FROM exercise_table WHERE name LIKE '%'||:text||'%'")
    LiveData<List<Exercise>> getExercisesByName(String text);
}
