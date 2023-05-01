package com.example.finalprojectgymapp.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.util.DecimalInputFilter;
import com.example.finalprojectgymapp.util.ExerciseSetTextWatcher;
import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.databinding.ExerciseSetEditItemBinding;
import com.example.finalprojectgymapp.model.ExerciseSet;
import com.example.finalprojectgymapp.viewmodel.EditExerciseLogViewModel;

public class EditExerciseSetAdapter extends ListAdapter<ExerciseSet, EditExerciseSetAdapter.ExerciseSetViewHolder> {

    private EditExerciseLogViewModel viewModel;

    public EditExerciseSetAdapter(EditExerciseLogViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.viewModel = viewModel;
    }

    private static final DiffUtil.ItemCallback<ExerciseSet> DIFF_CALLBACK = new DiffUtil.ItemCallback<ExerciseSet>() {
        @Override
        public boolean areItemsTheSame(@NonNull ExerciseSet oldItem, @NonNull ExerciseSet newItem) {
            return oldItem.getId() == newItem.getId(); // identify if same by primary key
        }

        @Override
        public boolean areContentsTheSame(@NonNull ExerciseSet oldItem, @NonNull ExerciseSet newItem) {
            return oldItem.getWeight() == newItem.getWeight() &&
                    oldItem.getReps() == newItem.getReps();

        }
    };

    @NonNull
    @Override
    public EditExerciseSetAdapter.ExerciseSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExerciseSetEditItemBinding exerciseSetEditItemBinding = ExerciseSetEditItemBinding.inflate(layoutInflater, parent, false);
        return new ExerciseSetViewHolder(exerciseSetEditItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseSetViewHolder holder, int position) {
        ExerciseSet exerciseSet = getItem(position);
        holder.binding.setExerciseSet(exerciseSet);
        holder.binding.executePendingBindings();

        holder.binding.setNumberTextView.setText(holder.itemView.getContext().getString(R.string.set_number, position+1));
        holder.binding.weightEditText.setText(Double.toString(exerciseSet.getWeight()));
        holder.binding.repEditText.setText(Integer.toString(exerciseSet.getReps()));

        setKeyboardOption(holder, position);

        // Filter edit text inputs
        holder.binding.weightEditText.setFilters(new InputFilter[]{new DecimalInputFilter(9, 2)}); // restrict to 2 d.p.
        holder.binding.repEditText.setFilters(new InputFilter[]{new DecimalInputFilter(9, 0)});
        // Remove any existing TextWatcher
        holder.binding.weightEditText.removeTextChangedListener(holder.weightTextWatcher);
        holder.binding.repEditText.removeTextChangedListener(holder.repTextWatcher);

        // Update the position and add the custom TextWatcher
        holder.weightTextWatcher = new ExerciseSetTextWatcher(position, (pos, newText) -> {
            double weight = TextUtils.isEmpty(newText) ? 0 : Double.parseDouble(newText);
            getItem(pos).setWeight(weight);
            updateExerciseSetWeight(position, weight, exerciseSet.getReps(), exerciseSet, true);
        });
        holder.repTextWatcher = new ExerciseSetTextWatcher(position, (pos, newText) -> {
            int reps = TextUtils.isEmpty(newText) ? 0 : Integer.parseInt(newText);
            getItem(pos).setReps(reps);
            updateExerciseSetWeight(position, exerciseSet.getWeight(), reps, exerciseSet, false);
        });

        holder.binding.weightEditText.addTextChangedListener(holder.weightTextWatcher);
        holder.binding.repEditText.addTextChangedListener(holder.repTextWatcher);
    }

    private void setKeyboardOption(@NonNull ExerciseSetViewHolder holder, int position){
        holder.binding.weightEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        holder.binding.weightEditText.setNextFocusDownId(holder.binding.repEditText.getId());

        holder.binding.repEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        holder.binding.repEditText.setNextFocusDownId(holder.binding.weightEditText.getId());
        holder.binding.repEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Save the input and close the keyboard
                holder.binding.repEditText.clearFocus();
                InputMethodManager imm = (InputMethodManager) holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.binding.repEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private void updateExerciseSetWeight(int position, double weight, int reps, ExerciseSet oldExerciseSet, boolean isWeightOrRep) {
        ExerciseSet currentSet = viewModel.getUpdatedExerciseSets().get(position);
        // If first time applying change to set, assign new temporary ExerciseSet
        if (currentSet == null) {
            // ID - either a number from database copy, or 0 from temporary item
            currentSet = new ExerciseSet(oldExerciseSet.getId(), oldExerciseSet.getReps(), oldExerciseSet.getWeight(), oldExerciseSet.getExerciseLogId());
        }
        // set new inputted weights OR new inputted reps
        if (isWeightOrRep) { currentSet.setWeight(weight);}
        else {currentSet.setReps(reps);}

        viewModel.updateExerciseSet(position, currentSet);
        // TODO: update recyclerView to represent changes
    }

    public ExerciseSet getExerciseSetAt(int position){
        return getItem(position);
    }

    class ExerciseSetViewHolder extends RecyclerView.ViewHolder{
        private ExerciseSetEditItemBinding binding;
        ExerciseSetTextWatcher weightTextWatcher;
        ExerciseSetTextWatcher repTextWatcher;
        public ExerciseSetViewHolder(@NonNull ExerciseSetEditItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}