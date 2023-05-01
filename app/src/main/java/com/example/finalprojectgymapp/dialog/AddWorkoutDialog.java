package com.example.finalprojectgymapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.dataviewmodel.WorkoutsViewModel;

public class AddWorkoutDialog extends DialogFragment {

    private EditText planNameEditText;
    private Button btnDismiss, btnAdd;

    private WorkoutsViewModel workoutsViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_workout_details, null);
        initView(view);
        workoutsViewModel = new ViewModelProvider(requireActivity()).get(WorkoutsViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add Workout");

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = planNameEditText.getText().toString();
                workoutsViewModel.addWorkout(name);
                dismiss();
            }
        });

        return builder.create();
    }

    private void initView(View view) {
        planNameEditText = view.findViewById(R.id.planNameEditText);
        btnDismiss = view.findViewById(R.id.btnDismiss);
        btnAdd = view.findViewById(R.id.btnAdd);
    }

}
