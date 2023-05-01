package com.example.finalprojectgymapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "set_table", foreignKeys =
        @ForeignKey(entity = ExerciseLog.class,
            parentColumns = "id",
            childColumns = "exercise_log_id",
            onDelete = ForeignKey.CASCADE),
        indices = @Index("exercise_log_id"))
public class ExerciseSet {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int reps;
    private double weight;

    @ColumnInfo(name = "exercise_log_id")
    private int exerciseLogId;

    public ExerciseSet(int reps, double weight, int exerciseLogId) {
        this.reps = reps;
        this.weight = weight;
        this.exerciseLogId = exerciseLogId;
    }

    // Used to update an existing set
    @Ignore
    public ExerciseSet(int id, int reps, double weight, int exerciseLogId) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
        this.exerciseLogId = exerciseLogId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getReps() {
        return reps;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getExerciseLogId() {
        return exerciseLogId;
    }
    public void setExerciseLogId(int exerciseLogId) {
        this.exerciseLogId = exerciseLogId;
    }
}
