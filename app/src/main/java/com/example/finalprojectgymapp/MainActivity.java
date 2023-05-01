package com.example.finalprojectgymapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finalprojectgymapp.databinding.ActivityMainBinding;
import com.example.finalprojectgymapp.util.Utils;
import com.example.finalprojectgymapp.viewmodel.TrackWorkoutViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private BottomNavigationView bottomNavView;

    private FloatingActionButton resumeFab, stopFab;
    private boolean isPerformingWorkout = false;
    private TrackWorkoutViewModel trackWorkoutViewModel;

    private NavController.OnDestinationChangedListener onDestinationChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // target layout file with data binding
        setContentView(binding.getRoot());

        trackWorkoutViewModel = new ViewModelProvider(this).get(TrackWorkoutViewModel.class);

        initViews();
        Utils.getInstance(this);

        // BUILD navController to support with Bottom Navigation Bar View
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController(); // obtain NavGraph associated with fragment container
        // Support action bar to link with bottom nav bar
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_workout_item, R.id.navigation_exercise_item, R.id.navigation_history_item, R.id.navigation_profile_item)
                .build(); // Obtain top-level destinations for bottom nav bar

        NavigationUI.setupWithNavController(bottomNavView, navController); // Link Navigation to bottom nav bar

        // Manage floating action buttons
        setupFloatingActionButtons();
        setupDestinationChangedListener();
    }

    private void initViews() {
        resumeFab = binding.resumeFab;
        stopFab = binding.stopFab;
        bottomNavView = binding.bottomNavView;
    }

    private void setupFloatingActionButtons() {
        resumeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic based on current fragment
                if (isPerformingWorkout) {
                    // If the user wants to resume the workout, navigate back to PerformExerciseFragment
                    navController.navigate(R.id.action_global_performExerciseFragment); // Replace with the appropriate action or destination
                }
            }
        });

        stopFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPerformingWorkout) {
                    // TODO: confirm dialog to end exercise
                    if (true) {
                        isPerformingWorkout = false;
                        trackWorkoutViewModel.requestStopWorkout();
                    }
                }
            }
        });
    }

    private void setupDestinationChangedListener() {
        onDestinationChangedListener = new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.performExerciseFragment || !isPerformingWorkout) {
                    // Hide floating action buttons when PerformExerciseFragment is visible
                    resumeFab.setVisibility(View.GONE);
                    stopFab.setVisibility(View.GONE);
                } else {
                    // Show floating action buttons when PerformExerciseFragment is not visible
                    // Or we stopped performing workout
                    resumeFab.setVisibility(View.VISIBLE);
                    stopFab.setVisibility(View.VISIBLE);
                }
            }
        };
        navController.addOnDestinationChangedListener(onDestinationChangedListener);
    }

    @Override
    protected void onDestroy() {
        if (navController != null && onDestinationChangedListener != null) {
            navController.removeOnDestinationChangedListener(onDestinationChangedListener);
        }
        super.onDestroy();
    }

    // Toggle perform workout from PerformExerciseFragment
    public void setPerformingWorkout(boolean performingWorkout) {
        isPerformingWorkout = performingWorkout;
    }
}