package com.example.smartstudentattendancetracker.models;

public class Timetable {
    private int id;
    private String day;
    private String subjectName;
    private String startTime;
    private String endTime;

    public Timetable(int id, String day, String subjectName, String startTime, String endTime) {
        this.id = id;
        this.day = day;
        this.subjectName = subjectName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }
    public String getDay() { return day; }
    public String getSubjectName() { return subjectName; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}