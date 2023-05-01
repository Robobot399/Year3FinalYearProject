package com.example.finalprojectgymapp.util;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.function.BiConsumer;

public class ExerciseSetTextWatcher implements TextWatcher {
    private int position;
    private BiConsumer<Integer, String> onTextChanged;

    public ExerciseSetTextWatcher(int position, BiConsumer<Integer, String> onTextChanged) {
        this.position = position;
        this.onTextChanged = onTextChanged;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextChanged.accept(position, s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}

