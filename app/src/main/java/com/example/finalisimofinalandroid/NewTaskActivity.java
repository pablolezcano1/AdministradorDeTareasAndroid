package com.example.finalisimofinalandroid;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewTaskActivity extends AppCompatActivity {

    private EditText editTextTaskName;
    private Button buttonTaskDate;
    private Spinner spinnerCategory;
    private Button buttonSaveTask;
    private Button buttonAddCategory;
    private ArrayAdapter<String> adapter;
    private List<String> categories;
    private CheckBox checkBoxReminder;
    private LocalDateTime selectedDateTime;

    private static final int REQUEST_CODE_NEW_CATEGORY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        NotificationHelper.createNotificationChannel(this);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        buttonTaskDate = findViewById(R.id.buttonTaskDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);
        checkBoxReminder = findViewById(R.id.checkBoxReminder);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> savedCategories = prefs.getStringSet("categories", new HashSet<String>());
        categories = new ArrayList<>(savedCategories);
        if (categories.isEmpty()) {
            categories.add("Front-End");
            categories.add("Back-End");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        buttonSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTaskActivity.this, NewCategoryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_CATEGORY);
            }
        });
    }

    private void showDateTimePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        showTimePicker(year, monthOfYear, dayOfMonth);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            selectedDateTime = LocalDateTime.of(year, month + 1, day, hourOfDay, minute);
                        }
                        // Formatear la fecha y hora en una cadena legible
                        DateTimeFormatter formatter = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        }
                        String formattedDateTime = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formattedDateTime = selectedDateTime.format(formatter);
                        }
                        buttonTaskDate.setText(formattedDateTime);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void saveTask() {
        String taskName = editTextTaskName.getText().toString();
        String taskCategory = spinnerCategory.getSelectedItem().toString();
        boolean isReminderActive = checkBoxReminder.isChecked();

        if (selectedDateTime == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("TASK_NAME", taskName);
        long dateTimeMillis = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeMillis = selectedDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            intent.putExtra("TASK_DATE_TIME", dateTimeMillis);
        }
        intent.putExtra("TASK_CATEGORY", taskCategory);
        intent.putExtra("TASK_REMINDER", isReminderActive);
        setResult(RESULT_OK, intent);

        if (isReminderActive) {
            scheduleReminder(dateTimeMillis, taskName);
        }

        finish();
    }

    private void scheduleReminder(long triggerAtMillis, String taskName) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("TASK_NAME", taskName);
        int requestCode = (int) System.currentTimeMillis(); // Unique request code
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEW_CATEGORY && resultCode == RESULT_OK && data != null) {
            String newCategory = data.getStringExtra("NEW_CATEGORY");
            if (newCategory != null && !categories.contains(newCategory)) {
                categories.add(newCategory);
                adapter.notifyDataSetChanged();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                Set<String> updatedCategories = new HashSet<>(categories);
                editor.putStringSet("categories", updatedCategories);
                editor.apply();
            }
        }
    }
}