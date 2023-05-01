package com.example.finalprojectgymapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.finalprojectgymapp.database.dao.ExerciseDao;
import com.example.finalprojectgymapp.database.dao.ExerciseItemDao;
import com.example.finalprojectgymapp.database.dao.ExerciseLogDao;
import com.example.finalprojectgymapp.database.dao.ExerciseSetDao;
import com.example.finalprojectgymapp.database.dao.WorkoutDao;
import com.example.finalprojectgymapp.database.dao.WorkoutLogDao;
import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.ExerciseItem;
import com.example.finalprojectgymapp.model.ExerciseLog;
import com.example.finalprojectgymapp.model.ExerciseSet;
import com.example.finalprojectgymapp.model.Workout;
import com.example.finalprojectgymapp.model.WorkoutLog;

@Database(entities = {Exercise.class, ExerciseItem.class, ExerciseLog.class, ExerciseSet.class, Workout.class, WorkoutLog.class},
        version = 1, exportSchema = false)
public abstract class ExerciseDatabase extends RoomDatabase {
    private static ExerciseDatabase instance;

    // DAO list
    public abstract ExerciseDao exerciseDao();
    public abstract ExerciseItemDao exerciseItemDao();
    public abstract ExerciseLogDao exerciseLogDao();
    public abstract ExerciseSetDao exerciseSetDao();
    public abstract WorkoutDao workoutDao();
    public abstract WorkoutLogDao workoutLogDao();

    // Singleton pattern to avoid multiple instances
    public static synchronized ExerciseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ExerciseDatabase.class, "exercise_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
