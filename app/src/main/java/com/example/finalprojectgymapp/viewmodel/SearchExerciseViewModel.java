package com.example.finalprojectgymapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.repository.ExerciseDbRepository;

import java.util.List;

public class SearchExerciseViewModel extends AndroidViewModel {

    private ExerciseDbRepository exerciseDbRepository;
    private MutableLiveData<String> currentQuery = new MutableLiveData<>();

    private LiveData<List<Exercise>> allExercises;
    private MediatorLiveData<List<Exercise>> exercisesResult = new MediatorLiveData<>();

    public SearchExerciseViewModel(@NonNull Application application) {
        super(application);
        exerciseDbRepository = ExerciseDbRepository.getInstance(application);
        allExercises = exerciseDbRepository.getExercises();

        // Default: set exercisesResult with all exercises from database.
        exercisesResult.addSource(allExercises, exercisesResult::setValue);

        // Update exerciseResult based on current user search query
        currentQuery.observeForever(query -> {
            exercisesResult.removeSource(allExercises);

            if (query != null && !query.isEmpty()) {
                LiveData<List<Exercise>> filteredExercises = exerciseDbRepository.getExercisesByName(query);
                exercisesResult.addSource(filteredExercises, exercisesResult::setValue);
            } else {
                // Empty query, bring all exercises back
                exercisesResult.addSource(allExercises, exercisesResult::setValue);
            }
        });

    }

    public void searchExercises(String query) {
        currentQuery.setValue(query);
    }

    public void clearSearch() {
        currentQuery.setValue("");
    }

    public LiveData<List<Exercise>> getExercisesResult() {
        return exercisesResult;
    }
}
