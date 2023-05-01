package com.example.finalprojectgymapp.ui.exercise;

import static com.example.finalprojectgymapp.ui.exercise.EditExerciseLogFragment.FRAGMENT_STATE_KEY;
import static com.example.finalprojectgymapp.ui.exercise.ExerciseFragment.PASSED_EXERCISE_KEY;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalprojectgymapp.MainActivity;
import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.adapter.ExerciseLogAdapter;
import com.example.finalprojectgymapp.databinding.FragmentExerciseHistoryBinding;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseLogViewModel;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseSetViewModel;
import com.example.finalprojectgymapp.dataviewmodel.WorkoutLogViewModel;
import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.ExerciseLog;
import com.example.finalprojectgymapp.model.ExerciseLogWithWorkoutLog;
import com.example.finalprojectgymapp.model.WorkoutLog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseHistoryFragment extends Fragment implements ExerciseLogAdapter.OnExerciseLogClickListener {

    private static final String TAG = "ExerciseHistoryFragment";

    @Override
    public void onExerciseLog(ExerciseLog exerciseLog) {
        NavController navController = NavHostFragment.findNavController(ExerciseHistoryFragment.this);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASSED_EXERCISE_LOG, exerciseLog);
        bundle.putSerializable(FRAGMENT_STATE_KEY, EditExerciseLogFragment.FragmentState.EDITING_lOG);
        navController.navigate(R.id.action_exerciseHistoryFragment_to_editExerciseLogFragment, bundle);
    }

    public static final String PASSED_EXERCISE_LOG = "exercise_log";

    private ExerciseLogViewModel exerciseLogViewModel;
    private WorkoutLogViewModel workoutLogViewModel;
    private ExerciseSetViewModel exerciseSetViewModel;

    private FragmentExerciseHistoryBinding binding;

    private Exercise exercise;

    private ExerciseLogAdapter adapter;

    private List<WorkoutLog> workoutLogs;
    private List<ExerciseLog> exerciseLogs;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set toolbar
        setFragmentToolbar();
        setToolbarMenu(); // setup the menu
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exerciseLogViewModel = new ViewModelProvider(this).get(ExerciseLogViewModel.class);
        workoutLogViewModel = new ViewModelProvider(this).get(WorkoutLogViewModel.class);
        exerciseSetViewModel = new ViewModelProvider(this).get(ExerciseSetViewModel.class);
        binding = FragmentExerciseHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();

        // Obtain exercise id
        Bundle bundle = getArguments();
        if (null != bundle) {
            exercise = bundle.getParcelable(PASSED_EXERCISE_KEY);
        }

        workoutLogViewModel.getAllWorkoutLogs().observe(getViewLifecycleOwner(), new Observer<List<WorkoutLog>>() {
            @Override
            public void onChanged(List<WorkoutLog> passedWorkoutLogs) {
                workoutLogs = passedWorkoutLogs;
                combineExerciseLogsWithWorkoutLogs();
            }
        });

        exerciseLogViewModel.getExerciseLogsByExerciseId(exercise.getId()).observe(getViewLifecycleOwner(), new Observer<List<ExerciseLog>>() {
            @Override
            public void onChanged(List<ExerciseLog> passedExerciseLogs) {
                exerciseLogs = passedExerciseLogs;
                combineExerciseLogsWithWorkoutLogs();
            }
        });

        // TODO: implement remove ExerciseLog button

        return root;
    }

    private void combineExerciseLogsWithWorkoutLogs() {
        if (exerciseLogs == null || workoutLogs == null) {return;}

        List<ExerciseLogWithWorkoutLog> exerciseLogWithWorkoutLogs = new ArrayList<>();
        for (ExerciseLog exerciseLog : exerciseLogs) {
            for (WorkoutLog workoutLog : workoutLogs) {
                if (exerciseLog.getWorkoutLogId() == workoutLog.getId()) {
                    exerciseLogWithWorkoutLogs.add(new ExerciseLogWithWorkoutLog(exerciseLog, workoutLog));
                    break;
                }
            }
        }
        adapter.setExerciseLogWithWorkoutLogs((ArrayList<ExerciseLogWithWorkoutLog>) exerciseLogWithWorkoutLogs);
    }

    private void initViews() {
        adapter = new ExerciseLogAdapter(this, exercise, exerciseSetViewModel, requireContext());
        binding.recyclerView.setAdapter(adapter);
        if (getContext() != null) {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void setFragmentToolbar() {
        Toolbar toolbar = binding.exerciseLogToolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((MainActivity)requireActivity()).getSupportActionBar();
        if(actionBar != null){
            // set title
            actionBar.setTitle(exercise.getName() + " Log");

            // Enable back button in toolbar
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                menuInflater.inflate(R.menu.exercise_log_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.add_log_action) {
                    // Navigate to EditExerciseLogFragment while passing the newly added Exercise Log.
                    workoutLogViewModel.getOrCreateWorkoutLogIdForToday().observe(getViewLifecycleOwner(), workoutLogId -> {
                        int newExerciseLogId = exerciseLogViewModel.insert(new ExerciseLog(exercise.getId(), workoutLogId));

                        exerciseLogViewModel.getExerciseLogById(newExerciseLogId).observe(getViewLifecycleOwner(), new Observer<ExerciseLog>() {
                            @Override
                            public void onChanged(ExerciseLog exerciseLog) {
                                NavController navController = NavHostFragment.findNavController(ExerciseHistoryFragment.this);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(PASSED_EXERCISE_LOG, exerciseLog);
                                bundle.putSerializable(FRAGMENT_STATE_KEY, EditExerciseLogFragment.FragmentState.ADDING_LOG);

                                if (navController.getCurrentDestination().getId() == R.id.exerciseHistoryFragment) {
                                    navController.navigate(R.id.action_exerciseHistoryFragment_to_editExerciseLogFragment, bundle);
                                    exerciseLogViewModel.getExerciseLogById(newExerciseLogId).removeObserver(this);
                                } else {
                                    Log.d(TAG, "onChanged: Navigation not occurring, " + exerciseLog.getId() + ". Destination: " + navController.getCurrentDestination().getId());
                                }
                            }
                        });
                    });
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner());
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