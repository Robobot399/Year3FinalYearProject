package com.example.finalprojectgymapp.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

// Data binding to allow exercise images to be loaded
public class ExerciseBindingAdapter {
    @BindingAdapter("loadGif")
    public static void loadGif(ImageView view, String gifUrl) {
        if (gifUrl != null && !gifUrl.isEmpty()) {
            Glide.with(view.getContext())
                    .asGif()
                    .load(gifUrl)
                    .into(view);
        }
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView view, String gifUrl) {
        if (gifUrl != null && !gifUrl.isEmpty()) {
            Glide.with(view.getContext())
                    .asBitmap()
                    .circleCrop()
                    .load(gifUrl)
                    .into(view);
        }
    }
}
