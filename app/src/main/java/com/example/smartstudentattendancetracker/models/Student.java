package com.example.smartstudentattendancetracker.models;

public class Student {
    private int id;
    private String name;
    private String email;
    private String rollNumber;
    private String className;

    public Student(int id, String name, String email, String rollNumber, String className) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.rollNumber = rollNumber;
        this.className = className;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRollNumber() { return rollNumber; }
    public String getClassName() { return className; }
}