package com.example.madapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private HomeFragment homeFragment;
    private GroupsFragment groupsFragment;
    private FriendsFragment friendsFragment;
    private SettingsFragment settingsFragment;
    private ExtendedFloatingActionButton fab;
    private final String[] currencies = {"₹", "$", "€", "£"};
    private final String[] categories = {"Travel", "Food", "Leisure", "Miscellaneous"};
    private List<Expense> expenses = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "Starting onCreate");
            super.onCreate(savedInstanceState);
            Log.d(TAG, "Setting content view");
            setContentView(R.layout.activity_main);

            // Initialize fragments
            Log.d(TAG, "Initializing fragments");
            homeFragment = new HomeFragment();
            groupsFragment = new GroupsFragment();
            friendsFragment = new FriendsFragment();
            settingsFragment = new SettingsFragment();
            
            Log.d(TAG, "Loading home fragment");
            loadFragment(homeFragment); // Load home fragment by default

            // Setup bottom navigation
            Log.d(TAG, "Setting up bottom navigation");
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);

            // Setup FAB
            Log.d(TAG, "Setting up FAB");
            fab = findViewById(R.id.fabAddExpense);
            fab.setOnClickListener(v -> showAddExpenseDialog());
            
            Log.d(TAG, "onCreate completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error initializing app: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    try {
                        Log.d(TAG, "Navigation item selected: " + item.getItemId());
                        Fragment selectedFragment = null;
                        int itemId = item.getItemId();

                        if (itemId == R.id.navigation_home) {
                            selectedFragment = homeFragment;
                            fab.setVisibility(View.VISIBLE);
                            fab.setOnClickListener(v -> showAddExpenseDialog());
                            fab.setText("Add Expense");
                        } else if (itemId == R.id.navigation_groups) {
                            selectedFragment = groupsFragment;
                            fab.setVisibility(View.VISIBLE);
                            fab.setOnClickListener(v -> {
                                if (groupsFragment != null) {
                                    groupsFragment.showCreateGroupDialog();
                                }
                            });
                            fab.setText("Create Group");
                        } else if (itemId == R.id.navigation_friends) {
                            selectedFragment = friendsFragment;
                            fab.setVisibility(View.GONE);
                        } else if (itemId == R.id.navigation_settings) {
                            selectedFragment = settingsFragment;
                            fab.setVisibility(View.GONE);
                        }

                        if (selectedFragment != null) {
                            loadFragment(selectedFragment);
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                        Log.e(TAG, "Error in navigation", e);
                        return false;
                    }
                }
            };

    private void loadFragment(Fragment fragment) {
        try {
            Log.d(TAG, "Loading fragment: " + fragment.getClass().getSimpleName());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } catch (Exception e) {
            Log.e(TAG, "Error loading fragment", e);
        }
    }

    private void showAddExpenseDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_individual_expense, null);
        dialog.setContentView(view);

        // Initialize views
        EditText amountEditText = view.findViewById(R.id.editTextAmount);
        Spinner currencySpinner = view.findViewById(R.id.spinnerCurrency);
        Spinner categorySpinner = view.findViewById(R.id.spinnerCategory);
        EditText notesEditText = view.findViewById(R.id.editTextNotes);
        Button submitButton = view.findViewById(R.id.buttonSubmit);

        // Create custom adapters with white text
        ArrayAdapter<String> whiteTextCurrencyAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, currencies) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.on_surface));
                return view;
            }
            
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.on_surface));
                return view;
            }
        };
        whiteTextCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(whiteTextCurrencyAdapter);
        
        ArrayAdapter<String> whiteTextCategoryAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, categories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.on_surface));
                return view;
            }
            
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.on_surface));
                return view;
            }
        };
        whiteTextCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(whiteTextCategoryAdapter);

        // Setup submit button
        submitButton.setOnClickListener(v -> {
            if (validateInput(amountEditText)) {
                saveExpense(
                    amountEditText.getText().toString(),
                    currencySpinner.getSelectedItem().toString(),
                    categorySpinner.getSelectedItem().toString(),
                    notesEditText.getText().toString()
                );
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean validateInput(EditText amountEditText) {
        String amountStr = amountEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveExpense(String amountStr, String currency, String category, String notes) {
        double amount = Double.parseDouble(amountStr);
        Expense expense = new Expense(amount, currency, category, notes.trim());
        expenses.add(0, expense); // Add to the main list
        homeFragment.updateExpenses(expenses); // Update the fragment's view
        Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show();
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void navigateToGroups() {
        loadFragment(groupsFragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_groups);
    }

    public void addGroup(Group group) {
        groups.add(group);
        if (groupsFragment != null) {
            groupsFragment.updateGroups(groups);
            navigateToGroups();
        }
    }
}