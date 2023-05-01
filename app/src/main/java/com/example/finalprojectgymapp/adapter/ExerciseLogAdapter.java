package com.example.finalprojectgymapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.databinding.ExerciseLogItemBinding;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseSetViewModel;
import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.ExerciseLog;
import com.example.finalprojectgymapp.model.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class ExerciseLogAdapter extends RecyclerView.Adapter<ExerciseLogAdapter.ExerciseLogViewHolder> {

    public interface OnExerciseLogClickListener {
        void onExerciseLog(ExerciseLog exerciseLog);
    }

    private OnExerciseLogClickListener listener;

    private ArrayList<ExerciseLog> exerciseLogs = new ArrayList<>();
    private Exercise exercise;

    private ExerciseSetViewModel exerciseSetViewModel;
    private Context context;

    public ExerciseLogAdapter(OnExerciseLogClickListener listener, Exercise exercise, ExerciseSetViewModel exerciseSetViewModel, Context context) {
        this.listener = listener;
        this.exercise = exercise;
        this.exerciseSetViewModel = exerciseSetViewModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExerciseLogItemBinding exerciseLogItemBinding = ExerciseLogItemBinding.inflate(layoutInflater, parent, false);
        return new ExerciseLogViewHolder(exerciseLogItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseLogViewHolder holder, int position) {
        ExerciseLog currentExerciseLog = exerciseLogs.get(position);
        holder.binding.setExercise(exercise);
        holder.binding.setExerciseLog(currentExerciseLog);
        holder.binding.executePendingBindings();

        // Setup inner adapter
        holder.setExerciseSets(exerciseSetViewModel.getListExerciseSetsByExerciseLogId(currentExerciseLog.getId()));

        // Set on card click listener
        holder.binding.exerciseCardParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener.onExerciseLog(exerciseLogs.get(currentPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseLogs.size();
    }

    public void setExerciseLogs(ArrayList<ExerciseLog> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
        notifyDataSetChanged();
    }

    class ExerciseLogViewHolder extends RecyclerView.ViewHolder {
        private ExerciseLogItemBinding binding;
        private ExerciseSetAdapter exerciseSetAdapter;

        public ExerciseLogViewHolder(@NonNull ExerciseLogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // initialise inner recycler:
            exerciseSetAdapter = new ExerciseSetAdapter();
            binding.setListRecView.setLayoutManager(new LinearLayoutManager(binding.setListRecView.getContext()));
            binding.setListRecView.setAdapter(exerciseSetAdapter);
        }

        public void setExerciseSets(List<ExerciseSet> exerciseSets) {
            exerciseSetAdapter.setExerciseSets(exerciseSets);
        }
    }
}
