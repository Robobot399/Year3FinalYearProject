package com.example.finalprojectgymapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.databinding.WorkoutItemBinding;
import com.example.finalprojectgymapp.model.Workout;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    public interface OnWorkoutItemClickListener {
        void onWorkoutItemClick(Workout workout);
    }

    private ArrayList<Workout> workouts = new ArrayList<>();
    private OnWorkoutItemClickListener listener;

    public WorkoutAdapter(OnWorkoutItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        WorkoutItemBinding binding = WorkoutItemBinding.inflate(layoutInflater, parent, false);

        return new WorkoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.binding.setWorkout(workouts.get(position));
        holder.binding.executePendingBindings(); // bind data before user interacts with card item.

        holder.binding.cardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    if (listener != null) {
                        listener.onWorkoutItemClick(workouts.get(currentPosition));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        WorkoutItemBinding binding;

        public WorkoutViewHolder(WorkoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
