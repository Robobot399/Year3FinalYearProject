package com.example.finalprojectgymapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_log_table")
public class WorkoutLog {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "workout_date")
    private String workoutDate;

    public WorkoutLog(String workoutDate) {
        this.workoutDate = workoutDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getWorkoutDate() {
        return workoutDate;
    }
    public void setWorkoutDate(String workoutDate) {
        this.workoutDate = workoutDate;
    }
}
