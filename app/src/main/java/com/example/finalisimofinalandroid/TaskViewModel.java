package com.example.finalisimofinalandroid;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;
    private final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public void insertTask(Task task) {
        repository.insertTask(task);
    }
    public void setTasks(List<Task> tasks) {
        repository.setTasks(tasks);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}
