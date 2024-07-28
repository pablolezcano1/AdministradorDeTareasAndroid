package com.example.finalisimofinalandroid;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final MutableLiveData<List<Task>> taskList;

    public TaskRepository(Application application) {
        taskList = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskList;
    }

    public void insertTask(Task task) {
        List<Task> currentList = taskList.getValue();
        if (currentList != null) {
            currentList.add(task);
            taskList.setValue(currentList);
        }
    }

    public void deleteTask(Task task) {
        List<Task> currentList = taskList.getValue();
        if (currentList != null) {
            currentList.remove(task);
            taskList.setValue(currentList);
        }
    }
    public void setTasks(List<Task> tasks) {
    }

    public void updateTask(Task task) {
        List<Task> currentList = taskList.getValue();
        if (currentList != null) {
            int index = currentList.indexOf(task);
            if (index != -1) {
                currentList.set(index, task);
                taskList.setValue(currentList);
            }
        }
    }
}
