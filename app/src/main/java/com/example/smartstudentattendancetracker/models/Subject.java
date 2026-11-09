package com.example.smartstudentattendancetracker.models;

public class Subject {
    private int id;
    private String name;
    private int totalClass;
    private int presentClass;

    public Subject(int id, String name, int totalClass, int presentClass) {
        this.id = id;
        this.name = name;
        this.totalClass = totalClass;
        this.presentClass = presentClass;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalClass() {
        return totalClass;
    }

    public int getPresentClass() {
        return presentClass;
    }
}