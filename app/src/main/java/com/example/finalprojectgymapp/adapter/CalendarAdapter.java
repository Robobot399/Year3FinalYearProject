package com.example.finalprojectgymapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgymapp.databinding.CalendarCellBinding;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    public interface OnItemListener {
        // return our day of the month text
        void onItemClick(int position, String dayText);
    }

    private final OnItemListener onItemListener;

    private ArrayList<String> completeDates;
    private ArrayList<String> daysOfMonth;
    private ArrayList<String> workoutLogDates;

    public CalendarAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CalendarCellBinding binding = CalendarCellBinding.inflate(layoutInflater, parent, false);

        // Modify cells to be sized 1/6th of the entire view of our calendar
        ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        return new CalendarViewHolder(binding, this.onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayInMonth = daysOfMonth.get(position);
        String day;

        if(!dayInMonth.equals("")){
            String[] parts = dayInMonth.split("-");
            day = parts[2];
        } else {
            day = "";
        }
        holder.binding.cellDayText.setText(day);

        // Check if current cell date exist in list of workout logs, and highlights the cell
        if (workoutLogDates != null) {
            String currentCellDate = daysOfMonth.get(position);
            if (!currentCellDate.equals("") && workoutLogDates.contains(currentCellDate)) {
                // Highlight the cell
                holder.binding.cellDayText.setBackgroundColor(Color.parseColor("#FFC107"));
            } else {
                // Reset cell background back to default
                holder.binding.cellDayText.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public void setDaysOfMonth(ArrayList<String> daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
        notifyDataSetChanged();

    }

    public void setWorkoutLogDates(ArrayList<String> workoutLogDates) {
        this.workoutLogDates = workoutLogDates;
        notifyDataSetChanged();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CalendarCellBinding binding;
        private final CalendarAdapter.OnItemListener onItemListener;

        public CalendarViewHolder(@NonNull CalendarCellBinding binding, CalendarAdapter.OnItemListener onItemListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onItemListener = onItemListener;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getBindingAdapterPosition(), (String) binding.cellDayText.getText());
        }
    }
}
