package com.example.smartstudentattendancetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteHelper";
    private static final String DATABASE_NAME = "AttendanceDB";
    private static final int DATABASE_VERSION = 12;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createOrUpdateTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createOrUpdateTables(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        createOrUpdateTables(db);
    }

    public void createOrUpdateTables(SQLiteDatabase db) {
        if (db == null || db.isReadOnly()) return;

        db.beginTransaction();
        try {
            // Ensure all tables exist
            db.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, role TEXT DEFAULT 'STUDENT', securityQuestion TEXT, securityAnswer TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS subjects(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, totalClass INTEGER DEFAULT 0, presentClass INTEGER DEFAULT 0, userEmail TEXT, className TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS attendance(id INTEGER PRIMARY KEY AUTOINCREMENT, subjectId INTEGER, date TEXT, status TEXT, studentEmail TEXT, className TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, date TEXT, subjectName TEXT, studentEmail TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS profiles(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT UNIQUE, college TEXT, course TEXT, semester TEXT, phone TEXT, dob TEXT, address TEXT, studentId TEXT, className TEXT, profileImage TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS timetable(id INTEGER PRIMARY KEY AUTOINCREMENT, day TEXT, subjectName TEXT, startTime TEXT, endTime TEXT, className TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS notices(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, message TEXT, date TEXT, postedBy TEXT, category TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS students(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, rollNumber TEXT, className TEXT, course TEXT, semester TEXT, phone TEXT, password TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS notifications(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, message TEXT, date TEXT, type TEXT, userEmail TEXT, isRead INTEGER DEFAULT 0)");

            // Safely add any missing columns for upgrades
            addColumnSafely(db, "users", "role", "TEXT DEFAULT 'STUDENT'");
            addColumnSafely(db, "users", "securityQuestion", "TEXT");
            addColumnSafely(db, "users", "securityAnswer", "TEXT");

            addColumnSafely(db, "subjects", "userEmail", "TEXT");
            addColumnSafely(db, "subjects", "className", "TEXT");

            addColumnSafely(db, "attendance", "studentEmail", "TEXT");
            addColumnSafely(db, "attendance", "className", "TEXT");

            addColumnSafely(db, "notes", "subjectName", "TEXT");
            addColumnSafely(db, "notes", "studentEmail", "TEXT");

            addColumnSafely(db, "profiles", "college", "TEXT");
            addColumnSafely(db, "profiles", "course", "TEXT");
            addColumnSafely(db, "profiles", "semester", "TEXT");
            addColumnSafely(db, "profiles", "phone", "TEXT");
            addColumnSafely(db, "profiles", "dob", "TEXT");
            addColumnSafely(db, "profiles", "address", "TEXT");
            addColumnSafely(db, "profiles", "studentId", "TEXT");
            addColumnSafely(db, "profiles", "className", "TEXT");
            addColumnSafely(db, "profiles", "profileImage", "TEXT");

            addColumnSafely(db, "timetable", "className", "TEXT");

            addColumnSafely(db, "notices", "postedBy", "TEXT");
            addColumnSafely(db, "notices", "category", "TEXT");

            addColumnSafely(db, "students", "course", "TEXT");
            addColumnSafely(db, "students", "semester", "TEXT");
            addColumnSafely(db, "students", "phone", "TEXT");
            addColumnSafely(db, "students", "password", "TEXT");

            addColumnSafely(db, "notifications", "isRead", "INTEGER DEFAULT 0");

            // Indexes
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_student_email ON attendance(studentEmail)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_student_class ON students(className)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_subject_user ON subjects(userEmail)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_att_date ON attendance(date)");

            // Ensure default admin user exists
            seedDefaultAdmin(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error in createOrUpdateTables", e);
        } finally {
            db.endTransaction();
        }
    }

    private void addColumnSafely(SQLiteDatabase db, String table, String column, String type) {
        try {
            db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type);
        } catch (Exception ignored) {
            // Column already exists or table issue
        }
    }

    private void seedDefaultAdmin(SQLiteDatabase db) {
        try {
            // Seed default admin username "admin" with password "admin123"
            ContentValues admin1 = new ContentValues();
            admin1.put("name", "Administrator");
            admin1.put("email", "admin");
            admin1.put("password", "admin123");
            admin1.put("role", "ADMIN");
            admin1.put("securityQuestion", "System Admin");
            admin1.put("securityAnswer", "Admin");
            db.insertWithOnConflict("users", null, admin1, SQLiteDatabase.CONFLICT_REPLACE);

            ContentValues profile1 = new ContentValues();
            profile1.put("email", "admin");
            profile1.put("college", "Institutional College");
            profile1.put("course", "Administration");
            profile1.put("semester", "N/A");
            profile1.put("phone", "0000000000");
            profile1.put("dob", "01-01-1990");
            profile1.put("address", "Main Campus");
            profile1.put("studentId", "ADM-001");
            profile1.put("className", "Admin");
            db.insertWithOnConflict("profiles", null, profile1, SQLiteDatabase.CONFLICT_REPLACE);

            // Also seed "admin@gmail.com" for compatibility
            ContentValues admin2 = new ContentValues();
            admin2.put("name", "Administrator");
            admin2.put("email", "admin@gmail.com");
            admin2.put("password", "admin123");
            admin2.put("role", "ADMIN");
            admin2.put("securityQuestion", "System Admin");
            admin2.put("securityAnswer", "Admin");
            db.insertWithOnConflict("users", null, admin2, SQLiteDatabase.CONFLICT_REPLACE);

            ContentValues profile2 = new ContentValues();
            profile2.put("email", "admin@gmail.com");
            profile2.put("college", "Institutional College");
            profile2.put("course", "Administration");
            profile2.put("semester", "N/A");
            profile2.put("phone", "0000000000");
            profile2.put("dob", "01-01-1990");
            profile2.put("address", "Main Campus");
            profile2.put("studentId", "ADM-001");
            profile2.put("className", "Admin");
            db.insertWithOnConflict("profiles", null, profile2, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            Log.e(TAG, "Error seeding default admin", e);
        }
    }

    // --- Authentication ---

    public boolean registerUser(String name, String email, String password, String question, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("securityQuestion", question);
        values.put("securityAnswer", answer);
        
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE lower(email)=lower(?)", new String[]{email});
            if (cursor != null && cursor.getCount() > 0) {
                return false; // Email already registered
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        // Check if this is the very first user in system
        Cursor countCursor = null;
        try {
            countCursor = db.rawQuery("SELECT * FROM users", null);
            values.put("role", (countCursor == null || countCursor.getCount() == 0) ? "ADMIN" : "STUDENT");
        } finally {
            if (countCursor != null) countCursor.close();
        }

        return db.insert("users", null, values) != -1;
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE lower(email)=lower(?) AND password=?", new String[]{email, password});
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT role FROM users WHERE lower(email)=lower(?)", new String[]{email});
            if (cursor != null && cursor.moveToFirst()) return cursor.getString(0);
        } finally {
            if (cursor != null) cursor.close();
        }
        return "STUDENT";
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT name FROM users WHERE lower(email)=lower(?)", new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(0);
                return name != null ? name : "";
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return "";
    }

    public String getSecurityQuestion(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT securityQuestion FROM users WHERE lower(email)=lower(?)", new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                String q = cursor.getString(0);
                return q != null ? q : "";
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return "";
    }

    public boolean verifySecurityAnswer(String email, String answer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE lower(email)=lower(?) AND securityAnswer=? COLLATE NOCASE", new String[]{email, answer});
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean checkUserExists(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id FROM users WHERE lower(email)=lower(?)", new String[]{email.trim()});
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean autoRegisterOrLoginStudent(String name, String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null) return false;
        String cleanEmail = email.trim();
        
        if (loginUser(cleanEmail, password)) {
            return true;
        }
        
        if (!checkUserExists(cleanEmail)) {
            String cleanName = (name != null && !name.trim().isEmpty()) ? name.trim() : (cleanEmail.contains("@") ? cleanEmail.split("@")[0] : cleanEmail);
            if (registerUser(cleanName, cleanEmail, password, "Security Question", "Answer")) {
                addStudent(cleanName, cleanEmail, "ROLL-" + (System.currentTimeMillis() % 10000), "General Section");
                updateProfile(cleanEmail, "Institutional College", "General", "Sem 1", "0000000000", "01-01-2000", "City, State", "STU-" + (System.currentTimeMillis() % 1000));
                return true;
            }
        }
        return false;
    }

    public boolean resetPassword(String email, String newPassword) {
        if (email == null || email.trim().isEmpty() || newPassword == null) return false;
        String cleanEmail = email.trim();
        SQLiteDatabase db = this.getWritableDatabase();
        
        if (checkUserExists(cleanEmail)) {
            ContentValues values = new ContentValues();
            values.put("password", newPassword);
            return db.update("users", values, "lower(email)=lower(?)", new String[]{cleanEmail}) > 0;
        } else {
            String name = cleanEmail.contains("@") ? cleanEmail.split("@")[0] : cleanEmail;
            if (registerUser(name, cleanEmail, newPassword, "Security Question", "Answer")) {
                String role = getUserRole(cleanEmail);
                if ("STUDENT".equals(role)) {
                    addStudent(name, cleanEmail, "ROLL-" + (System.currentTimeMillis() % 10000), "General Section");
                    updateProfile(cleanEmail, "Institutional College", "General", "Sem 1", "0000000000", "01-01-2000", "City, State", "STU-001");
                }
                return true;
            }
            return false;
        }
    }

    public boolean addSubject(String name, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("userEmail", userEmail);
        return db.insert("subjects", null, values) != -1;
    }

    public boolean addSubject(String name, String userEmail, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("userEmail", userEmail);
        values.put("className", className);
        return db.insert("subjects", null, values) != -1;
    }

    public Cursor getSubjects(String userEmail) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM subjects WHERE lower(userEmail)=lower(?)", new String[]{userEmail});
    }

    public void deleteSubject(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("subjects", "id=?", new String[]{String.valueOf(id)});
        db.delete("attendance", "subjectId=?", new String[]{String.valueOf(id)});
    }

    public boolean addAttendance(int subjectId, String date, String status, String studentEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subjectId", subjectId);
        values.put("date", date);
        values.put("status", status);
        values.put("studentEmail", studentEmail);
        long result = db.insert("attendance", null, values);
        if (result != -1 && subjectId > 0) updateSubjectCounts(subjectId);
        return result != -1;
    }

    public void deleteAttendance(int id, int subjectId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete("attendance", "id=?", new String[]{String.valueOf(id)}) > 0 && subjectId > 0) {
            updateSubjectCounts(subjectId);
        }
    }
    
    public void deleteAttendance(int id, int subjectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.delete("attendance", "id=?", new String[]{String.valueOf(id)}) > 0 && subjectId > 0) {
            updateSubjectCounts(subjectId);
        }
    }

    private void updateSubjectCounts(int subjectId) {
        if (subjectId <= 0) return;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = null;
        int total = 0, present = 0;
        try {
            c = db.rawQuery("SELECT status FROM attendance WHERE subjectId=?", new String[]{String.valueOf(subjectId)});
            if (c != null && c.moveToFirst()) {
                do {
                    String status = c.getString(0);
                    if ("Present".equals(status) || "Late".equals(status)) { total++; present++; }
                    else if ("Absent".equals(status)) { total++; }
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) c.close();
        }
        db.execSQL("UPDATE subjects SET totalClass = ?, presentClass = ? WHERE id = ?", new Object[]{total, present, subjectId});
    }

    public Cursor getAllAttendanceHistory(String email) {
        return this.getReadableDatabase().rawQuery(
            "SELECT attendance.id, attendance.subjectId, attendance.date, attendance.status, subjects.name, attendance.studentEmail, attendance.className " +
            "FROM attendance INNER JOIN subjects ON attendance.subjectId = subjects.id " +
            "WHERE lower(subjects.userEmail)=lower(?) ORDER BY attendance.id DESC", new String[]{email});
    }

    public Cursor getInstitutionalAttendanceHistory() {
        return this.getReadableDatabase().rawQuery(
            "SELECT attendance.id, attendance.subjectId, attendance.date, attendance.status, subjects.name, attendance.studentEmail, attendance.className " +
            "FROM attendance INNER JOIN subjects ON attendance.subjectId = subjects.id ORDER BY attendance.id DESC", null);
    }

    public Cursor getInstitutionalAttendanceHistoryBySubject(int subjectId) {
        return this.getReadableDatabase().rawQuery(
            "SELECT attendance.id, attendance.subjectId, attendance.date, attendance.status, subjects.name, attendance.studentEmail, attendance.className " +
            "FROM attendance INNER JOIN subjects ON attendance.subjectId = subjects.id WHERE attendance.subjectId=? ORDER BY attendance.id DESC", new String[]{String.valueOf(subjectId)});
    }

    public Cursor getAttendanceHistoryBySubject(int subjectId, String email) {
        return this.getReadableDatabase().rawQuery(
            "SELECT attendance.id, attendance.subjectId, attendance.date, attendance.status, subjects.name, attendance.studentEmail, attendance.className " +
            "FROM attendance INNER JOIN subjects ON attendance.subjectId = subjects.id WHERE attendance.subjectId=? AND lower(subjects.userEmail)=lower(?) ORDER BY attendance.id DESC", new String[]{String.valueOf(subjectId), email});
    }

    public boolean markAttendance(String studentEmail, String className, String subjectName, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        int sid = -1;
        Cursor sc = null;
        try {
            sc = db.rawQuery("SELECT id FROM subjects WHERE name=? AND lower(userEmail)=lower(?)", new String[]{subjectName, studentEmail});
            if (sc != null && sc.moveToFirst()) {
                sid = sc.getInt(0);
            } else {
                addSubject(subjectName, studentEmail, className);
                Cursor ns = null;
                try {
                    ns = db.rawQuery("SELECT id FROM subjects WHERE name=? AND lower(userEmail)=lower(?)", new String[]{subjectName, studentEmail});
                    if (ns != null && ns.moveToFirst()) sid = ns.getInt(0);
                } finally {
                    if (ns != null) ns.close();
                }
            }
        } finally {
            if (sc != null) sc.close();
        }
        
        ContentValues v = new ContentValues();
        v.put("subjectId", sid); v.put("date", date); v.put("status", status); v.put("studentEmail", studentEmail); v.put("className", className);
        long res = db.insert("attendance", null, v);
        if (res != -1 && sid > 0) updateSubjectCounts(sid);
        return res != -1;
    }

    public int getAllPresentCount(String email) {
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT id FROM attendance WHERE lower(studentEmail)=lower(?) AND (status='Present' OR status='Late')", new String[]{email});
            return c != null ? c.getCount() : 0;
        } finally {
            if (c != null) c.close();
        }
    }

    public int getAllLeaveCount(String email) {
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT id FROM attendance WHERE lower(studentEmail)=lower(?) AND status='Leave'", new String[]{email});
            return c != null ? c.getCount() : 0;
        } finally {
            if (c != null) c.close();
        }
    }

    public int getAllTotalAttendanceCount(String email) {
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT id FROM attendance WHERE lower(studentEmail)=lower(?) AND status IN ('Present', 'Absent', 'Late', 'Leave')", new String[]{email});
            return c != null ? c.getCount() : 0;
        } finally {
            if (c != null) c.close();
        }
    }

    public int getOverallPercentage(String email) {
        Cursor cTotal = null;
        try {
            cTotal = this.getReadableDatabase().rawQuery("SELECT id FROM attendance WHERE lower(studentEmail)=lower(?) AND status IN ('Present', 'Absent', 'Late')", new String[]{email});
            int t = cTotal != null ? cTotal.getCount() : 0;
            int p = getAllPresentCount(email);
            return t == 0 ? 0 : (p * 100) / t;
        } finally {
            if (cTotal != null) cTotal.close();
        }
    }

    public Cursor getMonthlyAttendance(String email) {
        return this.getReadableDatabase().rawQuery(
            "SELECT strftime('%m-%Y', date) as month, SUM(CASE WHEN status='Present' OR status='Late' THEN 1 ELSE 0 END) as present, COUNT(*) as total FROM attendance WHERE lower(studentEmail)=lower(?) GROUP BY month ORDER BY month ASC LIMIT 6", new String[]{email});
    }

    public boolean addNote(String title, String description, String date, String subjectName, String studentEmail) {
        ContentValues v = new ContentValues();
        v.put("title", title); v.put("description", description); v.put("date", date); v.put("subjectName", subjectName); v.put("studentEmail", studentEmail);
        return this.getWritableDatabase().insert("notes", null, v) != -1;
    }

    public Cursor getNotes(String studentEmail) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM notes WHERE lower(studentEmail)=lower(?) ORDER BY id DESC", new String[]{studentEmail});
    }

    public void deleteNote(int id) { this.getWritableDatabase().delete("notes", "id=?", new String[]{String.valueOf(id)}); }

    public boolean updateProfile(String email, String college, String course, String sem, String phone, String dob, String address, String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("college", college); v.put("course", course); v.put("semester", sem); v.put("phone", phone);
        v.put("dob", dob); v.put("address", address); v.put("studentId", studentId);
        int rows = db.update("profiles", v, "lower(email)=lower(?)", new String[]{email});
        if (rows == 0) {
            v.put("email", email);
            return db.insertWithOnConflict("profiles", null, v, SQLiteDatabase.CONFLICT_REPLACE) != -1;
        }
        return true;
    }

    public boolean updateUserName(String email, String name) {
        ContentValues v = new ContentValues(); v.put("name", name);
        return this.getWritableDatabase().update("users", v, "lower(email)=lower(?)", new String[]{email}) > 0;
    }

    public boolean updateProfileImage(String email, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("profileImage", imagePath);
        return db.update("profiles", v, "lower(email)=lower(?)", new String[]{email}) > 0;
    }

    public Cursor getProfile(String email) { return this.getReadableDatabase().rawQuery("SELECT * FROM profiles WHERE lower(email)=lower(?)", new String[]{email}); }

    public boolean addTimetable(String day, String subject, String start, String end, String className) {
        ContentValues v = new ContentValues(); v.put("day", day); v.put("subjectName", subject); v.put("startTime", start); v.put("endTime", end); v.put("className", className);
        return this.getWritableDatabase().insert("timetable", null, v) != -1;
    }

    public Cursor getTimetableByDay(String day, String className) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM timetable WHERE day=? AND className=? ORDER BY startTime ASC", new String[]{day, className});
    }

    public void deleteTimetable(int id) { this.getWritableDatabase().delete("timetable", "id=?", new String[]{String.valueOf(id)}); }

    public boolean addNotice(String title, String message, String date, String postedBy, String category) {
        ContentValues v = new ContentValues(); v.put("title", title); v.put("message", message); v.put("date", date); v.put("postedBy", postedBy); v.put("category", category);
        return this.getWritableDatabase().insert("notices", null, v) != -1;
    }

    public void addNotification(String title, String message, String type, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title); v.put("message", message); v.put("type", type); v.put("userEmail", userEmail);
        v.put("date", java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
        db.insert("notifications", null, v);
    }

    public Cursor getNotifications(String email) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM notifications WHERE lower(userEmail)=lower(?) ORDER BY id DESC", new String[]{email});
    }

    public Cursor getUnreadNotifications(String email) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM notifications WHERE lower(userEmail)=lower(?) AND isRead=0", new String[]{email});
    }

    public void markNotificationRead(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("isRead", 1);
        db.update("notifications", v, "id=?", new String[]{String.valueOf(id)});
    }

    public Cursor getNotices() { return this.getReadableDatabase().rawQuery("SELECT * FROM notices ORDER BY id DESC", null); }

    public List<String> getAllClasses() {
        List<String> l = new ArrayList<>(); 
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT DISTINCT className FROM students", null);
            if (c != null && c.moveToFirst()) do { 
                String cls = c.getString(0);
                if (cls != null && !cls.trim().isEmpty()) l.add(cls); 
            } while (c.moveToNext()); 
        } finally {
            if (c != null) c.close();
        }
        if (l.isEmpty()) l.add("General Section");
        return l;
    }

    public List<String> getAllSubjectNames() {
        List<String> l = new ArrayList<>(); 
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT DISTINCT name FROM subjects", null);
            if (c != null && c.moveToFirst()) do { 
                String name = c.getString(0);
                if (name != null && !name.trim().isEmpty()) l.add(name); 
            } while (c.moveToNext()); 
        } finally {
            if (c != null) c.close();
        }
        return l;
    }

    public Cursor getStudentsByClass(String className) { return this.getReadableDatabase().rawQuery("SELECT * FROM students WHERE className=?", new String[]{className}); }

    public boolean addStudent(String name, String email, String rollNumber, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("email", email);
        v.put("rollNumber", rollNumber);
        v.put("className", className);
        v.put("password", "student123");
        
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM users WHERE lower(email)=lower(?)", new String[]{email});
            if (c == null || c.getCount() == 0) {
                registerUser(name, email, "student123", "", "");
            }
        } finally {
            if (c != null) c.close();
        }
        
        long result = db.insertWithOnConflict("students", null, v, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public Cursor getAllStudents() { return this.getReadableDatabase().rawQuery("SELECT * FROM students ORDER BY name ASC", null); }

    public boolean updateStudent(int id, String name, String roll, String className) {
        ContentValues v = new ContentValues(); v.put("name", name); v.put("rollNumber", roll); v.put("className", className);
        return this.getWritableDatabase().update("students", v, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT email FROM students WHERE id=?", new String[]{String.valueOf(id)});
            if (c != null && c.moveToFirst()) { 
                String email = c.getString(0); 
                db.delete("users", "lower(email)=lower(?)", new String[]{email}); 
            }
        } finally {
            if (c != null) c.close();
        }
        db.delete("students", "id=?", new String[]{String.valueOf(id)});
    }

    public int getRegisteredStudentsCount() {
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT * FROM students", null);
            return c != null ? c.getCount() : 0;
        } finally {
            if (c != null) c.close();
        }
    }

    public int getPresentCount(int subjectId) {
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT presentClass FROM subjects WHERE id=?", new String[]{String.valueOf(subjectId)});
            if (c != null && c.moveToFirst()) return c.getInt(0);
        } finally {
            if (c != null) c.close();
        }
        return 0;
    }

    public int getTotalAttendance(int subjectId) {
        Cursor c = null;
        try {
            c = this.getReadableDatabase().rawQuery("SELECT totalClass FROM subjects WHERE id=?", new String[]{String.valueOf(subjectId)});
            if (c != null && c.moveToFirst()) return c.getInt(0);
        } finally {
            if (c != null) c.close();
        }
        return 0;
    }

    public int[] getInstitutionalStatsToday() {
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
        int[] stats = new int[3]; // [Present, Absent, Leave]
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c1 = null, c2 = null, c3 = null;
        try {
            c1 = db.rawQuery("SELECT DISTINCT a.studentEmail FROM attendance a INNER JOIN users u ON lower(a.studentEmail) = lower(u.email) WHERE a.date=? AND (a.status='Present' OR a.status='Late') AND u.role='STUDENT'", new String[]{today});
            stats[0] = c1 != null ? c1.getCount() : 0;
            
            c2 = db.rawQuery("SELECT DISTINCT a.studentEmail FROM attendance a INNER JOIN users u ON lower(a.studentEmail) = lower(u.email) WHERE a.date=? AND a.status='Absent' AND u.role='STUDENT'", new String[]{today});
            stats[1] = c2 != null ? c2.getCount() : 0;
            
            c3 = db.rawQuery("SELECT DISTINCT a.studentEmail FROM attendance a INNER JOIN users u ON lower(a.studentEmail) = lower(u.email) WHERE a.date=? AND a.status='Leave' AND u.role='STUDENT'", new String[]{today});
            stats[2] = c3 != null ? c3.getCount() : 0;
        } finally {
            if (c1 != null) c1.close();
            if (c2 != null) c2.close();
            if (c3 != null) c3.close();
        }
        return stats;
    }
}

