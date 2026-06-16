package com.example.madapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private final List<Expense> expenses;

    public ExpenseAdapter(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView amountText;
        private final TextView paidByText;
        private final TextView dateText;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.expenseTitle);
            amountText = itemView.findViewById(R.id.expenseAmount);
            paidByText = itemView.findViewById(R.id.expensePaidBy);
            dateText = itemView.findViewById(R.id.expenseDate);
        }

        void bind(Expense expense) {
            titleText.setText(expense.getCategory());
            amountText.setText(expense.getFormattedAmount());
            paidByText.setText(expense.getNotes());
            dateText.setText(String.format("%s at %s", 
                expense.getFormattedDate(), 
                expense.getFormattedTime()));
        }
    }
} 