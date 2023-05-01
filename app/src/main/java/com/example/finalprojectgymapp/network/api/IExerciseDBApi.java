package com.example.finalprojectgymapp.network.api;

import com.example.finalprojectgymapp.model.Exercise;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

// API access point
public interface IExerciseDBApi {
    @Headers({
            "content-type: application/octet-stream",
            "X-RapidAPI-Key: 13fb68e040mshcdd4d389ef0040dp152f1fjsn365c62ff9e82",
            "X-RapidAPI-Host: exercisedb.p.rapidapi.com"
    })
    @GET("exercises")
    Call<List<Exercise>> getExercises();
}
