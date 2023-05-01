package com.example.finalprojectgymapp.network;

import com.example.finalprojectgymapp.network.api.IExerciseDBApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Build a API client to retrieve API data
public class ExerciseDBApiClient {
    private static final String BASE_URL = "https://exercisedb.p.rapidapi.com/";
    private static ExerciseDBApiClient instance;
    private IExerciseDBApi api;

    private ExerciseDBApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(IExerciseDBApi.class);
    }

    public static synchronized ExerciseDBApiClient getInstance() {
        if (instance == null) {
            instance = new ExerciseDBApiClient();
        }
        return instance;
    }

    public IExerciseDBApi getApi() {
        return api;
    }
}
