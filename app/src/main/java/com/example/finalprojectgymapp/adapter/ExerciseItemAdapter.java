package com.example.finalprojectgymapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.databinding.ExerciseitemItemBinding;
import com.example.finalprojectgymapp.model.ExerciseItemWithExercise;

import java.util.ArrayList;

public class ExerciseItemAdapter extends RecyclerView.Adapter<ExerciseItemAdapter.ExerciseItemViewHolder> {

    private ArrayList<ExerciseItemWithExercise> exerciseItemWithExercises = new ArrayList<>();

    @NonNull
    @Override
    public ExerciseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExerciseitemItemBinding exerciseItemBinding = ExerciseitemItemBinding.inflate(layoutInflater, parent, false);
        return new ExerciseItemViewHolder(exerciseItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseItemViewHolder holder, int position) {
        holder.binding.setExerciseItem(exerciseItemWithExercises.get(position).getExerciseItem());
        holder.binding.setExercise(exerciseItemWithExercises.get(position).getExercise());
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return exerciseItemWithExercises.size();
    }

    public void setExerciseItemWithExercises(ArrayList<ExerciseItemWithExercise> exerciseItemWithExercises) {
        this.exerciseItemWithExercises = exerciseItemWithExercises;
        notifyDataSetChanged();
    }

    class ExerciseItemViewHolder extends RecyclerView.ViewHolder {
        private ExerciseitemItemBinding binding;

        public ExerciseItemViewHolder(@NonNull ExerciseitemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
