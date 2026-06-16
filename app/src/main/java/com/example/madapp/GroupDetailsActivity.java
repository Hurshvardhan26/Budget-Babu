package com.example.madapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailsActivity extends AppCompatActivity {
    private static final String TAG = "GroupDetailsActivity";
    public static final String EXTRA_GROUP_NAME = "group_name";
    private TextView textGroupName;
    private RecyclerView membersRecyclerView;
    private RecyclerView expensesRecyclerView;
    private ExtendedFloatingActionButton fab;
    private Group currentGroup;
    private ExpensesAdapter expensesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Set white color for back button
            toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.white));
        }

        try {
            if (getIntent().hasExtra("group")) {
                currentGroup = getIntent().getParcelableExtra("group");
            }
            
            if (currentGroup == null) {
                Log.e(TAG, "No group data received");
                Toast.makeText(this, "Error loading group", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            Log.d(TAG, "Group loaded: " + currentGroup.getName() + " with " + currentGroup.getFriends().size() + " members");
            
            setupViews();
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error loading group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupViews() {
        textGroupName = findViewById(R.id.textGroupDetailName);
        textGroupName.setText(currentGroup.getName());

        // Setup members RecyclerView
        membersRecyclerView = findViewById(R.id.membersRecyclerView);
        membersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        MembersAdapter membersAdapter = new MembersAdapter(currentGroup.getFriends());
        membersRecyclerView.setAdapter(membersAdapter);

        // Setup expenses RecyclerView
        expensesRecyclerView = findViewById(R.id.expensesRecyclerView);
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        expensesAdapter = new ExpensesAdapter(currentGroup.getExpenses());
        expensesRecyclerView.setAdapter(expensesAdapter);

        fab = findViewById(R.id.fabAddExpense);
        fab.setOnClickListener(v -> showAddExpenseDialog());
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_MadApp_Dialog);
        builder.setTitle("Add Group Expense");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_group_expense, null);
        builder.setView(view);

        EditText amountInput = view.findViewById(R.id.editTextAmount);
        Spinner currencySpinner = view.findViewById(R.id.spinnerCurrency);
        Spinner categorySpinner = view.findViewById(R.id.spinnerCategory);
        EditText notesInput = view.findViewById(R.id.editTextNotes);
        Spinner paidBySpinner = view.findViewById(R.id.spinnerPaidBy);
        Spinner splitTypeSpinner = view.findViewById(R.id.spinnerSplitType);

        setupSpinners(currencySpinner, categorySpinner, paidBySpinner, splitTypeSpinner);

        builder.setPositiveButton("Add", (dialog, which) -> {
            try {
                String amount = amountInput.getText().toString();
                if (amount.isEmpty()) {
                    Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                GroupExpense expense = new GroupExpense(
                    Double.parseDouble(amount),
                    currencySpinner.getSelectedItem().toString(),
                    categorySpinner.getSelectedItem().toString(),
                    notesInput.getText().toString(),
                    paidBySpinner.getSelectedItem().toString()
                );

                if (splitTypeSpinner.getSelectedItem().toString().equals("Equal Split")) {
                    expense.splitEqually(currentGroup);
                    currentGroup.addExpense(expense);
                    expensesAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
                } else {
                    showCustomSplitDialog(expense);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error adding expense", e);
                Toast.makeText(this, "Error adding expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_background);
        dialog.show();
    }

    private void setupSpinners(Spinner currencySpinner, Spinner categorySpinner, 
                             Spinner paidBySpinner, Spinner splitTypeSpinner) {
        // Currency spinner
        ArrayAdapter<CharSequence> currencyAdapter = new ArrayAdapter<CharSequence>(this,
            android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.currencies)) {
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
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);

        // Category spinner
        ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<CharSequence>(this,
            android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.expense_categories)) {
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
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Paid by spinner
        ArrayAdapter<String> paidByAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, new ArrayList<>(currentGroup.getFriends())) {
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
        paidByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paidBySpinner.setAdapter(paidByAdapter);

        // Split type spinner
        String[] splitTypes = {"Equal Split", "Custom Split"};
        ArrayAdapter<String> splitTypeAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, splitTypes) {
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
        splitTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        splitTypeSpinner.setAdapter(splitTypeAdapter);
    }

    private void showCustomSplitDialog(GroupExpense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_MadApp_Dialog);
        builder.setTitle("Custom Split");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_custom_split, null);
        LinearLayout splitContainer = view.findViewById(R.id.splitContainer);

        // Show payer's share first
        View payerRow = LayoutInflater.from(this).inflate(
            R.layout.item_split_input, splitContainer, false);
        TextView payerName = payerRow.findViewById(R.id.textFriendName);
        EditText payerAmount = payerRow.findViewById(R.id.editTextSplitAmount);
        payerName.setText(expense.getPaidBy() + " (Paid)");
        splitContainer.addView(payerRow);

        // Add input fields for each friend
        Map<String, EditText> splitInputs = new HashMap<>();
        splitInputs.put(expense.getPaidBy(), payerAmount); // Add payer to splits map
        
        for (String friend : currentGroup.getFriends()) {
            if (!friend.equals(expense.getPaidBy())) {
                View splitRow = LayoutInflater.from(this).inflate(
                    R.layout.item_split_input, splitContainer, false);
                
                TextView friendName = splitRow.findViewById(R.id.textFriendName);
                EditText amountInput = splitRow.findViewById(R.id.editTextSplitAmount);
                
                friendName.setText(friend);
                splitInputs.put(friend, amountInput);
                
                splitContainer.addView(splitRow);
            }
        }

        builder.setView(view);
        
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            Map<String, Double> customSplits = new HashMap<>();
            double total = 0;
            
            // Calculate total of all splits including payer
            for (Map.Entry<String, EditText> entry : splitInputs.entrySet()) {
                String amount = entry.getValue().getText().toString();
                if (!amount.isEmpty()) {
                    double splitAmount = Double.parseDouble(amount);
                    customSplits.put(entry.getKey(), splitAmount);
                    total += splitAmount;
                }
            }
            
            // Validate total matches expense amount
            if (Math.abs(total - expense.getAmount()) > 0.01) {
                Toast.makeText(this, "Split amounts must equal the total expense amount: " + 
                    String.format("%,.2f", expense.getAmount()), Toast.LENGTH_SHORT).show();
                return;
            }
            
            expense.splitCustom(customSplits);
            currentGroup.addExpense(expense);
            expensesAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Expense added with custom split", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_background);
        dialog.show();
    }

    private class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
        private List<String> members;
        private int[] memberColors;

        MembersAdapter(List<String> members) {
            this.members = members;
            // Get the colors array from resources
            TypedArray colors = getResources().obtainTypedArray(R.array.member_circle_colors);
            memberColors = new int[colors.length()];
            for (int i = 0; i < colors.length(); i++) {
                memberColors[i] = colors.getColor(i, 0);
            }
            colors.recycle();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_member_circle, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String member = members.get(position);
            holder.textView.setText(String.valueOf(member.charAt(0)).toUpperCase());
            holder.textView.setContentDescription(member);
            
            // Set background color based on position
            GradientDrawable background = (GradientDrawable) holder.textView.getBackground();
            background.setColor(memberColors[position % memberColors.length]);
        }

        @Override
        public int getItemCount() {
            return members.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textMemberInitial);
            }
        }
    }

    private class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {
        private List<GroupExpense> expenses;
        private java.text.SimpleDateFormat dateFormat;

        ExpensesAdapter(List<GroupExpense> expenses) {
            this.expenses = expenses;
            this.dateFormat = new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group_expense, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GroupExpense expense = expenses.get(position);
            holder.textAmount.setText(String.format("%s %,.2f", expense.getCurrency(), expense.getAmount()));
            holder.textCategory.setText(expense.getCategory());
            holder.textPaidBy.setText("Paid by: " + expense.getPaidBy());
            holder.textDateTime.setText(dateFormat.format(expense.getDate()));
        }

        @Override
        public int getItemCount() {
            return expenses.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textAmount;
            TextView textCategory;
            TextView textPaidBy;
            TextView textDateTime;

            ViewHolder(View itemView) {
                super(itemView);
                textAmount = itemView.findViewById(R.id.textAmount);
                textCategory = itemView.findViewById(R.id.textCategory);
                textPaidBy = itemView.findViewById(R.id.textPaidBy);
                textDateTime = itemView.findViewById(R.id.textDateTime);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Update the group in MainActivity before finishing
        Intent resultIntent = new Intent();
        resultIntent.putExtra("group", currentGroup);
        setResult(RESULT_OK, resultIntent);

        // Navigate to groups tab in MainActivity
        if (getParent() instanceof MainActivity) {
            ((MainActivity) getParent()).navigateToGroups();
        }

        finish();
        return true;
    }
} 