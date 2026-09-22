# Student-Attendance-Tracker-Application
Smart Student Attendance Tracker is an Android app for managing student attendance with Admin and Student roles. Features include attendance tracking, reports, analytics, 75% low-attendance alerts, notifications, student management, notices, and timetables, built with Java, XML, SQLite, and Material Design.





📱 Smart Student Attendance Tracker

A modern Android-based **Student Attendance Management System** designed to simplify attendance management for administrators and provide students with real-time access to their attendance records, academic notices, timetable, analytics, and attendance alerts.

The application uses **role-based authentication**, providing separate interfaces and functionality for **Admin** and **Student** users. The system automatically calculates attendance percentages and generates alerts when a student's attendance falls below the required **75% threshold**.

📌 Project Overview

The **Smart Student Attendance Tracker** is a mobile application developed for educational institutions to digitally manage student attendance.

Traditional attendance systems often require manual registers or spreadsheets, making attendance tracking time-consuming and increasing the possibility of errors. This application provides a centralized mobile solution where administrators can manage students and attendance, while students can monitor their own attendance and receive important notifications.

The system includes an intelligent attendance monitoring mechanism that identifies students with low attendance and provides appropriate warnings.

 👥 User Roles

The application supports two primary roles:

* 👨‍💼 **Admin**
* 🎓 **Student**

Each role has its own dashboard and permissions.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ---------------------------------------------------------------------

# 🎯 Project Objectives

The main objectives of the project are:

* Digitize the student attendance process.
* Provide secure role-based authentication.
* Allow administrators to manage student information.
* Allow administrators to mark and update attendance.
* Allow students to view their attendance history.
* Automatically calculate attendance percentages.
* Identify students with attendance below 75%.
* Generate low-attendance warnings.
* Notify students when attendance is updated.
* Provide attendance analytics and reports.
* Provide notices and timetable information.
* Reduce manual attendance-management work.
* Provide a simple and modern mobile user interface.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- -------------------------------------------------------------------

# ✨ Key Features

## 🔐 1. Role-Based Authentication

The application provides a common login system with role-based access.

 Admin

Admin users can access:

* Admin Dashboard
* Student Management
* Attendance Management
* Attendance Reports
* Low Attendance Monitoring
* Notice Management
* Timetable Management
* Analytics
* Profile
* Settings

 Student

Students can access:

* Student Dashboard
* Attendance History
* Attendance Analytics
* Low Attendance Alerts
* Notifications
* Notices
* Timetable
* Profile
* Settings

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- --------------------------------------------------------------------

# 👨‍💼 Admin Features

## 1. Admin Dashboard

The admin dashboard provides an overview of the institution's attendance status.

 Dashboard Statistics

* Total Students
* Present Students
* Absent Students
* Students on Leave
* Students Below 75%
* Overall Attendance Percentage

 Quick Actions

* Add Student
* Manage Students
* Mark Attendance
* View Reports
* Manage Notices
* Manage Timetable
* View Analytics

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- --------------------------------------------------------------------

## 2. Student Management

Admin can manage student records using CRUD operations.

 Create

Admin can add new students.

Student information includes:

* Student ID
* Full Name
* Roll Number
* Course
* Semester
* Section
* Email
* Mobile Number
* Password
* Profile Photo

 Read

Admin can:

* View all students.
* View individual student details.
* Search students.

 Update

Admin can update:

* Student name
* Course
* Semester
* Contact information
* Academic information

 Delete

Admin can remove student records when required.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- --------------------------------------------------------------------

# 3. Attendance Management

Admin can mark attendance for students.

 Attendance Status

* ✅ Present
* ❌ Absent
* 🟠 Leave
* ⏰ Late

 Attendance Process

Admin selects:

1. Date
2. Course
3. Semester
4. Subject

The application then displays the student list.

Admin selects the appropriate attendance status for each student and saves the attendance.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- --------------------------------------------------------------------

# 4. Attendance Editing

Admin can:

* View previously recorded attendance.
* Update attendance.
* Correct incorrect attendance.
* Search attendance.
* Filter attendance by date.
* Filter attendance by subject.
* Filter attendance by student.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ---------------------------------------------------------------------

# 5. Attendance Reports

The application provides different attendance reports.

 Reports

* Daily Attendance Report
* Weekly Attendance Report
* Monthly Attendance Report
* Student-wise Attendance Report
* Subject-wise Attendance Report
* Low Attendance Report

 Report Information

Reports can contain:

* Student Name
* Student ID
* Subject
* Total Classes
* Present
* Absent
* Leave
* Late
* Attendance Percentage

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ---------------------------------------------------------------------

# 6. Low Attendance Monitoring ⭐

One of the major features of the application is automatic attendance monitoring.

The system calculates the student's attendance percentage after attendance records are updated.

 Attendance Status

| Attendance   | Status      |
|  ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- |  ------------------------------------------------------------------- ------------------------------------------------------------------- --------------------------------------------------------------------- |
| 75% or above | 🟢 Safe     |
| 60% – 74%    | 🟠 Warning  |
| Below 60%    | 🔴 Critical |

 Example

If a student's attendance becomes:

**72%**

The system identifies the student as having low attendance.

The student receives a warning such as:

> Your attendance is currently 72%, which is below the required 75%. Please attend upcoming lectures regularly.

If attendance becomes critically low:

> Your attendance is currently 58%. Please avoid further absences and contact your faculty advisor if required.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ---------------------------------------------------------------------

# 7. Attendance Recovery Calculator

The system can calculate the number of consecutive classes a student needs to attend to reach 75%.

 Example

Current:

* Total Classes = 40
* Present = 26
* Attendance = 65%

The application calculates the required future attendance and displays a recovery suggestion.

Example:

> You need to attend the next X consecutive classes to reach 75% attendance.

This gives students a clear understanding of how their attendance can improve.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ---------------------------------------------------------------------

# 8. Low Attendance Student List

The Admin Dashboard contains a dedicated section for students below 75%.

Admin can see:

* Student Name
* Student ID
* Course
* Semester
* Attendance Percentage
* Warning/Critical Status

Admin can open the student's detailed attendance report.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ---------------------------------------------------------------------

# 9. Notice Board

Admin can create and manage notices.

 Admin Functions

* Add Notice
* Edit Notice
* Delete Notice
* View Notices

 Notice Categories

* Academic
* Examination
* Assignment
* Holiday
* Event
* General Announcement

Students can view all active notices from their dashboard.

 ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ---------------------------------------------------------------------

# 10. Timetable Management

Admin can manage the class timetable.

 Timetable Information

* Day
* Subject
* Faculty
* Start Time
* End Time
* Classroom

Admin can:

* Add timetable entry
* Edit timetable entry
* Delete timetable entry

Students can view their timetable from the Student Dashboard.

 -------------------------------------------------------------------

# 11. Admin Analytics

The admin can view attendance analytics through charts.

 Analytics

* Overall Attendance
* Monthly Attendance
* Subject-wise Attendance
* Class-wise Attendance
* Highest Attendance
* Lowest Attendance
* Attendance Trends

Charts can include:

* Pie Chart
* Bar Chart
* Line Chart
* Progress Indicators

 -------------------------------------------------------------------

# 🎓 Student Features

## 1. Student Dashboard

The Student Dashboard provides a quick overview of the student's academic attendance.

 Dashboard Information

* Overall Attendance Percentage
* Present Classes
* Absent Classes
* Leave Classes
* Today's Attendance
* Latest Notice
* Attendance Warning

 -------------------------------------------------------------------

# 2. Attendance History

Students can view their complete attendance history.

 Filters

* Subject
* Month
* Semester
* Date

 Attendance Information

| Date   | Subject                        | Status  |
|  ------------------------------------------------------------------- ------------------------------------------------------------------- |  ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- ------------------------------------------------------------------- |  ------------------------------------------------------------------- -------------------------------------------------------------------- |
| 28 Sep | Mobile Application Development | Present |
| 27 Sep | Big Data Analytics             | Absent  |
| 26 Sep | Database Management            | Present |

 -------------------------------------------------------------------

# 3. Student Attendance Analytics

Students can view their attendance through visual analytics.

 Statistics

* Overall Attendance
* Present Percentage
* Absent Percentage
* Leave Percentage
* Subject-wise Attendance
* Monthly Attendance

Charts make attendance easier to understand.

 -------------------------------------------------------------------

# 4. Low Attendance Alert

When attendance falls below 75%, the application displays a warning.

 Example

> ⚠️ Low Attendance Alert
> Your attendance is currently 72%. The required attendance is 75%. Please attend upcoming lectures regularly.

For critical attendance:

> 🚨 Critical Attendance Alert
> Your attendance is currently 58%. You should avoid further absences and contact the faculty advisor if necessary.

 -------------------------------------------------------------------

# 5. Attendance Update Notification ⭐

Whenever the administrator records attendance, the student can receive an attendance notification.

 Present Example

> 📚 Attendance Updated
> Your attendance for Mobile Application Development has been marked as Present.

 Absent Example

> 📚 Attendance Updated
> Your attendance for Mobile Application Development has been marked as Absent. Your current attendance is 74%.

If the updated attendance causes the percentage to fall below 75%, the application can additionally generate a low-attendance warning.

 -------------------------------------------------------------------

# 6. Notification History

Students can view previous notifications.

Notification types include:

* Attendance Updated
* Low Attendance Warning
* Critical Attendance Alert
* New Notice
* Timetable Updated
* Academic Announcement

Notifications can have:

* Read status
* Unread status
* Date
* Time
* Notification type

 -------------------------------------------------------------------

# 7. Student Notice Board

Students can view notices published by the administrator.

They can filter notices according to category.

 -------------------------------------------------------------------

# 8. Student Timetable

Students can view their weekly timetable.

Information includes:

* Day
* Subject
* Faculty
* Time
* Classroom

 -------------------------------------------------------------------

# 9. Student Profile

Students can view their profile information.

 Profile Information

* Student ID
* Name
* Roll Number
* Course
* Semester
* Section
* Email
* Mobile Number

Students can update permitted personal information.

 -------------------------------------------------------------------

# 10. Settings

The application provides basic settings.

 Settings

* Change Password
* Notification Settings
* Dark Mode
* About Application
* Privacy Information
* Logout

 -------------------------------------------------------------------

# ⚙️ Functional Requirements

Functional requirements describe what the system must do.

## Authentication

* The system shall allow users to log in.
* The system shall identify the user's role.
* The system shall redirect users to the appropriate dashboard.
* The system shall validate login credentials.
* The system shall allow users to log out.
* The system shall provide password-change functionality.

## Student Management

* Admin shall be able to add students.
* Admin shall be able to view students.
* Admin shall be able to search students.
* Admin shall be able to update student records.
* Admin shall be able to delete student records.

## Attendance

* Admin shall be able to select a date.
* Admin shall be able to select a subject.
* Admin shall be able to mark attendance.
* Admin shall be able to modify attendance.
* Admin shall be able to view attendance history.
* The system shall calculate attendance percentages.

## Attendance Monitoring

* The system shall monitor attendance automatically.
* The system shall identify attendance below 75%.
* The system shall classify attendance as Safe, Warning, or Critical.
* The system shall calculate attendance recovery requirements.
* The system shall display low-attendance students to administrators.

## Notifications

* The system shall generate attendance-update notifications.
* The system shall generate low-attendance warnings.
* The system shall maintain notification history.
* The system shall display unread notifications.

## Reports

* The system shall generate attendance reports.
* The system shall support daily, weekly, and monthly attendance views.
* The system shall provide student-wise and subject-wise reports.
* The system shall provide low-attendance reports.

## Notice Management

* Admin shall be able to create notices.
* Admin shall be able to update notices.
* Admin shall be able to delete notices.
* Students shall be able to view published notices.

## Timetable

* Admin shall be able to manage timetable entries.
* Students shall be able to view the timetable.

 -------------------------------------------------------------------

# 🔒 Non-Functional Requirements

## 1. Usability

The application should provide:

* Simple navigation
* Clear icons
* Consistent layouts
* Readable text
* Easy-to-understand messages
* Responsive user interface

## 2. Performance

The application should:

* Load screens quickly.
* Perform database operations efficiently.
* Avoid unnecessary processing.
* Handle normal classroom-sized datasets smoothly.

## 3. Security

The application should:

* Restrict admin functionality to authorized users.
* Prevent students from accessing administrative functions.
* Validate login credentials.
* Protect user passwords.
* Validate user input.

> For a production system, passwords should be securely hashed rather than stored as plain text.

## 4. Reliability

The application should:

* Preserve attendance records correctly.
* Prevent accidental duplicate attendance where applicable.
* Handle invalid input gracefully.
* Provide confirmation messages for important operations.

## 5. Maintainability

The project should use:

* Modular code
* Separate activities
* Model classes
* Database helper classes
* Adapter classes
* Reusable UI components

## 6. Scalability

The application should be designed so that future versions can support:

* Multiple teachers
* Multiple departments
* Multiple classes
* Cloud database
* Parent accounts
* QR attendance
* Push notifications

## 7. Compatibility

The application should be designed for modern Android smartphones and tested on multiple screen sizes where possible.

 -------------------------------------------------------------------

# 🛠️ Technologies Used

## Frontend / Android

* **Android Studio**
* **Java**
* **XML**
* **Material Design Components**

## Database

* **SQLite**

## Architecture

* **MVC (Model–View–Controller)**

## UI Components

* RecyclerView
* CardView / Material Card
* Material Buttons
* TextInputLayout
* Bottom Navigation
* Toolbar
* Floating Action Button
* Dialogs
* Progress Indicators
* Search
* Filters

## Data Visualization

**MPAndroidChart**

Used for:

* Pie Charts
* Bar Charts
* Line Charts

## Version Control

* **Git**
* **GitHub**

## Development Environment

* Android Studio
* JDK
* Android SDK
* Emulator / Physical Android Device

 -------------------------------------------------------------------

# 🏗️ System Architecture

The application follows a simple MVC-based structure.


                 ┌─────────────────────┐
                 │     Android UI      │
                 │      XML Views      │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │    Activities /     │
                 │    Controllers      │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │    Model Classes   │
                 │ Student / Attendance│
                 │ Notice / Timetable  │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │    SQLite Database  │
                 └─────────────────────┘


 -------------------------------------------------------------------

# 🗄️ Database Structure

## Admin Table


Admin
├── admin_id
├── name
├── username
└── password
 

## Student Table


Student
├── student_id
├── name
├── roll_no
├── course
├── semester
├── section
├── email
├── mobile
├── password
└── profile_photo


## Attendance Table


Attendance
├── attendance_id
├── student_id
├── subject
├── date
└── status


## Notice Table


Notice
├── notice_id
├── title
├── description
├── category
└── date


## Timetable Table


Timetable
├── timetable_id
├── subject
├── faculty
├── day
├── start_time
├── end_time
└── room
 

## Notification Table


Notification
├── notification_id
├── student_id
├── title
├── message
├── type
├── date
├── time
└── read_status
 

 -------------------------------------------------------------------

# 🔄 Working Process

## Step 1 — Application Launch

The user opens the application.


Splash Screen
       ↓
Login Screen
 

 -------------------------------------------------------------------

## Step 2 — Authentication

The user enters:

* Username / Student ID
* Password

The system validates the credentials and determines the user role.


Login
  ↓
Validate Credentials
  ↓
Identify Role
  ↓
Admin / Student
 

 -------------------------------------------------------------------

# 👨‍💼 Admin Workflow


Admin Login
     ↓
Admin Dashboard
     ↓
Select Module
     │
     ├── Student Management
     │       ↓
     │   Add / Edit / Delete
     │
     ├── Attendance
     │       ↓
     │   Select Date & Subject
     │       ↓
     │   Mark Attendance
     │       ↓
     │   Save
     │
     ├── Reports
     │       ↓
     │   Generate Attendance Report
     │
     ├── Low Attendance
     │       ↓
     │   Identify Students < 75%
     │
     ├── Notices
     │       ↓
     │   Create / Edit / Delete
     │
     └── Timetable
             ↓
         Add / Edit / Delete
 

 -------------------------------------------------------------------

# 🎓 Student Workflow


Student Login
      ↓
Student Dashboard
      ↓
View Attendance
      ↓
Attendance History
      ↓
Attendance Percentage
      ↓
Check Attendance Status
      │
      ├── ≥ 75%
      │      ↓
      │    Safe
      │
      └── < 75%
             ↓
       Warning Notification
             ↓
       Recovery Calculation
 

 -------------------------------------------------------------------

# 🔔 Attendance Notification Workflow


Admin Marks Attendance
          ↓
Attendance Saved
          ↓
Calculate Student Attendance
          ↓
Check Percentage
          │
          ├───────────────┐
          ↓               ↓
      ≥ 75%             < 75%
          ↓               ↓
   Attendance        Low Attendance
    Updated             Alert
          │               │
          └───────┬───────┘
                  ↓
           Student Notification
 

 -------------------------------------------------------------------

# 📊 Attendance Calculation

The application calculates attendance using:


Attendance Percentage =
(Present Classes / Total Conducted Classes) × 100
 

For example:


Present = 30
Total Classes = 40

Attendance =
(30 / 40) × 100

= 75%
 

The system then determines the appropriate attendance status.

 -------------------------------------------------------------------

# 🎨 UI/UX Design

The application uses a modern mobile interface with:

* Material Design
* Blue/purple gradient theme
* Rounded cards
* Consistent icons
* Progress indicators
* Attendance charts
* Bottom navigation
* Search bars
* Filter chips
* Alert cards
* Notification badges
* Responsive layouts

 Main Design Goals

* Clean
* Modern
* Easy to navigate
* Mobile-friendly
* Consistent
* Accessible

 -------------------------------------------------------------------

# 📱 Main Screens

## Admin

1. Splash Screen
2. Admin Login
3. Admin Dashboard
4. Student Management
5. Add Student
6. Student Details
7. Mark Attendance
8. Attendance Reports
9. Low Attendance Students
10. Notice Board
11. Timetable Management
12. Analytics
13. Settings
14. Change Password

## Student

1. Splash Screen
2. Student Login
3. Student Dashboard
4. Attendance History
5. Attendance Analytics
6. Low Attendance Alert
7. Notice Board
8. Timetable
9. Notifications
10. Profile
11. Settings
12. Change Password

 -------------------------------------------------------------------

# 👥 Team Structure

This project is developed by a **2-member team**.

 Member 1 — Admin & Backend

Responsibilities:

* Admin authentication
* Admin dashboard
* Student management
* Attendance management
* Reports
* Notice management
* Timetable management
* SQLite database

 Member 2 — Student & UI

Responsibilities:

* Student authentication
* Student dashboard
* Attendance history
* Attendance analytics
* Low attendance alerts
* Notifications
* Student profile
* UI/UX implementation

 Shared Responsibilities

Both members collaborate on:

* Database integration
* Testing
* Debugging
* Documentation
* GitHub repository
* Final presentation

 -------------------------------------------------------------------

🧪 Testing

The application should be tested for:

 Authentication Testing

* Correct credentials
* Incorrect credentials
* Empty fields
* Wrong role access

 Student Testing

* Add student
* Edit student
* Delete student
* Search student

 Attendance Testing

* Mark Present
* Mark Absent
* Mark Leave
* Mark Late
* Update attendance
* Calculate percentage

 Alert Testing

* Attendance ≥75%
* Attendance between 60–74%
* Attendance below 60%

 Notification Testing

* Attendance update
* Low attendance
* New notice
* Timetable update

 UI Testing

* Different screen sizes
* Navigation
* Button functionality
* Form validation
* Orientation handling where applicable



# 🚀 Future Enhancements

The current project can be extended with:

* ☁️ Firebase Cloud Database
* 🔔 Firebase Cloud Messaging
* 📱 QR Code Attendance
* 🤳 Face Recognition Attendance
* 👨‍👩‍👦 Parent Login
* 👨‍🏫 Multiple Teacher Accounts
* 📧 Email Attendance Reports
* 📄 PDF Attendance Reports
* 📊 Excel Export
* 🔐 Biometric Authentication
* 🌐 Web-based Admin Dashboard
* ☁️ Cloud Backup
* 🤖 AI-based Attendance Prediction
* 📍 Location-based Attendance
* 🔒 Advanced role and permission management

 -------------------------------------------------------------------

# 📂 Suggested Project Structure


SmartStudentAttendanceTracker/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com.example.attendancetracker/
│           │       ├── activities/
│           │       ├── adapters/
│           │       ├── database/
│           │       ├── models/
│           │       ├── utils/
│           │       └── notifications/
│           │
│           ├── res/
│           │   ├── drawable/
│           │   ├── layout/
│           │   ├── menu/
│           │   ├── mipmap/
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
├── screenshots/
├── documentation/
├── README.md
└── .gitignore


📸 Screenshots

Add your final application screenshots to the repository.

Recommended screenshot categories:


screenshots/
│
├── login.png
├── admin-dashboard.png
├── student-management.png
├── mark-attendance.png
├── attendance-report.png
├── low-attendance.png
├── student-dashboard.png
├── attendance-history.png
├── analytics.png
├── notifications.png
├── notice-board.png
├── timetable.png
└── profile.png


🔑 Default Demo Accounts

For development/demo purposes, you can create sample accounts such as:


Admin
Username:  vishwrajsingh12@gmail.com
Password: 123456

Student
Student ID: STU001
Password: student123


**Important:** These are demonstration credentials only and should be changed or removed before production deployment.



 Planned Development Stages

* [x] Project planning
* [x] UI/UX design
* [x] Feature definition
* [ ] Database implementation
* [ ] Authentication
* [ ] Admin module
* [ ] Student module
* [ ] Attendance management
* [ ] Attendance analytics
* [ ] Low attendance system
* [ ] Notifications
* [ ] Testing
* [ ] Final documentation
* [ ] Final release


👨‍💻 Authors

**Smart Student Attendance Tracker**

Developed as a Mobile Application Development academic project by Purushottam Sah.

⭐ Project Highlights

"Smart Student Attendance Tracker" is more than a basic attendance application. It combines role-based authentication, student management, attendance recording, automated percentage calculation, low-attendance monitoring, recovery calculation, notifications, reports, analytics, notices, and timetable management into a single Android application.

The primary focus is to make attendance management **digital, transparent, automated, and easy to monitor, for both administrators and students.

