package com.example.finalprojectgymapp.model;

// Class required to keep associated items together for ExerciseItemAdapter
public class ExerciseItemWithExercise {
    private ExerciseItem exerciseItem;
    private Exercise exercise;

    public ExerciseItemWithExercise(ExerciseItem exerciseItem, Exercise exercise) {
        this.exerciseItem = exerciseItem;
        this.exercise = exercise;
    }

    public ExerciseItem getExerciseItem() {
        return exerciseItem;
    }
    public void setExerciseItem(ExerciseItem exerciseItem) {
        this.exerciseItem = exerciseItem;
    }

    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
