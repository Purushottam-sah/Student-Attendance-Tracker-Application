package com.example.smartstudentattendancetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartstudentattendancetracker.R;
import com.example.smartstudentattendancetracker.session.SessionManager;
import com.example.smartstudentattendancetracker.utils.ThemeManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);

        session = new SessionManager(requireContext());

        // Hide back button in fragment mode
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setVisibility(View.GONE);

        // Dark Mode
        SwitchMaterial darkMode = view.findViewById(R.id.darkSwitch);
        if (darkMode != null) {
            darkMode.setChecked(ThemeManager.isDark(requireContext()));
            darkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ThemeManager.setDark(requireContext(), isChecked);
                if (getActivity() != null) getActivity().recreate();
            });
        }

        // Setup Rows matching Screen 11
        setupRow(view, R.id.btnProfileSettings, "My Profile", android.R.drawable.ic_menu_myplaces, v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity)getActivity()).loadFragment(new ProfileFragment());
            }
        });

        setupRow(view, R.id.btnChangePassword, "Change Password", android.R.drawable.ic_lock_idle_lock, v -> {
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
        });

        setupRow(view, R.id.btnNotiSettings, "Notification Settings", android.R.drawable.ic_popup_reminder, v -> {
            startActivity(new Intent(getActivity(), NotificationListActivity.class));
        });

        setupRow(view, R.id.btnPrivacy, "Privacy Policy", android.R.drawable.ic_menu_info_details, v -> {
            startActivity(new Intent(getActivity(), PrivacyActivity.class));
        });

        setupRow(view, R.id.btnTerms, "Terms & Conditions", android.R.drawable.ic_menu_agenda, v -> {
            startActivity(new Intent(getActivity(), TermsActivity.class));
        });

        setupRow(view, R.id.btnAbout, "About App", R.drawable.ic_logo, v -> {
            startActivity(new Intent(getActivity(), AboutActivity.class));
        });

        // Logout
        View logoutBtn = view.findViewById(R.id.logoutBtn);
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> {
                session.logout();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                if (getActivity() != null) getActivity().finish();
            });
        }

        return view;
    }

    private void setupRow(View root, int id, String text, int iconRes, View.OnClickListener listener) {
        View row = root.findViewById(id);
        if (row != null) {
            TextView tv = row.findViewById(R.id.rowText);
            ImageView iv = row.findViewById(R.id.rowIcon);
            if (tv != null) tv.setText(text);
            if (iv != null) iv.setImageResource(iconRes);
            row.setOnClickListener(listener);
        }
    }
}