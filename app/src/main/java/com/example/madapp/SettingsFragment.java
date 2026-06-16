package com.example.madapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private SharedPreferences preferences;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        // Initialize shared preferences
        preferences = requireActivity().getSharedPreferences("MadAppPreferences", Context.MODE_PRIVATE);
        
        // Setup profile section
        setupProfileSection(view);
        
        // Setup preferences section
        setupPreferencesSection(view);
        
        // Setup account section
        setupAccountSection(view);
        
        return view;
    }
    
    private void setupProfileSection(View view) {
        TextView editProfile = view.findViewById(R.id.textEditProfile);
        TextView changePassword = view.findViewById(R.id.textChangePassword);
        
        editProfile.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Edit Profile functionality will be implemented in future updates", Toast.LENGTH_SHORT).show();
        });
        
        changePassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Change Password functionality will be implemented in future updates", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupPreferencesSection(View view) {
        TextView defaultCurrency = view.findViewById(R.id.textDefaultCurrency);
        TextView categories = view.findViewById(R.id.textCategories);
        TextView notifications = view.findViewById(R.id.textNotifications);
        TextView themeSelector = view.findViewById(R.id.textThemeSelector);
        
        // Set current preference values
        String currentCurrency = preferences.getString("default_currency", "$");
        boolean notificationsEnabled = preferences.getBoolean("notifications_enabled", true);
        boolean darkModeEnabled = preferences.getBoolean("dark_mode_enabled", true);
        
        defaultCurrency.setText("Default Currency: " + currentCurrency);
        notifications.setText("Email Notifications: " + (notificationsEnabled ? "Enabled" : "Disabled"));
        themeSelector.setText("Dark Mode: " + (darkModeEnabled ? "Enabled" : "Disabled"));
        
        defaultCurrency.setOnClickListener(v -> {
            // Cycle through currencies: $, €, £, ₹
            String newCurrency;
            switch (currentCurrency) {
                case "$":
                    newCurrency = "€";
                    break;
                case "€":
                    newCurrency = "£";
                    break;
                case "£":
                    newCurrency = "₹";
                    break;
                default:
                    newCurrency = "$";
                    break;
            }
            
            // Save preference
            preferences.edit().putString("default_currency", newCurrency).apply();
            
            // Update UI
            defaultCurrency.setText("Default Currency: " + newCurrency);
            Toast.makeText(requireContext(), "Default currency set to " + newCurrency, Toast.LENGTH_SHORT).show();
        });
        
        categories.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Categories management will be implemented in future updates", Toast.LENGTH_SHORT).show();
        });
        
        notifications.setOnClickListener(v -> {
            // Toggle notifications
            boolean newState = !notificationsEnabled;
            
            // Save preference
            preferences.edit().putBoolean("notifications_enabled", newState).apply();
            
            // Update UI
            notifications.setText("Email Notifications: " + (newState ? "Enabled" : "Disabled"));
            Toast.makeText(requireContext(), "Notifications " + (newState ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });
        
        themeSelector.setOnClickListener(v -> {
            // Toggle dark mode
            boolean newState = !darkModeEnabled;
            
            // Save preference
            preferences.edit().putBoolean("dark_mode_enabled", newState).apply();
            
            // Update UI
            themeSelector.setText("Dark Mode: " + (newState ? "Enabled" : "Disabled"));
            
            // Apply theme change
            if (newState) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            
            Toast.makeText(requireContext(), "Dark Mode " + (newState ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupAccountSection(View view) {
        TextView exportData = view.findViewById(R.id.textExportData);
        TextView privacyPolicy = view.findViewById(R.id.textPrivacyPolicy);
        TextView termsOfService = view.findViewById(R.id.textTermsOfService);
        TextView signOut = view.findViewById(R.id.textSignOut);
        
        exportData.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Data export functionality will be implemented in future updates", Toast.LENGTH_SHORT).show();
        });
        
        privacyPolicy.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Privacy Policy will be implemented in future updates", Toast.LENGTH_SHORT).show();
        });
        
        termsOfService.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Terms of Service will be implemented in future updates", Toast.LENGTH_SHORT).show();
        });
        
        signOut.setOnClickListener(v -> {
            // Clear preferences (simulating sign out)
            preferences.edit().clear().apply();
            
            Toast.makeText(requireContext(), "You have been signed out", Toast.LENGTH_SHORT).show();
            
            // In a real app, you would navigate to the login screen
            // For now, we'll just show a message
            Toast.makeText(requireContext(), "Note: Sign out functionality will fully implemented with user authentication in future updates", Toast.LENGTH_LONG).show();
        });
    }
} 