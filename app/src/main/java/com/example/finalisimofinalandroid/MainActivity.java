package com.example.finalisimofinalandroid;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private static final int REQUEST_CODE_NEW_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1);
            }
        }
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        LinearLayout taskContainer = findViewById(R.id.taskContainer);
        Button buttonNewTask = findViewById(R.id.buttonNewTask);

        // Observa los cambios en la lista de tareas
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                updateTaskViews(tasks);
            }
        });

        buttonNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_TASK);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEW_TASK && resultCode == RESULT_OK && data != null) {
            String taskName = data.getStringExtra("TASK_NAME");
            long taskDateTime = data.getLongExtra("TASK_DATE_TIME", -1);
            String taskCategory = data.getStringExtra("TASK_CATEGORY");
            boolean isReminderActive = data.getBooleanExtra("TASK_REMINDER", false);

            // Convert the long to a formatted date string
            String taskDate = "No Date Selected";
            if (taskDateTime != -1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                taskDate = sdf.format(new Date(taskDateTime));
            }

            Task newTask = new Task(taskName, taskDate, taskCategory, isReminderActive);
            taskViewModel.insertTask(newTask);
        }
    }

    private void updateTaskViews(List<Task> tasks) {
        LinearLayout taskContainer = findViewById(R.id.taskContainer);
        taskContainer.removeAllViews();
        int taskNumber = 1; // Inicializa el contador de tareas

        for (Task task : tasks) {
            View taskView = getLayoutInflater().inflate(R.layout.task_item, taskContainer, false);

            Button buttonTaskName = taskView.findViewById(R.id.buttonTaskName);
            buttonTaskName.setText("Tarea " + taskNumber);
            buttonTaskName.setTag(task);

            TextView textViewTaskName = taskView.findViewById(R.id.textViewTaskName);
            TextView textViewTaskDate = taskView.findViewById(R.id.textViewDate);
            TextView textViewTaskCategory = taskView.findViewById(R.id.textViewTaskCategory);

            textViewTaskName.setText(task.getName());
            textViewTaskDate.setText(task.getDate());
            textViewTaskCategory.setText(task.getCategory());
            Button buttonTaskStatus = taskView.findViewById(R.id.buttonTaskStatus);


            buttonTaskStatus.setText(task.getStatus());
            switch (task.getStatus()) {
                case Task.STATUS_PENDING:
                    buttonTaskStatus.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    break;
                case Task.STATUS_IN_PROGRESS:
                    buttonTaskStatus.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    break;
                case Task.STATUS_COMPLETED:
                    buttonTaskStatus.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    break;
            }

            buttonTaskStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (task.getStatus()) {
                        case Task.STATUS_PENDING:
                            task.setStatus(Task.STATUS_IN_PROGRESS);
                            break;
                        case Task.STATUS_IN_PROGRESS:
                            task.setStatus(Task.STATUS_COMPLETED);
                            break;
                        case Task.STATUS_COMPLETED:
                            task.setStatus(Task.STATUS_PENDING);
                            break;
                    }
                    updateTaskViews(taskViewModel.getAllTasks().getValue()); // Actualiza la vista
                }
            });

            ImageButton buttonDeleteTask = taskView.findViewById(R.id.buttonDeleteTask);
            buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTask(task);
                }
            });

            taskContainer.addView(taskView);
            taskNumber++;
        }
    }
    private void deleteTask(Task task) {
        List<Task> tasks = taskViewModel.getAllTasks().getValue();
        if (tasks != null) {
            tasks.remove(task);
            taskViewModel.setTasks(tasks);
        }
    }
}
