package com.example.finalprojectgymapp.ui.workout;

import static com.example.finalprojectgymapp.ui.workout.MyPlansFragment.PASSED_WORKOUT_KEY;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        // Passing requireActivity in ViewModelProvider binds it to the activity hosting the fragment, allowing shared data between fragments
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
            if (exercises != null) {
                itemsLoaded.set(true);
                mediatorLiveData.setValue(new Pair<>(exerciseItems, exercises));
            }
        });
        // Observe changes in Exercise list
        mediatorLiveData.addSource(exercisesLiveData, exercises -> {
            List<ExerciseItem> exerciseItems = exerciseItemsLiveData.getValue();
            if (exerciseItems != null) {
                exercisesLoaded.set(true);
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
                exerciseItemAdapter.submitList(exerciseItemWithExercises);
            } else {
                // LIST is empty
                binding.nestedScrollViewExercise.setVisibility(View.GONE);
                binding.noExerciseTextView.setVisibility(View.VISIBLE);
            }

            itemsLoaded.set(false);
            exercisesLoaded.set(false);
        });

        // Swipe items to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ExerciseItem exerciseItem = exerciseItemAdapter.getExerciseItemWithExerciseAt(viewHolder.getBindingAdapterPosition()).getExerciseItem();
                exerciseItemViewModel.delete(exerciseItem);
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // Display red background to indicate a delete action
                if (dX > 0) {
                    // Swiping to the right
                    ColorDrawable background = new ColorDrawable(Color.RED);
                    // specify bound for the drawn box
                    background.setBounds(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop(), viewHolder.itemView.getLeft() + ((int) dX), viewHolder.itemView.getBottom());
                    background.draw(canvas);
                } else if (dX < 0) {
                    // Swiping to the left
                    ColorDrawable background = new ColorDrawable(Color.RED);
                    background.setBounds(viewHolder.itemView.getRight() + ((int) dX), viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                    background.draw(canvas);
                }
            }
        }).attachToRecyclerView(recyclerView);

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