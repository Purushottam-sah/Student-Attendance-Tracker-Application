package com.example.smartstudentattendancetracker.models;

public class Note {

    private int id;
    private String title;
    private String description;
    private String subjectName;

    public Note(int id, String title, String description, String subjectName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subjectName = subjectName;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubjectName() { return subjectName; }
}