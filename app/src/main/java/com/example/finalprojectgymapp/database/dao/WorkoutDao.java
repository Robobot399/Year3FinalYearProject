package com.example.finalprojectgymapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalprojectgymapp.model.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Insert
    void insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workout")
    void deleteAllWorkouts();

    @Query("SELECT * FROM workout")
    LiveData<List<Workout>> getAllWorkouts();
}
