package com.example.finalisimofinalandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> tasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewName.setText(currentTask.getName());
        holder.textViewDate.setText(currentTask.getDate());
        holder.textViewCategory.setText(currentTask.getCategory());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }
    public void deleteTask(Task task) {
        tasks.remove(task);
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDate;
        private TextView textViewCategory;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewTaskName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewCategory = itemView.findViewById(R.id.textViewTaskCategory);
        }
    }
}
