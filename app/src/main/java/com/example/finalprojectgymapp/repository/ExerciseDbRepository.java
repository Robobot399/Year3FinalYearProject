package com.example.finalprojectgymapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.finalprojectgymapp.database.ExerciseDatabase;
import com.example.finalprojectgymapp.database.dao.ExerciseDao;
import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.ExerciseItem;
import com.example.finalprojectgymapp.network.ExerciseDBApiClient;
import com.example.finalprojectgymapp.network.api.IExerciseDBApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseDbRepository {
    public enum ApiCallState {
        IDLE, LOADING, SUCCESS, FAILURE
    }

    public interface IExerciseDbResponse {
        void onResponse();

        void onFailure();
    }

    private MutableLiveData<ApiCallState> apiCallState = new MutableLiveData<>(ApiCallState.IDLE);
    // Database
    private ExerciseDao exerciseDao;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    // API
    private static ExerciseDbRepository instance;
    private List<Exercise> onlineExerciseList;
    private IExerciseDBApi exerciseDBApi;

    private ExerciseDbRepository(Application application) {
        ExerciseDatabase exerciseDatabase = ExerciseDatabase.getInstance(application);
        exerciseDao = exerciseDatabase.exerciseDao();

        onlineExerciseList = new ArrayList<>();
        exerciseDBApi = ExerciseDBApiClient.getInstance().getApi();
    }

    public static synchronized ExerciseDbRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ExerciseDbRepository(application);
        }
        return instance;
    }

    // API call: obtain list of exercise
    private void obtainExerciseList(int retryCount, IExerciseDbResponse exerciseDbResponse) {
        apiCallState.setValue(ApiCallState.LOADING);
        Call<List<Exercise>> responseCall = exerciseDBApi.getExercises();

        responseCall.enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    apiCallState.setValue(ApiCallState.SUCCESS);
                    onlineExerciseList = response.body();
                    insertExercisesIntoDb(onlineExerciseList);
                    exerciseDbResponse.onResponse();
                } else {
                    handleFailure(retryCount, exerciseDbResponse);
                }
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                handleFailure(retryCount, exerciseDbResponse);
            }
        });
    }

    private void handleFailure(int retryCount, IExerciseDbResponse exerciseDbResponse) {
        if (retryCount > 0) {
            // Retry the API call
            obtainExerciseList(retryCount - 1, exerciseDbResponse);
        } else {
            apiCallState.setValue(ApiCallState.FAILURE);
            exerciseDbResponse.onFailure();
        }
    }

    // obtain online exercise list from API
    private List<Exercise> getOnlineExerciseList() {
        return onlineExerciseList;
    }

    public LiveData<List<Exercise>> getExercises() {
        MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>();

        // First attempt get exercises from local database
        LiveData<List<Exercise>> localExercises = exerciseDao.getAllExercises();
        localExercises.observeForever(new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                if (exercises != null && !exercises.isEmpty()) {
                    // Local data is available: use it
                    exercisesLiveData.setValue(exercises);
                    localExercises.removeObserver(this);
                } else {
                    // Local data is not available: fetch from API
                    obtainExerciseList(3, new IExerciseDbResponse() {
                        @Override
                        public void onResponse() {
                            exercisesLiveData.setValue(getOnlineExerciseList());
                        }

                        @Override
                        public void onFailure() {
                            exercisesLiveData.setValue(null);
                        }
                    });
                    localExercises.removeObserver(this);
                }
            }
        });
        return exercisesLiveData;
    }

    // Obtain list of exercises by given ExerciseItems list
    public LiveData<List<Exercise>> getExercisesByExerciseItems(List<ExerciseItem> exerciseItems) {
        List<String> exercisesIds = new ArrayList<>();
        for (ExerciseItem exerciseItem : exerciseItems) {
            exercisesIds.add(exerciseItem.getExerciseId());
        }
        return exerciseDao.getExercisesByIds(exercisesIds);
    }

    private void insertExercisesIntoDb(List<Exercise> exercises) {
        executor.execute(() -> {
            exerciseDao.insertExercises(this.onlineExerciseList);
        });
    }

    public LiveData<ApiCallState> getApiCallState() {
        return apiCallState;
    }

    public LiveData<List<Exercise>> getExercisesByName(String name) {
        return exerciseDao.getExercisesByName(name);
    }
}
