package com.example.finalisimofinalandroid;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_item);

        String taskName = getIntent().getStringExtra("TASK_NAME");
        long taskDateTimeMillis = getIntent().getLongExtra("TASK_DATE_TIME", -1);
        String taskCategory = getIntent().getStringExtra("TASK_CATEGORY");

        TextView textViewTaskName = findViewById(R.id.textViewTaskName);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewTaskCategory = findViewById(R.id.textViewTaskCategory);

        textViewTaskName.setText(taskName);

        if (taskDateTimeMillis != -1) {
            // Convertir el timestamp a un objeto Date
            Date date = new Date(taskDateTimeMillis);

            // Formatear la fecha y hora
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String formattedDate = dateFormat.format(date);

            textViewDate.setText(formattedDate);
        } else {
            textViewDate.setText("No Date Selected");
        }

        textViewTaskCategory.setText(taskCategory);
    }
}
