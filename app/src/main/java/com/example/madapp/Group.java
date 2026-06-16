package com.example.madapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group implements Parcelable {
    private String id;
    private String name;
    private List<String> friends;
    private List<GroupExpense> expenses;
    public static final String DEFAULT_USER = "Mehul";

    public Group(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.friends = new ArrayList<>();
        this.friends.add(DEFAULT_USER); // Add default user
        this.expenses = new ArrayList<>();
    }

    protected Group(Parcel in) {
        id = in.readString();
        name = in.readString();
        friends = new ArrayList<>();
        in.readStringList(friends);
        expenses = new ArrayList<>();
        // Read expenses one by one
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            expenses.add((GroupExpense) in.readParcelable(GroupExpense.class.getClassLoader()));
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeStringList(friends);
        // Write expenses one by one
        dest.writeInt(expenses.size());
        for (GroupExpense expense : expenses) {
            dest.writeParcelable(expense, flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<String> getFriends() { return friends; }
    public void setFriends(List<String> friends) { this.friends = friends; }
    public void addFriend(String friendName) { 
        if (!friendName.equals(DEFAULT_USER) && !friends.contains(friendName)) {
            this.friends.add(friendName);
        }
    }
    public List<GroupExpense> getExpenses() { return expenses; }
    public void setExpenses(List<GroupExpense> expenses) { this.expenses = expenses; }
    public void addExpense(GroupExpense expense) { this.expenses.add(expense); }
} 