package com.example.smartstudentattendancetracker.utils;

public class AttendanceUtils {

    public static int calculatePercentage(int present, int total) {
        if (total == 0) {
            return 0;
        }
        return (present * 100) / total;
    }

    public static String getStatus(int percentage) {
        if (percentage < 75) {
            return "LOW";
        } else if (percentage < 85) {
            return "AVERAGE";
        } else {
            return "GOOD";
        }
    }

    public static int classesNeeded(int present, int total, int goal) {
        if (total == 0) return 0;
        if ((present * 100) / total >= goal) return 0;

        int count = 0;
        int tempPresent = present;
        int tempTotal = total;

        while ((tempPresent * 100) / tempTotal < goal) {
            tempPresent++;
            tempTotal++;
            count++;
        }
        return count;
    }

    public static int classesCanMiss(int present, int total, int goal) {
        if (total == 0) return 0;
        if ((present * 100) / total < goal) return 0;

        int count = 0;
        int tempTotal = total;

        while ((present * 100) / (tempTotal + 1) >= goal) {
            tempTotal++;
            count++;
        }
        return count;
    }
}