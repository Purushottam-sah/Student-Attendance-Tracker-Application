# 🎓 Smart Student Attendance Tracker

A modern, high-fidelity Android application designed for educational institutions to seamlessly track, manage, and analyze student attendance. Built with an offline-first approach using SQLite, the app provides distinct experiences for both Administrators and Students with a premium **Material Design 3** interface.

## ✨ Key Features

### 👨‍💼 Admin Portal
- **Batch Attendance:** Mark daily attendance (Present, Absent, Leave) with a streamlined interface.
- **Institutional Analytics:** View overall college performance with interactive Bar and Pie charts.
- **Student Management:** Add, edit, and manage student profiles and track their individual subject-wise progress.
- **Custom Notices:** Broadcast updates and timetables directly to student dashboards.

### 👨‍🎓 Student Portal
- **Real-time Dashboard:** Track personal attendance percentages and classes needed to reach target goals (e.g., 75%).
- **Subject-wise Progress:** Monitor individual subject performance and track Leave statuses without penalizing grades.
- **Instant Alerts:** Receive local device notifications immediately when attendance is marked by an admin.
- **Profile Customization:** Update personal details and upload a custom profile avatar.

## 🏗️ System Architecture

The application follows a layered Android application architecture with an offline-first database approach.

### Architecture Diagram

```text
             SMART STUDENT ATTENDANCE TRACKER
                         |
              +----------+----------+
              |                     |
        ADMIN PORTAL           STUDENT PORTAL
              |                     |
     +--------+--------+    +-------+--------+
     |        |        |    |       |        |
 Attendance Student  Notice Dashboard Profile
 Management Management Management Analytics
     |        |        |    |       |        |
     +--------+--------+----+-------+--------+
                         |
                PRESENTATION LAYER
                (Android XML UI)
                         |
                BUSINESS LOGIC LAYER
                 (Java Application)
                         |
                  DATA ACCESS LAYER
                  (Database Helper)
                         |
                  DATABASE LAYER
                     (SQLite)
                         |
              +----------+----------+
              |                     |
        Local Storage          Notifications
        (Profile Images)       (User Alerts)
```

### Architecture Components

1. **Presentation Layer:** Provides the Android user interface using XML layouts and Material Design 3 components.
2. **Business Logic Layer:** Handles attendance operations, student management, attendance calculations and application functionality using Java.
3. **Data Access Layer:** Manages communication between application logic and the SQLite database.
4. **Database Layer:** Stores student information, attendance records, subjects and other application data locally using SQLite.
5. **Supporting Components:** Handles profile image storage, local notifications and attendance analytics.

### Application Workflow

1. Admin logs into the application.
2. Admin manages student information and marks daily attendance.
3. Attendance records are stored in the SQLite database.
4. Students access their dashboards to view attendance percentages and subject-wise progress.
5. The application displays analytics and generates local notifications when attendance is updated.

## 🎨 UI/UX Highlights

- **Royal Indigo & Arctic Theme:** A sophisticated custom color palette giving an enterprise-grade feel.
- **Floating Navigation:** Modern floating bottom navigation bar with active pill indicators.
- **Dynamic Greetings:** UI adapts greetings based on the time of day.
- **Premium Geometry:** Consistent 28dp rounded corners and custom geometric app icons.

## 🛠️ Tech Stack

| Technology | Description |
|---|---|
| Programming Language | Java |
| Platform | Android SDK |
| Database | SQLite |
| UI/Design | XML, Material Design 3 |
| Charting | [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) |
| IDE | Android Studio |
| Image Storage | Internal Device Storage |
| Notifications | Android Local Notifications |

## 💻 System Requirements

Before installing the application, make sure your system has:

- Windows, macOS or Linux.
- Android Studio installed.
- Android SDK and required SDK platform tools.
- JDK compatible with the project's Gradle version.
- Internet connection for the initial dependency synchronization.
- Android emulator or physical Android device.

## 🚀 Installation and Setup

Follow these steps to download, configure and run the project.

### Step 1: Install Required Software

1. Download and install [Android Studio](https://developer.android.com/studio).
2. Install the Android SDK through Android Studio SDK Manager.
3. Install Git from [Git Official Website](https://git-scm.com/downloads).
4. Make sure the required JDK is configured in Android Studio.

### Step 2: Clone the Repository

Open Git Bash or your terminal and execute:

```bash
git clone https://github.com/Purushottam-sah/Student-Attendance-Tracker-Application.git
```

### Step 3: Navigate to the Project Directory

```bash
cd Student-Attendance-Tracker-Application
```

### Step 4: Open the Project in Android Studio

- Launch Android Studio.
- Select **Open**.
- Navigate to the cloned project folder.
- Select the project and open it.
- Wait for Gradle synchronization to complete.

### Step 5: Configure Android SDK

- Open Android Studio SDK Manager.
- Install the Android SDK platform required by the project.
- Ensure the required build tools are installed.
- Check that Gradle synchronization completes successfully.

### Step 6: Run the Application

**Option 1: Using Android Studio**

1. Start an Android emulator or connect your Android phone using USB debugging.
2. Select your device from the device list.
3. Click the **Run ▶** button.
4. Wait for the application to build and launch.

**Option 2: Using Terminal**

For Windows (PowerShell or Git Bash):

```bash
./gradlew.bat assembleDebug
```

To install the debug application on a connected Android device:

```bash
./gradlew.bat installDebug
```

For macOS or Linux:

```bash
chmod +x gradlew
./gradlew assembleDebug
./gradlew installDebug
```

*Note: Gradle commands must be executed from the project root directory. The project must include the Gradle wrapper files.*

## 📦 Build APK

To generate a debug APK, execute:

**Windows:**
```bash
./gradlew.bat assembleDebug
```

**macOS / Linux:**
```bash
./gradlew assembleDebug
```

The generated APK is generally available at:

```text
app/build/outputs/apk/debug/app-debug.apk
```



## 📁 Project Repository

**GitHub:** [Student Attendance Tracker Application](https://github.com/Purushottam-sah/Student-Attendance-Tracker-Application)

---

**Designed & Developed with ❤️ for better education management.**
