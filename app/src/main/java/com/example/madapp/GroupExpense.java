package com.example.madapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupExpense implements Parcelable {
    private String id;
    private double amount;
    private String currency;
    private String category;
    private String notes;
    private Date date;
    private String paidBy;  // Friend who paid
    private Map<String, Double> splits;  // Friend name -> Amount to pay

    public GroupExpense(double amount, String currency, String category, String notes, String paidBy) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.notes = notes;
        this.date = new Date();
        this.paidBy = paidBy;
        this.splits = new HashMap<>();
    }

    protected GroupExpense(Parcel in) {
        id = in.readString();
        amount = in.readDouble();
        currency = in.readString();
        category = in.readString();
        paidBy = in.readString();
        notes = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        splits = new HashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            Double value = in.readDouble();
            splits.put(key, value);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(amount);
        dest.writeString(currency);
        dest.writeString(category);
        dest.writeString(paidBy);
        dest.writeString(notes);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeInt(splits.size());
        for (Map.Entry<String, Double> entry : splits.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeDouble(entry.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupExpense> CREATOR = new Creator<GroupExpense>() {
        @Override
        public GroupExpense createFromParcel(Parcel in) {
            return new GroupExpense(in);
        }

        @Override
        public GroupExpense[] newArray(int size) {
            return new GroupExpense[size];
        }
    };

    // Split types
    public void splitEqually(Group group) {
        double equalShare = amount / (group.getFriends().size());
        for (String friend : group.getFriends()) {
            if (!friend.equals(paidBy)) {
                splits.put(friend, equalShare);
            }
        }
    }

    public void splitCustom(Map<String, Double> customSplits) {
        this.splits = new HashMap<>(customSplits);
    }

    // Getters and setters
    public String getId() { return id; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCategory() { return category; }
    public String getNotes() { return notes; }
    public Date getDate() { return date; }
    public String getPaidBy() { return paidBy; }
    public Map<String, Double> getSplits() { return splits; }
    public void setSplits(Map<String, Double> splits) { this.splits = splits; }
} 