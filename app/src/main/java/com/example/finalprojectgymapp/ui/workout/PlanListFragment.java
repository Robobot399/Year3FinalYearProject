package com.example.finalprojectgymapp.ui.workout;

import static com.example.finalprojectgymapp.ui.workout.MyPlansFragment.PASSED_WORKOUT_KEY;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.adapter.ExerciseItemAdapter;
import com.example.finalprojectgymapp.databinding.FragmentPlanListBinding;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseDbViewModel;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseDbViewModelFactory;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseItemViewModel;
import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.ExerciseItem;
import com.example.finalprojectgymapp.model.ExerciseItemWithExercise;
import com.example.finalprojectgymapp.model.Workout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlanListFragment extends Fragment {

    private ExerciseItemViewModel exerciseItemViewModel;
    private ExerciseDbViewModel exerciseDbViewModel;

    private FragmentPlanListBinding binding;

    private MaterialButton addExerciseMaterialButton;
    private TextView numExerciseTextView;

    private RecyclerView recyclerView;
    private ExerciseItemAdapter exerciseItemAdapter;

    private Workout workout;
    private static final String TAG = "PlanListFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Set ViewModelProvider to 'this' Fragment, so ViewModel scoped to the Fragment and not follow Activity LifeCycle
        ExerciseDbViewModelFactory viewModelFactory = new ExerciseDbViewModelFactory(requireActivity().getApplication());
        exerciseDbViewModel = new ViewModelProvider(this, viewModelFactory).get(ExerciseDbViewModel.class);
        exerciseItemViewModel = new ViewModelProvider(this).get(ExerciseItemViewModel.class);

        binding = FragmentPlanListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();

        Bundle bundle = getArguments();
        if (null != bundle) {
            workout = bundle.getParcelable(PASSED_WORKOUT_KEY);
        }
        LiveData<List<ExerciseItem>> exerciseItemsLiveData = exerciseItemViewModel.getExerciseItemsByWorkoutId(workout.getId());
        LiveData<List<Exercise>> exercisesLiveData = exerciseDbViewModel.getExercisesByExerciseItemsLiveData(exerciseItemsLiveData);

        // Avoiding overhead and nested observers by implementing MediatorLiveData
        AtomicBoolean itemsLoaded = new AtomicBoolean(false);
        AtomicBoolean exercisesLoaded = new AtomicBoolean(false);

        MediatorLiveData<Pair<List<ExerciseItem>, List<Exercise>>> mediatorLiveData = new MediatorLiveData<>();
        // Observe changes in ExerciseItem list
        mediatorLiveData.addSource(exerciseItemsLiveData, exerciseItems -> {
            List<Exercise> exercises = exercisesLiveData.getValue();
            if (exercises != null && itemsLoaded.compareAndSet(false, true)) {
                mediatorLiveData.setValue(new Pair<>(exerciseItems, exercises));
            }
        });
        // Observe changes in Exercise list
        mediatorLiveData.addSource(exercisesLiveData, exercises -> {
            List<ExerciseItem> exerciseItems = exerciseItemsLiveData.getValue();
            if (exerciseItems != null && exercisesLoaded.compareAndSet(false, true)) {
                mediatorLiveData.setValue(new Pair<>(exerciseItems, exercises));
            }
        });
        mediatorLiveData.observe(getViewLifecycleOwner(), data -> {
            List<ExerciseItem> exerciseItems = data.first;
            List<Exercise> exercises = data.second;
            List<ExerciseItemWithExercise> exerciseItemWithExercises = exerciseDbViewModel.getExerciseItemWithExercises(exerciseItems, exercises);

            if (exerciseItemWithExercises != null && exerciseItemWithExercises.size() > 0) {
                // LIST is not empty
                binding.nestedScrollViewExercise.setVisibility(View.VISIBLE);
                binding.noExerciseTextView.setVisibility(View.GONE);
                exerciseItemAdapter.setExerciseItemWithExercises((ArrayList<ExerciseItemWithExercise>) exerciseItemWithExercises);
            } else {
                // LIST is empty
                binding.nestedScrollViewExercise.setVisibility(View.GONE);
                binding.noExerciseTextView.setVisibility(View.VISIBLE);
            }
        });

        // Navigate to search exercise activity
        addExerciseMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchActivity();
            }
        });
        // TODO: fix binding for adapter, to display the corresponding exercise name for exercise_item
        // TODO: modify action bar, allow EDIT button for Exercise item
        // TODO: add 'start workout' button, toggle visibility based on MainActivity reading it's online status

        return root;
    }

    private void initViews() {
        addExerciseMaterialButton = binding.addExerciseMaterialButton;
        numExerciseTextView = binding.numExerciseTextView;

        recyclerView = binding.exercisesRecView;
        exerciseItemAdapter = new ExerciseItemAdapter();
        recyclerView.setAdapter(exerciseItemAdapter);
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void goToSearchActivity() {
        NavController navController = NavHostFragment.findNavController(this);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PASSED_WORKOUT_KEY, workout);

        navController.navigate(R.id.action_planListFragment_to_searchExerciseFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Hide bottom nav bar when fragment is visible
        BottomNavigationView bottomNavBar = getActivity().findViewById(R.id.bottom_nav_view);
        bottomNavBar.clearAnimation();
        bottomNavBar.animate().translationY(+bottomNavBar.getHeight()).setInterpolator(new AccelerateInterpolator(1)); // animate disappear
    }

    @Override
    public void onPause() {
        super.onPause();
        /// Show bottom nav bar when fragment is not visible
        BottomNavigationView bottomNavBar = getActivity().findViewById(R.id.bottom_nav_view);
        bottomNavBar.clearAnimation();
        bottomNavBar.animate().translationY(0).setInterpolator(new AccelerateInterpolator(1)); // animate appear
    }
}