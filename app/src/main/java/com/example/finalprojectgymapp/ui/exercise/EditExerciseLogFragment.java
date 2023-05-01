package com.example.finalprojectgymapp.ui.exercise;

import static com.example.finalprojectgymapp.ui.exercise.ExerciseHistoryFragment.PASSED_EXERCISE_LOG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.MainActivity;
import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.adapter.EditExerciseSetAdapter;
import com.example.finalprojectgymapp.databinding.FragmentEditExerciseLogBinding;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseLogViewModel;
import com.example.finalprojectgymapp.dataviewmodel.WorkoutLogViewModel;
import com.example.finalprojectgymapp.model.ExerciseLog;
import com.example.finalprojectgymapp.model.ExerciseSet;
import com.example.finalprojectgymapp.model.WorkoutLog;
import com.example.finalprojectgymapp.util.DateToStringConverter;
import com.example.finalprojectgymapp.viewmodel.EditExerciseLogViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditExerciseLogFragment extends Fragment {
    private FragmentEditExerciseLogBinding binding;

    public enum FragmentState {
        ADDING_LOG, EDITING_lOG
    }

    private FragmentState fragmentState;
    public static final String FRAGMENT_STATE_KEY = "ExerciseLog_State";

    private ImageView addSetBtn, deleteSetBtn;
    private RecyclerView recyclerView;
    private EditExerciseSetAdapter adapter;

    private LiveData<List<ExerciseSet>> exerciseSetsLiveData;
    private ExerciseLog exerciseLog;

    private EditExerciseLogViewModel editExerciseLogViewModel;
    private ExerciseLogViewModel exerciseLogViewModel;
    private WorkoutLogViewModel workoutLogViewModel;

    private Calendar calendar = Calendar.getInstance();
    private Date date;

    private boolean changesSaved = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up toolbar
        setFragmentToolbar();
        setToolbarMenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditExerciseLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        workoutLogViewModel = new ViewModelProvider(this).get(WorkoutLogViewModel.class);
        editExerciseLogViewModel = new ViewModelProvider(this).get(EditExerciseLogViewModel.class);
        exerciseLogViewModel = new ViewModelProvider(this).get(ExerciseLogViewModel.class);

        initViews();

        Bundle bundle = getArguments();
        if (null != bundle) {
            // Obtain viewed ExerciseLog for this fragment
            exerciseLog = bundle.getParcelable(PASSED_EXERCISE_LOG);
            fragmentState = (FragmentState) bundle.getSerializable(FRAGMENT_STATE_KEY);
        }

        if (fragmentState == FragmentState.ADDING_LOG) {
            // ADDING state - generates temporary empty sets
            exerciseSetsLiveData = editExerciseLogViewModel.initTemporaryExerciseSets();
        } else if (fragmentState == FragmentState.EDITING_lOG) {
            // EDITING state - obtain local copy of ExerciseSet from database
            exerciseSetsLiveData = editExerciseLogViewModel.getExerciseSetsByExerciseLogId(exerciseLog.getId());
        }
        exerciseSetsLiveData.observe(getViewLifecycleOwner(), new Observer<List<ExerciseSet>>() {
            @Override
            public void onChanged(List<ExerciseSet> exerciseSets) {
                adapter.submitList(exerciseSets);
            }
        });

        // Get currently set date for the related ExerciseLog
        workoutLogViewModel.getWorkoutLogById(exerciseLog.getWorkoutLogId()).observe(getViewLifecycleOwner(), new Observer<WorkoutLog>() {
            @Override
            public void onChanged(WorkoutLog workoutLog) {
                date = DateToStringConverter.convertStringToDate(workoutLog.getWorkoutDate());
            }
        });

        // add button set
        addSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editExerciseLogViewModel.addExerciseSet(new ExerciseSet(0, 0, 0)); // Don't pass LogId until confirm save
                hideKeyboard(v);
                adapter.notifyDataSetChanged();
            }
        });
        // delete button set
        deleteSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editExerciseLogViewModel.deleteExerciseSet();
                hideKeyboard(v);
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    private void initViews() {
        addSetBtn = binding.addSetBtn;
        deleteSetBtn = binding.deleteSetBtn;
        recyclerView = binding.recyclerView;

        adapter = new EditExerciseSetAdapter(editExerciseLogViewModel);
        recyclerView.setAdapter(adapter);
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void setFragmentToolbar() {
        Toolbar toolbar = binding.exerciseSetToolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((MainActivity)requireActivity()).getSupportActionBar();
        if(actionBar != null) {
            // set title
            actionBar.setTitle("Edit exercise log");
            // Display back button in toolbar
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            // Handle back button press
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    private void setToolbarMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.exercise_set_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // Menu handled here
                if (menuItem.getItemId() == R.id.date_picker_action) {
                    // Change date of workout
                    showDatePicker();
                    return true;
                } else if (menuItem.getItemId() == R.id.save_log_action) {
                    // Save changes
                    editExerciseLogViewModel.saveUpdatedExerciseSets(exerciseLog.getId()); // save sets
                    exerciseLogViewModel.updateExerciseLogDate(exerciseLog.getId(), date);// save date
                    changesSaved = true;
                    getActivity().onBackPressed(); // navigate back to previous fragment
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date = calendar.getTime(); // store changed date in fragment
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Delete newly temporary Exercise Log if user decides to not save
        if (fragmentState == FragmentState.ADDING_LOG && !changesSaved) {
            exerciseLogViewModel.delete(exerciseLog);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Hide bottom nav bar when fragment is visible
        BottomNavigationView bottomNavBar = getActivity().findViewById(R.id.bottom_nav_view);
        bottomNavBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        /// Show bottom nav bar when fragment is not visible
        BottomNavigationView bottomNavBar = getActivity().findViewById(R.id.bottom_nav_view);
        bottomNavBar.setVisibility(View.VISIBLE);
    }
}
