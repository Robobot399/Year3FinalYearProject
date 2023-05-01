package com.example.finalprojectgymapp.ui.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalprojectgymapp.adapter.WorkoutViewPagerAdapter;
import com.example.finalprojectgymapp.databinding.FragmentWorkoutsBinding;
import com.example.finalprojectgymapp.dataviewmodel.WorkoutsViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WorkoutFragments extends Fragment {

    private FragmentWorkoutsBinding binding;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private WorkoutViewPagerAdapter workoutViewPagerAdapter;

    private static final String TAG = "WorkoutFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initViews();

        // Connect the TabLayout with the ViewPager2
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            // Assign tab's title
            if (position == 0) {
                tab.setText("Recommend plan");
            } else if (position == 1) {
                tab.setText("My Plans");
            }
        }).attach();

        return root;
    }

    public void initViews() {
        // Setup Tab Layout
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager;
        workoutViewPagerAdapter = new WorkoutViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager2.setAdapter(workoutViewPagerAdapter);
    }
}