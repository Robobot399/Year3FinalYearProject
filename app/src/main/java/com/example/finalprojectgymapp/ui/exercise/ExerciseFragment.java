package com.example.finalprojectgymapp.ui.exercise;

import static com.example.finalprojectgymapp.ui.workout.MyPlansFragment.PASSED_WORKOUT_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.databinding.FragmentExerciseBinding;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseDbViewModel;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseDbViewModelFactory;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseItemViewModel;
import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.ExerciseItem;
import com.example.finalprojectgymapp.model.Workout;
import com.example.finalprojectgymapp.util.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExerciseFragment extends Fragment {
    private ExerciseDbViewModel exerciseDbViewModel;
    private ExerciseItemViewModel exerciseItemViewModel;
    private FragmentExerciseBinding binding;

    private Exercise chosenExercise;
    private Workout selectedWorkout;

    private TextView historyButtonTextView;
    private RelativeLayout relButtonContainer;
    private Button addExerciseButton;

    public static final String PASSED_EXERCISE_KEY = "exercise";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ExerciseDbViewModelFactory viewModelFactory = new ExerciseDbViewModelFactory(requireActivity().getApplication());
        exerciseDbViewModel = new ViewModelProvider(this, viewModelFactory).get(ExerciseDbViewModel.class);
        exerciseItemViewModel = new ViewModelProvider(this).get(ExerciseItemViewModel.class);
        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();

        // Obtain selected Exercise
        Bundle bundle = getArguments();
        if (null != bundle) {
            chosenExercise = bundle.getParcelable(PASSED_EXERCISE_KEY);
            selectedWorkout = bundle.getParcelable(PASSED_WORKOUT_KEY); // null if not adding from PlanListFragment
        }
        binding.setExercise(chosenExercise);

        if (selectedWorkout == null) {
            relButtonContainer.setVisibility(View.GONE);
        } else {
            relButtonContainer.setVisibility(View.VISIBLE);
            relButtonContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addExerciseToPlan();
                }
            });
        }

        // Navigate to ExerciseHistoryFragment, with chosen Exercise
        historyButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(ExerciseFragment.this);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PASSED_EXERCISE_KEY, chosenExercise);
                navController.navigate(R.id.action_exerciseFragment_to_exerciseHistoryFragment, bundle);
            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedWorkout != null){
                    addExerciseToPlan();
                }
            }
        });

        return root;
    }

    private void initViews() {
        historyButtonTextView = binding.historyButtonTextView;
        relButtonContainer = binding.relButtonContainer;
        addExerciseButton = binding.addExerciseButton;
    }

    private void addExerciseToPlan() {
        // Creates ExerciseItem for this Exercise, for our Workout
        exerciseItemViewModel.insert(new ExerciseItem(selectedWorkout.getId(), chosenExercise.getId(),
                Utils.getInstance(requireContext()).getSetAmount(),
                Utils.getInstance(requireContext()).getRepAmount()));

        // Remove from backstack this fragment, and the SearchExercise fragment upon adding a exercise
        NavController navController = NavHostFragment.findNavController(ExerciseFragment.this);
        navController.popBackStack(R.id.exerciseFragment, true);
        navController.popBackStack(R.id.searchExerciseFragment, true);

        navController.popBackStack();
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
