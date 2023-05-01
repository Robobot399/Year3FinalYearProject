package com.example.finalprojectgymapp.model;

public class ExerciseLogWithWorkoutLog {
    private ExerciseLog exerciseLog;
    private WorkoutLog workoutLog;

    public ExerciseLogWithWorkoutLog(ExerciseLog exerciseLog, WorkoutLog workoutLog) {
        this.exerciseLog = exerciseLog;
        this.workoutLog = workoutLog;
    }

    public ExerciseLog getExerciseLog() {
        return exerciseLog;
    }

    public void setExerciseLog(ExerciseLog exerciseLog) {
        this.exerciseLog = exerciseLog;
    }

    public WorkoutLog getWorkoutLog() {
        return workoutLog;
    }

    public void setWorkoutLog(WorkoutLog workoutLog) {
        this.workoutLog = workoutLog;
    }
}
