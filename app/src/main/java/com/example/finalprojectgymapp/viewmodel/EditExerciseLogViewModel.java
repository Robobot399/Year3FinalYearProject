package com.example.finalprojectgymapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.finalprojectgymapp.model.ExerciseSet;
import com.example.finalprojectgymapp.repository.ExerciseSetRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditExerciseLogViewModel extends AndroidViewModel {

    private ExerciseSetRepository repository;

    private MutableLiveData<List<ExerciseSet>> exerciseSetsLiveData = new MutableLiveData<>(); // list for Fragment
    private List<ExerciseSet> deletedExerciseSets = new ArrayList<>();

    private HashMap<Integer, ExerciseSet> updatedExerciseSets = new HashMap<>(); // list for adapter

    public EditExerciseLogViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseSetRepository(application);
    }

    public LiveData<List<ExerciseSet>> initTemporaryExerciseSets() {
        List<ExerciseSet> initialSets = new ArrayList<>();
        initialSets.add(new ExerciseSet(0, 0, 0));
        initialSets.add(new ExerciseSet(0, 0, 0));
        initialSets.add(new ExerciseSet(0, 0, 0));
        exerciseSetsLiveData.setValue(initialSets);
        return exerciseSetsLiveData;
    }

    public LiveData<List<ExerciseSet>> getExerciseSetsByExerciseLogId(int exerciseLogId) {
        LiveData<List<ExerciseSet>> liveDataFromRepo = repository.getExerciseSetsByExerciseLogId(exerciseLogId);

        liveDataFromRepo.observeForever(new Observer<List<ExerciseSet>>() {
            @Override
            public void onChanged(List<ExerciseSet> exerciseSets) {
                exerciseSetsLiveData.setValue(exerciseSets);
            }
        });
        return exerciseSetsLiveData;
    }

    // For EditExerciseLogFragment - storing local copies
    public void addExerciseSet(ExerciseSet newSet) {
        List<ExerciseSet> currentSets = exerciseSetsLiveData.getValue();
        if (currentSets != null) {
            currentSets.add(newSet);
            exerciseSetsLiveData.setValue(currentSets);
        }
    }

    // When user deletes a set that exist in local database
    public void deleteExerciseSet() {
        List<ExerciseSet> currentSets = exerciseSetsLiveData.getValue();
        if (currentSets != null && currentSets.size() > 1) {
            ExerciseSet lastSet = currentSets.get(currentSets.size() - 1);
            currentSets.remove(currentSets.size() - 1);
            exerciseSetsLiveData.setValue(currentSets);

            // Connected by foreign key -> it's a local copy, add to deletedExerciseSets for later confirmed removal
            if (lastSet.getExerciseLogId() != 0) {
                deletedExerciseSets.add(lastSet);
            }
        }
    }

    // User confirms saving changes
    public void saveUpdatedExerciseSets(int exerciseLogId) {
        boolean singleSet = updatedExerciseSets.size() == 1; // Save if there's just a single set, regardless of value

        // Obtain entry (key-value pair), then iterate through values only
        for (Map.Entry<Integer, ExerciseSet> entry : updatedExerciseSets.entrySet()) {
            ExerciseSet exerciseSet = entry.getValue();

            // Skip adding set if not singular item in list, or has no values in it
            if (!singleSet && exerciseSet.getReps() == 0 && exerciseSet.getWeight() == 0) {
                // if set is a local copy, add to deleteExerciseSets list
                if (exerciseSet.getExerciseLogId() != 0) {
                    deletedExerciseSets.add(exerciseSet);
                }
                continue;
            }

            if (exerciseSet.getExerciseLogId() != 0) {
                // Update the existing ExerciseSet
                repository.update(exerciseSet);
            } else {
                // Insert a new ExerciseSet - assign them to the viewed ExerciseLog
                exerciseSet.setExerciseLogId(exerciseLogId); // attach foreign key exerciseLogId to the set item
                repository.insert(exerciseSet);
            }
        }

        // Delete sets from deleteExerciseSets list
        for (ExerciseSet exerciseSet : deletedExerciseSets) {
            repository.delete(exerciseSet);
        }
        deletedExerciseSets.clear();
    }

    // Adapter: update temporary list
    public void updateExerciseSet(int position, ExerciseSet updatedSet) {
        updatedExerciseSets.put(position, updatedSet);
    }

    public HashMap<Integer, ExerciseSet> getUpdatedExerciseSets() {
        return updatedExerciseSets;
    }
}
