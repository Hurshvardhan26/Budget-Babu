package com.example.madapp;

import android.graphics.Color;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsFragment extends Fragment {
    private TextView textTotalBalance;
    private RecyclerView friendBalancesRecyclerView;
    private FriendBalanceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        
        textTotalBalance = view.findViewById(R.id.textTotalBalance);
        friendBalancesRecyclerView = view.findViewById(R.id.friendBalancesRecyclerView);
        friendBalancesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        updateBalances();
        return view;
    }

    private void updateBalances() {
        Map<String, Double> balances = new HashMap<>();
        double totalBalance = 0;

        // Get all groups
        List<Group> groups = ((MainActivity) requireActivity()).getGroups();
        
        // Calculate balances for each group
        for (Group group : groups) {
            for (GroupExpense expense : group.getExpenses()) {
                String paidBy = expense.getPaidBy();
                Map<String, Double> splits = expense.getSplits();
                
                // Add amount for person who paid
                if (!paidBy.equals("Mehul")) {
                    balances.put(paidBy, balances.getOrDefault(paidBy, 0.0) - expense.getAmount());
                    totalBalance -= expense.getAmount();
                }
                
                // Distribute splits
                for (Map.Entry<String, Double> split : splits.entrySet()) {
                    String person = split.getKey();
                    double amount = split.getValue();
                    
                    if (!person.equals("Mehul")) {
                        balances.put(person, balances.getOrDefault(person, 0.0) + amount);
                        totalBalance += amount;
                    }
                }
            }
        }

        // Update total balance display
        String totalBalanceText = String.format("%s %,.2f", "₹", Math.abs(totalBalance));
        if (totalBalance > 0) {
            textTotalBalance.setTextColor(Color.GREEN);
            textTotalBalance.setText("+" + totalBalanceText);
        } else if (totalBalance < 0) {
            textTotalBalance.setTextColor(Color.RED);
            textTotalBalance.setText("-" + totalBalanceText);
        } else {
            textTotalBalance.setTextColor(Color.BLACK);
            textTotalBalance.setText(totalBalanceText);
        }

        // Update RecyclerView
        List<FriendBalance> friendBalances = new ArrayList<>();
        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            friendBalances.add(new FriendBalance(entry.getKey(), entry.getValue()));
        }
        
        adapter = new FriendBalanceAdapter(friendBalances);
        friendBalancesRecyclerView.setAdapter(adapter);
    }

    private static class FriendBalance {
        String name;
        double balance;

        FriendBalance(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }
    }

    private class FriendBalanceAdapter extends RecyclerView.Adapter<FriendBalanceAdapter.ViewHolder> {
        private List<FriendBalance> friendBalances;

        FriendBalanceAdapter(List<FriendBalance> friendBalances) {
            this.friendBalances = friendBalances;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friend_balance, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FriendBalance friendBalance = friendBalances.get(position);
            holder.textFriendName.setText(friendBalance.name);
            
            double balance = friendBalance.balance;
            String balanceText = String.format("%s %,.2f", "₹", Math.abs(balance));
            
            if (balance > 0) {
                holder.textBalance.setTextColor(Color.GREEN);
                holder.textBalance.setText("+" + balanceText);
            } else if (balance < 0) {
                holder.textBalance.setTextColor(Color.RED);
                holder.textBalance.setText("-" + balanceText);
            } else {
                holder.textBalance.setTextColor(Color.BLACK);
                holder.textBalance.setText(balanceText);
            }
        }

        @Override
        public int getItemCount() {
            return friendBalances.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textFriendName;
            TextView textBalance;

            ViewHolder(View itemView) {
                super(itemView);
                textFriendName = itemView.findViewById(R.id.textFriendName);
                textBalance = itemView.findViewById(R.id.textBalance);
            }
        }
    }
} 