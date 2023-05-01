package com.example.finalprojectgymapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.databinding.ExerciseitemItemBinding;
import com.example.finalprojectgymapp.model.ExerciseItemWithExercise;
import com.example.finalprojectgymapp.model.ExerciseSet;

import java.util.ArrayList;

public class ExerciseItemAdapter extends ListAdapter<ExerciseItemWithExercise, ExerciseItemAdapter.ExerciseItemViewHolder> {

    public ExerciseItemAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ExerciseItemWithExercise> DIFF_CALLBACK = new DiffUtil.ItemCallback<ExerciseItemWithExercise>() {
        @Override
        public boolean areItemsTheSame(@NonNull ExerciseItemWithExercise oldItem, @NonNull ExerciseItemWithExercise newItem) {
            return oldItem.getExerciseItem().getId() == newItem.getExerciseItem().getId(); // identify if same by primary key
        }

        @Override
        public boolean areContentsTheSame(@NonNull ExerciseItemWithExercise oldItem, @NonNull ExerciseItemWithExercise newItem) {
            return oldItem.getExerciseItem().getSetAmount() == newItem.getExerciseItem().getSetAmount() &&
                    oldItem.getExerciseItem().getRepAmount() == newItem.getExerciseItem().getRepAmount();
        }
    };

    @NonNull
    @Override
    public ExerciseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExerciseitemItemBinding exerciseItemBinding = ExerciseitemItemBinding.inflate(layoutInflater, parent, false);
        return new ExerciseItemViewHolder(exerciseItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseItemViewHolder holder, int position) {
        holder.binding.setExerciseItem(getItem(position).getExerciseItem());
        holder.binding.setExercise(getItem(position).getExercise());
        holder.binding.executePendingBindings();
        Log.d("Adapter", "onBindViewHolder: position: " + position + ", id:" + getItem(position).getExerciseItem().getId() + " exerciseItem: " + getItem(position).getExerciseItem());
    }

    public ExerciseItemWithExercise getExerciseItemWithExerciseAt(int position){
        return getItem(position);
    }

    class ExerciseItemViewHolder extends RecyclerView.ViewHolder {
        private ExerciseitemItemBinding binding;

        public ExerciseItemViewHolder(@NonNull ExerciseitemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
