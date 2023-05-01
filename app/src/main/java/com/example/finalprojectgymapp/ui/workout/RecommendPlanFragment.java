package com.example.finalprojectgymapp.ui.workout;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectgymapp.databinding.FragmentRecommendPlanBinding;

public class RecommendPlanFragment extends Fragment {

    private FragmentRecommendPlanBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRecommendPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initViews();
        return root;
    }

    public void initViews(){
    }
}