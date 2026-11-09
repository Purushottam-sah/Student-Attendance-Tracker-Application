package com.example.smartstudentattendancetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.database.SQLiteHelper;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private TextView textProfileName, textProfileSubtitle;
    private TextView textStudentID, textEmail, textPhone, textDOB, textAddress;
    private ImageView imgProfile;
    private MaterialButton btnUpdate;
    private com.google.android.material.floatingactionbutton.FloatingActionButton btnEditAvatar;
    private SQLiteHelper database;
    private SessionManager session;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    saveProfileImage(imageUri);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.activity_profile, container, false);

            database = new SQLiteHelper(requireContext());
            session = new SessionManager(requireContext());

            initViews(view);
            
            View btnBack = view.findViewById(R.id.btnBack);
            if (btnBack != null) btnBack.setVisibility(View.GONE);

            btnUpdate.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfileActivity.class)));
            
            btnEditAvatar.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            });

            loadUserData();
            return view;
        } catch (Exception e) {
            Log.e("ProfileFragment", "Inflation Error", e);
            return new View(requireContext());
        }
    }

    private void initViews(View view) {
        textProfileName = view.findViewById(R.id.textProfileName);
        textProfileSubtitle = view.findViewById(R.id.textProfileSubtitle);
        imgProfile = view.findViewById(R.id.imgProfile);
        btnUpdate = view.findViewById(R.id.btnUpdateProfile);
        btnEditAvatar = view.findViewById(R.id.btnEditAvatar);

        setupInfoRow(view, R.id.rowStudentID, android.R.drawable.ic_menu_info_details, "Student ID");
        setupInfoRow(view, R.id.rowEmail, android.R.drawable.ic_dialog_email, "Email");
        setupInfoRow(view, R.id.rowPhone, android.R.drawable.stat_sys_phone_call, "Phone");
        setupInfoRow(view, R.id.rowDOB, android.R.drawable.ic_menu_day, "Date of Birth");
        setupInfoRow(view, R.id.rowAddress, android.R.drawable.ic_menu_mapmode, "Address");

        textStudentID = view.findViewById(R.id.rowStudentID).findViewById(R.id.infoValue);
        textEmail = view.findViewById(R.id.rowEmail).findViewById(R.id.infoValue);
        textPhone = view.findViewById(R.id.rowPhone).findViewById(R.id.infoValue);
        textDOB = view.findViewById(R.id.rowDOB).findViewById(R.id.infoValue);
        textAddress = view.findViewById(R.id.rowAddress).findViewById(R.id.infoValue);
    }

    private void setupInfoRow(View parent, int rowId, int iconRes, String label) {
        View row = parent.findViewById(rowId);
        if (row != null) {
            ImageView icon = row.findViewById(R.id.infoIcon);
            TextView tvLabel = row.findViewById(R.id.infoLabel);
            if (icon != null) icon.setImageResource(iconRes);
            if (tvLabel != null) tvLabel.setText(label);
        }
    }

    private void saveProfileImage(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            File file = new File(requireContext().getFilesDir(), "profile_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.close();
            inputStream.close();

            String path = file.getAbsolutePath();
            if (database.updateProfileImage(session.getUserEmail(), path)) {
                imgProfile.setImageURI(Uri.fromFile(file));
                Toast.makeText(requireContext(), "Profile Photo Updated", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Image Save Error", e);
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserData() {
        if (!isAdded()) return;
        
        String email = session.getUserEmail();
        String name = database.getUserName(email);

        textProfileName.setText(name.isEmpty() ? "Student Name" : name);
        textEmail.setText(email);

        Cursor profileCursor = null;
        try {
            profileCursor = database.getProfile(email);
            if (profileCursor != null && profileCursor.moveToFirst()) {
                String course = profileCursor.getString(3);
                String semester = profileCursor.getString(4);
                if (course == null) course = "";
                if (semester == null) semester = "";
                textProfileSubtitle.setText((semester.isEmpty() && course.isEmpty()) ? "Not Assigned" : (semester + " | " + course));
                
                String phone = profileCursor.getString(5);
                String dob = profileCursor.getString(6);
                String address = profileCursor.getString(7);
                String studentId = profileCursor.getString(8);

                textPhone.setText(phone != null && !phone.isEmpty() ? phone : "N/A");
                textDOB.setText(dob != null && !dob.isEmpty() ? dob : "N/A");
                textAddress.setText(address != null && !address.isEmpty() ? address : "N/A");
                textStudentID.setText(studentId != null && !studentId.isEmpty() ? studentId : "TBD");

                String imagePath = profileCursor.getString(10); // index 10 for profileImage
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imgFile = new File(imagePath);
                    if (imgFile.exists()) {
                        imgProfile.setImageURI(Uri.fromFile(imgFile));
                    }
                }
            } else {
                textProfileSubtitle.setText("Not Assigned");
                textPhone.setText("N/A");
                textDOB.setText("N/A");
                textAddress.setText("N/A");
                textStudentID.setText("TBD");
            }
        } finally {
            if (profileCursor != null) profileCursor.close();
        }
    }
}