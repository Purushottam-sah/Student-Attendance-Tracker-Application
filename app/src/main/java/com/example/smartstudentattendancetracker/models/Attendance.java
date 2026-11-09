package com.example.smartstudentattendancetracker.models;

public class Attendance {

    private int id;
    private int subjectId;
    private String subjectName;
    private String date;
    private String status;

    public Attendance(int id, int subjectId, String subjectName, String date, String status) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}