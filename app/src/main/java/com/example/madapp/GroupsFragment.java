package com.example.madapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

public class GroupsFragment extends Fragment {
    private RecyclerView recyclerView;
    private GroupAdapter adapter;
    private List<Group> groups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        
        recyclerView = view.findViewById(R.id.groupsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        groups = new ArrayList<>();
        adapter = new GroupAdapter(groups);
        recyclerView.setAdapter(adapter);

        // Load existing groups from MainActivity
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            updateGroups(activity.getGroups());
        }

        return view;
    }

    public void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create New Group");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_group, null);
        builder.setView(dialogView);

        EditText groupNameInput = dialogView.findViewById(R.id.editTextGroupName);
        LinearLayout friendsContainer = dialogView.findViewById(R.id.friendsContainer);
        Button addFriendButton = dialogView.findViewById(R.id.buttonAddFriend);

        // Add a TextView to show the default user
        TextView defaultUserText = new TextView(requireContext());
        defaultUserText.setText("Members: " + Group.DEFAULT_USER + " (You)");
        defaultUserText.setPadding(0, 16, 0, 16);
        friendsContainer.addView(defaultUserText, 0);

        addFriendButton.setOnClickListener(v -> {
            EditText friendInput = new EditText(requireContext());
            friendInput.setHint("Friend's Name");
            friendInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            friendsContainer.addView(friendInput);
        });

        builder.setPositiveButton("Create", (dialog, which) -> {
            String groupName = groupNameInput.getText().toString().trim();
            if (!groupName.isEmpty()) {
                Group newGroup = new Group(groupName);
                
                // Add friends (starting from index 1 to skip the default user TextView)
                for (int i = 1; i < friendsContainer.getChildCount(); i++) {
                    View child = friendsContainer.getChildAt(i);
                    if (child instanceof EditText) {
                        String friendName = ((EditText) child).getText().toString().trim();
                        if (!friendName.isEmpty()) {
                            newGroup.addFriend(friendName);
                        }
                    }
                }

                // Add to MainActivity's groups list
                if (getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.addGroup(newGroup);
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
        private List<Group> groups;
        private static final int GROUP_DETAILS_REQUEST_CODE = 1;

        public GroupAdapter(List<Group> groups) {
            this.groups = groups;
        }

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group, parent, false);
            return new GroupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            Group group = groups.get(position);
            holder.textGroupName.setText(group.getName());
            holder.textMemberCount.setText(group.getFriends().size() + " friends");
            
            holder.itemView.setOnClickListener(v -> {
                try {
                    if (group != null) {
                        Intent intent = new Intent(requireContext(), GroupDetailsActivity.class);
                        intent.putExtra("group", group);
                        startActivityForResult(intent, GROUP_DETAILS_REQUEST_CODE);
                    } else {
                        Toast.makeText(requireContext(), "Error: Invalid group", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error opening group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        class GroupViewHolder extends RecyclerView.ViewHolder {
            TextView textGroupName;
            TextView textMemberCount;

            public GroupViewHolder(@NonNull View itemView) {
                super(itemView);
                textGroupName = itemView.findViewById(R.id.textGroupName);
                textMemberCount = itemView.findViewById(R.id.textMemberCount);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GroupAdapter.GROUP_DETAILS_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Group updatedGroup = data.getParcelableExtra("group");
            if (updatedGroup != null) {
                // Find and update the group in MainActivity's list
                if (getActivity() instanceof MainActivity) {
                    MainActivity activity = (MainActivity) getActivity();
                    List<Group> groups = activity.getGroups();
                    for (int i = 0; i < groups.size(); i++) {
                        if (groups.get(i).getId().equals(updatedGroup.getId())) {
                            groups.set(i, updatedGroup);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void updateGroups(List<Group> newGroups) {
        this.groups.clear();
        this.groups.addAll(newGroups);
        adapter.notifyDataSetChanged();
    }
} 