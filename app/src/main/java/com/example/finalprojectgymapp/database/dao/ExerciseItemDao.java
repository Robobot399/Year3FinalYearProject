package com.example.finalprojectgymapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalprojectgymapp.model.ExerciseItem;

import java.util.List;

@Dao
public interface ExerciseItemDao {

    @Insert
    void insert(ExerciseItem exerciseItem);

    @Update
    void update(ExerciseItem exerciseItem);

    @Delete
    void delete(ExerciseItem exerciseItem);

    @Query("DELETE FROM exercise_item")
    void deleteAllExerciseItems();

    @Query("SELECT * FROM exercise_item")
    LiveData<List<ExerciseItem>> getAllExerciseItems();

    // Find list of exercise items from selected Workout
    @Query("SELECT * FROM exercise_item " +
            "INNER JOIN workout ON workout.id = workout_id " +
            "WHERE exercise_item.workout_id =:workoutId")
    LiveData<List<ExerciseItem>> getExerciseItemsByWorkoutId(int workoutId);
}
