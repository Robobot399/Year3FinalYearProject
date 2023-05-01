package com.example.finalprojectgymapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.databinding.ExerciseSetItemBinding;
import com.example.finalprojectgymapp.model.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSetAdapter extends RecyclerView.Adapter<ExerciseSetAdapter.ExerciseSetViewHolder> {

    private List<ExerciseSet> exerciseSets = new ArrayList<>();

    @NonNull
    @Override
    public ExerciseSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExerciseSetItemBinding exerciseSetItemBinding = ExerciseSetItemBinding.inflate(layoutInflater, parent, false);
        return new ExerciseSetViewHolder(exerciseSetItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseSetViewHolder holder, int position) {
        holder.binding.setExerciseSet(exerciseSets.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return exerciseSets.size();
    }

    public void setExerciseSets(List<ExerciseSet> exerciseSets) {
        this.exerciseSets = exerciseSets;
        notifyDataSetChanged();
    }

    class ExerciseSetViewHolder extends RecyclerView.ViewHolder {
        private ExerciseSetItemBinding binding;

        public ExerciseSetViewHolder(@NonNull ExerciseSetItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

