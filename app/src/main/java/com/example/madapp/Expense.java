package com.example.madapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Expense {
    private final double amount;
    private final String currency;
    private final String category;
    private final String notes;
    private final Date timestamp;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public Expense(double amount, String currency, String category, String notes) {
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.notes = notes;
        this.timestamp = new Date(); // Current date and time
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    public String getFormattedAmount() {
        return currency + " " + String.format("%.2f", amount);
    }

    public String getFormattedDate() {
        return dateFormat.format(timestamp);
    }

    public String getFormattedTime() {
        return timeFormat.format(timestamp);
    }
} 