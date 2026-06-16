package com.example.madapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenses = new ArrayList<>();
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            Log.d(TAG, "Creating HomeFragment view");
            return inflater.inflate(R.layout.fragment_home, container, false);
        } catch (Exception e) {
            Log.e(TAG, "Error creating HomeFragment view", e);
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            Log.d(TAG, "Setting up HomeFragment views");
            super.onViewCreated(view, savedInstanceState);

            // Initialize views
            RecyclerView recyclerView = view.findViewById(R.id.expensesRecyclerView);
            emptyView = view.findViewById(R.id.emptyView);

            // Setup RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            expenseAdapter = new ExpenseAdapter(expenses);
            recyclerView.setAdapter(expenseAdapter);

            // Load any existing expenses from MainActivity
            if (getActivity() instanceof MainActivity) {
                Log.d(TAG, "Loading existing expenses");
                MainActivity activity = (MainActivity) getActivity();
                updateExpenses(activity.getExpenses());
            }

            Log.d(TAG, "HomeFragment setup completed");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up HomeFragment", e);
        }
    }

    public void updateExpenses(List<Expense> newExpenses) {
        try {
            Log.d(TAG, "Updating expenses list");
            expenses.clear();
            expenses.addAll(newExpenses);
            if (expenseAdapter != null) {
                expenseAdapter.notifyDataSetChanged();
            }
            updateEmptyView();
        } catch (Exception e) {
            Log.e(TAG, "Error updating expenses", e);
        }
    }

    private void updateEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(expenses.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }
} 