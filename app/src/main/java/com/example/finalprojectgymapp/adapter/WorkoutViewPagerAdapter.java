package com.example.finalprojectgymapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalprojectgymapp.ui.workout.MyPlansFragment;
import com.example.finalprojectgymapp.ui.workout.RecommendPlanFragment;

public class WorkoutViewPagerAdapter extends FragmentStateAdapter {
    public WorkoutViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RecommendPlanFragment();
            case 1:
                return new MyPlansFragment();
            default:
                return new RecommendPlanFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
