package com.example.finalprojectgymapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.databinding.ExerciseItemBinding;
import com.example.finalprojectgymapp.model.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    public interface OnExerciseClickListener {
        void onExercise(Exercise exercise);
    }

    private ArrayList<Exercise> exercises = new ArrayList<>();
    private OnExerciseClickListener onExerciseClickListener;

    public ExerciseAdapter(OnExerciseClickListener listener) {
        this.onExerciseClickListener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExerciseItemBinding exerciseItemBinding = ExerciseItemBinding.inflate(layoutInflater, parent, false);

        return new ExerciseViewHolder(exerciseItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.exerciseItemBinding.setExercise(exercises.get(position));
        holder.exerciseItemBinding.executePendingBindings();

        holder.exerciseItemBinding.exerciseParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    onExerciseClickListener.onExercise(exercises.get(currentPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private ExerciseItemBinding exerciseItemBinding;

        public ExerciseViewHolder(@NonNull ExerciseItemBinding exerciseItemBinding) {
            super(exerciseItemBinding.getRoot());
            this.exerciseItemBinding = exerciseItemBinding;
        }
    }
}
