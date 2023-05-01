package com.example.finalprojectgymapp.ui.exercise;

import static com.example.finalprojectgymapp.ui.exercise.ExerciseFragment.PASSED_EXERCISE_KEY;
import static com.example.finalprojectgymapp.ui.workout.MyPlansFragment.PASSED_WORKOUT_KEY;

import android.os.Bundle;
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
import androidx.appcompat.widget.SearchView;
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
import com.example.finalprojectgymapp.adapter.ExerciseAdapter;
import com.example.finalprojectgymapp.databinding.FragmentSearchExerciseBinding;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseDbViewModel;
import com.example.finalprojectgymapp.dataviewmodel.ExerciseDbViewModelFactory;
import com.example.finalprojectgymapp.model.Exercise;
import com.example.finalprojectgymapp.model.Workout;
import com.example.finalprojectgymapp.viewmodel.SearchExerciseViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchExerciseFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    @Override
    public void onExercise(Exercise exercise) {
        NavController navController = NavHostFragment.findNavController(SearchExerciseFragment.this);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PASSED_EXERCISE_KEY, exercise);
        if (selectedWorkout != null) {
            bundle.putParcelable(PASSED_WORKOUT_KEY, selectedWorkout);
        }
        navController.navigate(R.id.action_searchExerciseFragment_to_exerciseFragment, bundle);
    }

    private ExerciseDbViewModel exerciseDbViewModel;
    private SearchExerciseViewModel searchExerciseViewModel;
    private FragmentSearchExerciseBinding binding;
    private ExerciseAdapter adapter;
    private Workout selectedWorkout;

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
        ExerciseDbViewModelFactory viewModelFactory = new ExerciseDbViewModelFactory(requireActivity().getApplication());
        exerciseDbViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(ExerciseDbViewModel.class);
        searchExerciseViewModel = new ViewModelProvider(requireActivity()).get(SearchExerciseViewModel.class);

        binding = FragmentSearchExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();

        // If adding exercise from selected Workout
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedWorkout = bundle.getParcelable(PASSED_WORKOUT_KEY);
        }

        // TODO: present loading screen while obtaining list
        searchExerciseViewModel.getExercisesResult().observe(getViewLifecycleOwner(), new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exerciseList) {
                adapter.setExercises((ArrayList<Exercise>) exerciseList);
            }
        });

        // TODO: create muscle target catalogue
        return root;
    }

    public void initViews() {
        // setup adapter
        adapter = new ExerciseAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        if (getContext() != null) {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void setFragmentToolbar() {
        Toolbar toolbar = binding.searchExerciseToolbar;
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((MainActivity)requireActivity()).getSupportActionBar();
        if(actionBar != null) {
            // set title
            actionBar.setTitle("Search Exercise");
        }
    }

    private void setToolbarMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu);
                // Obtain toolbar items
                MenuItem searchItem = menu.findItem(R.id.search_action);
                SearchView searchView = (SearchView) searchItem.getActionView();

                // Implements search filter for exercises by name
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (query == null) query = "";
                        binding.recyclerView.scrollToPosition(0);
                        searchExerciseViewModel.searchExercises(query);
                        searchView.clearFocus();
                        return true;
                    }

                    // execute search while typing
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText == null || newText.isEmpty())
                            searchExerciseViewModel.clearSearch();
                        return false;
                    }
                });
                searchView.setQueryHint("Search Exercise...");
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.search_action) {
                    return true;
                } else if (menuItem.getItemId() == R.id.filter_action) {
                    // TODO: implement filter catalogue
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