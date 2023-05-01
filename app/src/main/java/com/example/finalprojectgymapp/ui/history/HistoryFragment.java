package com.example.finalprojectgymapp.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.adapter.CalendarAdapter;
import com.example.finalprojectgymapp.databinding.FragmentHistoryBinding;
import com.example.finalprojectgymapp.dataviewmodel.WorkoutLogViewModel;
import com.example.finalprojectgymapp.model.WorkoutLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment implements CalendarAdapter.OnItemListener {

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private WorkoutLogViewModel workoutLogViewModel;
    private FragmentHistoryBinding binding;

    private TextView monthYearTextView;
    private RecyclerView calendarRecyclerView;
    private Date selectedDate;
    private CalendarAdapter calendarAdapter;

    private ImageView previousMonthButton, nextButtonMonth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        workoutLogViewModel = new ViewModelProvider(this).get(WorkoutLogViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialise Views
        initViews();
        selectedDate = new Date();
        getWorkoutLogDates();

        // Calls setMonthView after layout has been fully drawn, to allow calendar to be seen
        root.post(new Runnable() {
            @Override
            public void run() {
                setMonthView();
            }
        });

        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction(v);
            }
        });
        nextButtonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction(v);
            }
        });

        return root;
    }

    private void initViews() {
        monthYearTextView = binding.monthYearTextView;
        calendarRecyclerView = binding.calendarRecyclerView;
        previousMonthButton = binding.previousMonthButton;
        nextButtonMonth = binding.nextMonthButton;
        // Setup Adapter
        calendarAdapter = new CalendarAdapter(this);
    }

    // Initialise calendar to display
    private void setMonthView() {
        // Retrieve current month and year to calendar
        monthYearTextView.setText(monthYearFromDate(selectedDate));

        // Assign adapter to initial list of days of the current month of now
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        calendarAdapter.setDaysOfMonth(daysInMonth);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 7); // assign 7 days columns
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    // Get all days from current month
    private ArrayList<String> daysInMonthArray(Date date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // returns 31

        // Retrieve integer of days between 0 and 7
        calendar.set(Calendar.DAY_OF_MONTH, 1); // set day of month to 1, e.g. March 1st
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // retrieve how many days it is from the starting point of Sunday

        // To match with workout log date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                // Create empty cell if before the first day of the month, or after the 29th/30th/31st of the month
                // + dayOfWeek to add offset from initial starting point of 1st day in the month
                daysInMonthArray.add("");
            } else {
                // Else assign day into the month
                calendar.set(Calendar.DAY_OF_MONTH, i - dayOfWeek);
                String formattedDate = sdf.format(calendar.getTime());
                daysInMonthArray.add(String.valueOf(formattedDate));
            }
        }
        return daysInMonthArray;
    }

    // Get month name and year from current time
    private String monthYearFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    // Called via the previous button icon
    private void previousMonthAction(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.add(Calendar.MONTH, -1);

        selectedDate = calendar.getTime();
        setMonthView();
    }

    // Called via the next button icon
    private void nextMonthAction(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.add(Calendar.MONTH, 1);
        selectedDate = calendar.getTime();
        setMonthView();
    }

    private void getWorkoutLogDates() {
        // Retrieves the list of workout logs, and extract their dates. Then adds to list of workoutLogDates
        workoutLogViewModel.getAllWorkoutLogs().observe(getViewLifecycleOwner(), new Observer<List<WorkoutLog>>() {
            @Override
            public void onChanged(List<WorkoutLog> workoutLogs) {
                ArrayList<String> workoutLogDates = new ArrayList<>();

                for(WorkoutLog workoutLog: workoutLogs){
                    String dateString = workoutLog.getWorkoutDate();
                    workoutLogDates.add(dateString);
                }
                calendarAdapter.setWorkoutLogDates(workoutLogDates);
            }
        });
    }
}