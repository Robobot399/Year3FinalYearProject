package com.example.finalprojectgymapp.ui.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalprojectgymapp.MainActivity;
import com.example.finalprojectgymapp.R;
import com.example.finalprojectgymapp.databinding.FragmentPerformExerciseBinding;
import com.example.finalprojectgymapp.viewmodel.TrackWorkoutViewModel;

public class PerformExerciseFragment extends Fragment {

    private FragmentPerformExerciseBinding binding;
    private TrackWorkoutViewModel trackWorkoutViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setPerformingWorkout(true);
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Keep this fragment alive, navigate back to previous fragment
                NavController navController = NavHostFragment.findNavController(PerformExerciseFragment.this);
                navController.popBackStack();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Keep instance of ViewModel alive with requireActivity to pass data to EndWorkoutFragment
        trackWorkoutViewModel = new ViewModelProvider(requireActivity()).get(TrackWorkoutViewModel.class);
        binding = FragmentPerformExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Initialise Views
        initViews();

        // CODE
        trackWorkoutViewModel.getStopWorkout().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                trackWorkoutViewModel.clearStopWorkout();
                endWorkout();
            }
        });

        // log new exercise with viewModel.addExerciseLog(...) or a similar method
        //endWorkout(); -> call when clicking end button. TODO: implement a end workout button
        return root;
    }


    private void initViews(){

    }

    private void endWorkout(){
        NavController navController = NavHostFragment.findNavController(this);
        navController.popBackStack(R.id.performExerciseFragment, true);
        navController.navigate(R.id.action_performExerciseFragment_to_endWorkoutFragment);
    }
}
