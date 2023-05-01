package com.example.finalprojectgymapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_log", foreignKeys = {
        @ForeignKey(entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = WorkoutLog.class,
                parentColumns = "id",
                childColumns = "workout_log_id",
                onDelete = ForeignKey.SET_NULL)},
        indices = {@Index("exercise_id"), @Index("workout_log_id")})
public class ExerciseLog implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "exercise_id")
    private String exerciseId;
    @ColumnInfo(name = "workout_log_id")
    private int workoutLogId;

    public ExerciseLog(String exerciseId, int workoutLogId) {
        this.exerciseId = exerciseId;
        this.workoutLogId = workoutLogId;
    }

    protected ExerciseLog(Parcel in) {
        id = in.readInt();
        exerciseId = in.readString();
        workoutLogId = in.readInt();
    }

    public static final Creator<ExerciseLog> CREATOR = new Creator<ExerciseLog>() {
        @Override
        public ExerciseLog createFromParcel(Parcel in) {
            return new ExerciseLog(in);
        }

        @Override
        public ExerciseLog[] newArray(int size) {
            return new ExerciseLog[size];
        }
    };

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getExerciseId() {
        return exerciseId;
    }
    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getWorkoutLogId() {
        return workoutLogId;
    }
    public void setWorkoutLogId(int workoutLogId) {
        this.workoutLogId = workoutLogId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(exerciseId);
        dest.writeInt(workoutLogId);
    }
}
