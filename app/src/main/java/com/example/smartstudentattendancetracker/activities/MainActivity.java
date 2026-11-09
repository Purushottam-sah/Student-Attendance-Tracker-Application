package com.example.smartstudentattendancetracker.activities;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartstudentattendancetracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Handle navigation extra
        String fragmentName = getIntent().getStringExtra("fragment");
        if ("profile".equals(fragmentName)) {
            loadFragment(new ProfileFragment(), false); // Initial load, don't add to backstack
            bottomNavigation.setSelectedItemId(R.id.settings); 
        } else {
            loadFragment(new HomeFragment(), false); // Initial load, don't add to backstack
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment = null;

            if (id == R.id.home) fragment = new HomeFragment();
            else if (id == R.id.subject) fragment = new SubjectFragment();
            else if (id == R.id.notes) fragment = new NotesFragment();
            else if (id == R.id.settings) fragment = new SettingsFragment();

            if (fragment != null) {
                loadFragment(fragment, true); // User action, add to backstack
                return true;
            }
            return false;
        });

        // Sync bottom nav on back stack pop
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
            if (current instanceof HomeFragment) bottomNavigation.getMenu().findItem(R.id.home).setChecked(true);
            else if (current instanceof SubjectFragment) bottomNavigation.getMenu().findItem(R.id.subject).setChecked(true);
            else if (current instanceof NotesFragment) bottomNavigation.getMenu().findItem(R.id.notes).setChecked(true);
            else if (current instanceof SettingsFragment) bottomNavigation.getMenu().findItem(R.id.settings).setChecked(true);
            else if (current instanceof ProfileFragment) bottomNavigation.getMenu().findItem(R.id.settings).setChecked(true);
        });

        // Modern Back Button Handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else {
                    finish();
                }
            }
        });
    }

    // Default version for HomeFragment clicks or simple calls
    public void loadFragment(Fragment fragment) {
        loadFragment(fragment, true);
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.container, fragment);
        
        if (addToBackStack && !(fragment instanceof HomeFragment)) {
            ft.addToBackStack(null);
        } else if (fragment instanceof HomeFragment) {
            // If going home, clear all backstack entries to make Home the root
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        
        ft.commit();
    }
}