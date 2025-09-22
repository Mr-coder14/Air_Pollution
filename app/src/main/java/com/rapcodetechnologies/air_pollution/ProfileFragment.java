package com.rapcodetechnologies.air_pollution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import Classes.User;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private ImageView profilePic;
    private TextView profileEmail;
    private TextView profileNameDisplay; // Added for the header name
    private EditText profileName, profilePhone;
    private Button editProfileButton;
    private Button signOutButton; // Added for the sign-out button

    private DatabaseReference userDatabaseRef;
    private FirebaseAuth mAuth;
    private boolean isEditMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");

        profilePic = view.findViewById(R.id.profilePic);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileNameDisplay = view.findViewById(R.id.profileNameDisplay); // Initialized the new TextView
        profileName = view.findViewById(R.id.profileName);
        profilePhone = view.findViewById(R.id.profilePhone);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        signOutButton = view.findViewById(R.id.signOutButton); // Initialized the sign-out button

        fetchAndDisplayUserProfile();

        editProfileButton.setOnClickListener(v -> toggleEditMode());

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void fetchAndDisplayUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            profileEmail.setText(user.getEmail());

            userDatabaseRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User userProfile = dataSnapshot.getValue(User.class);
                        if (userProfile != null) {
                            // Set the text for both the EditText and the header TextView
                            profileName.setText(userProfile.getName());
                            profilePhone.setText(userProfile.getPhno());
                            profileNameDisplay.setText(userProfile.getName());
                        }
                    } else {
                        Toast.makeText(getContext(), "User profile not found in database.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error fetching user profile", databaseError.toException());
                    Toast.makeText(getContext(), "Failed to load profile details.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void toggleEditMode() {
        if (isEditMode) {
            saveProfileChanges();
        } else {
            profileName.setEnabled(true);
            profilePhone.setEnabled(true);
            editProfileButton.setText("Save Changes");
            isEditMode = true;
            Toast.makeText(getContext(), "Editing enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileChanges() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            Map<String, Object> updates = new HashMap<>();
            updates.put("name", profileName.getText().toString());
            updates.put("phno", profilePhone.getText().toString());

            userDatabaseRef.child(uid).updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        profileName.setEnabled(false);
                        profilePhone.setEnabled(false);
                        editProfileButton.setText("Edit Profile");
                        isEditMode = false;
                        // Update the header TextView immediately after a successful save
                        profileNameDisplay.setText(profileName.getText().toString());
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating profile", e);
                        Toast.makeText(getContext(), "Failed to save changes.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}