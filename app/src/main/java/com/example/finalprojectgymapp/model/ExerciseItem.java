package com.example.finalprojectgymapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_item", foreignKeys = {
        @ForeignKey(entity = Workout.class,
                parentColumns = "id",
                childColumns = "workout_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id",
                onDelete = ForeignKey.CASCADE)},
        indices = {@Index("workout_id"), @Index("exercise_id")})
public class ExerciseItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "workout_id")
    private int workoutId;
    @ColumnInfo(name = "exercise_id")
    private String exerciseId;
    private int setAmount;
    private int repAmount;

    public ExerciseItem(int workoutId, String exerciseId, int setAmount, int repAmount) {
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
        this.setAmount = setAmount;
        this.repAmount = repAmount;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getWorkoutId() {
        return workoutId;
    }
    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getExerciseId() {
        return exerciseId;
    }
    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getSetAmount() {
        return setAmount;
    }
    public void setSetAmount(int setAmount) {
        this.setAmount = setAmount;
    }

    public int getRepAmount() {
        return repAmount;
    }
    public void setRepAmount(int repAmount) {
        this.repAmount = repAmount;
    }
}
