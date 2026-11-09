package com.example.smartstudentattendancetracker.models;

public class Notification {
    private String title;
    private String message;
    private String time;
    private boolean isRead;
    private int type; // 0: Checkmark, 1: Warning, 2: Notice, 3: Timetable

    public Notification(String title, String message, String time, boolean isRead, int type) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.isRead = isRead;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
    public boolean isRead() { return isRead; }
    public int getType() { return type; }
    
    public void setRead(boolean read) { isRead = read; }
}