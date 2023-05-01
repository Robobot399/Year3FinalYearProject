package com.example.finalprojectgymapp.ui.workout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.databinding.FragmentEndWorkoutBinding;
import com.example.finalprojectgymapp.databinding.FragmentPerformExerciseBinding;
import com.example.finalprojectgymapp.viewmodel.TrackWorkoutViewModel;


public class EndWorkoutFragment extends Fragment {

    private FragmentEndWorkoutBinding binding;
    private TrackWorkoutViewModel trackWorkoutViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        trackWorkoutViewModel = new ViewModelProvider(requireActivity()).get(TrackWorkoutViewModel.class);
        binding = FragmentEndWorkoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialise Views
        initViews();

        // CODE
        // Retrieve exerciseLogs from viewModel as required


        return root;
    }

    public void initViews(){

    }

    private void closeWorkout(){
        NavController navController = NavHostFragment.findNavController(this);
        navController.popBackStack(navController.getGraph().getStartDestinationId(), false);
        navController.navigate(R.id.action_endWorkoutFragment_to_navigation_history_item);
    }
}