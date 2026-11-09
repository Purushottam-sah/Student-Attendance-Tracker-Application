package com.example.smartstudentattendancetracker.models;

public class Notice {
    private int id;
    private String title;
    private String message;
    private String date;
    private String postedBy;

    public Notice(int id, String title, String message, String date, String postedBy) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.date = date;
        this.postedBy = postedBy;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
    public String getPostedBy() { return postedBy; }
}