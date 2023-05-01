package com.example.finalprojectgymapp.ui.workout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.adapter.WorkoutAdapter;
import com.example.finalprojectgymapp.databinding.FragmentMyPlansBinding;
import com.example.finalprojectgymapp.dialog.AddWorkoutDialog;
import com.example.finalprojectgymapp.model.Workout;
import com.example.finalprojectgymapp.dataviewmodel.WorkoutsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyPlansFragment extends Fragment implements WorkoutAdapter.OnWorkoutItemClickListener {

    @Override
    public void onWorkoutItemClick(Workout workout) {
        // Get the NavController from the fragment, setup nav and pass argument to next fragment
        NavController navController = NavHostFragment.findNavController(this);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PASSED_WORKOUT_KEY, workout);

        navController.navigate(R.id.action_navigation_workout_item_to_planListFragment, bundle);
    }

    private static final String TAG = "MyPlansFragment";
    public static final String MY_PLANS_KEY = "myPlansKey";
    public static final String PASSED_WORKOUT_KEY = "workout";

    private WorkoutsViewModel workoutsViewModel;

    private FragmentMyPlansBinding binding;

    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private Button addButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        workoutsViewModel = new ViewModelProvider(requireActivity()).get(WorkoutsViewModel.class);
        binding = FragmentMyPlansBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialise Views
        initViews();

        // Set new view data
        workoutsViewModel.getAllWorkouts().observe(getViewLifecycleOwner(), new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                if (workouts != null && workouts.size() > 0){
                    // Switch View for item listed, and not empty
                    binding.nestedScrollView.setVisibility(View.VISIBLE);
                    binding.txtNoPlan.setVisibility(View.GONE);

                    adapter.setWorkouts((ArrayList<Workout>) workouts);
                } else {
                    // List is empty
                    binding.nestedScrollView.setVisibility(View.GONE);
                    binding.txtNoPlan.setVisibility(View.VISIBLE);
                }
            }
        });

        // Create workout dialog
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWorkoutDialog dialog = new AddWorkoutDialog();
                dialog.show(getParentFragmentManager(), "add workout dialog");
            }
        });

        return root;
    }

    public void initViews(){
        Log.d(TAG, "initViews: called");
        addButton = binding.btnAddPlan;

        //Setup adapter -> list workout list
        recyclerView = binding.plansRecView;

        adapter = new WorkoutAdapter(this);

        recyclerView.setAdapter(adapter);
        if(getContext()!= null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}