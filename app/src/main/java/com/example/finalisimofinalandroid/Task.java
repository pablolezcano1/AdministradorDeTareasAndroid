package com.example.finalisimofinalandroid;

public class Task {
    private String name;
    private String date;
    private String category;
    private boolean isCompleted;
    private String status;  // Añadido para el estado de la tarea
    private boolean isReminderActive;

    public static final String STATUS_PENDING = "Pendiente";
    public static final String STATUS_IN_PROGRESS = "En curso";
    public static final String STATUS_COMPLETED = "Completa";
    public Task(String name, String date, String category, boolean IsReminderActive) {
        this.name = name;
        this.date = date;
        this.category = category;
        this.isCompleted = false;
        this.status = STATUS_PENDING; // Inicialmente, la tarea está pendiente
        this.isReminderActive = isReminderActive; // Establece el estado del recordatorio


    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isReminderActive() { return isReminderActive; }
    public void setReminderActive(boolean reminderActive) { isReminderActive = reminderActive; }
}
