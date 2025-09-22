package com.rapcodetechnologies.air_pollution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private TextView registerbtn, forget;
    private Button loginbtn;
    private FirebaseAuth auth;
    private EditText email, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        // The following code is commented out to ensure the app always opens at the login screen.
        // For a final app, this check is good practice to keep users logged in.
        // FirebaseUser userid = auth.getCurrentUser();
        // if (userid != null) {
        //     startActivity(new Intent(LoginActivity.this, MainActivity.class));
        //     finish();
        // }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerbtn = findViewById(R.id.registerpage);
        loginbtn = findViewById(R.id.loginbtn);
        email = findViewById(R.id.emaillogin);
        forget = findViewById(R.id.forgettxt);
        password = findViewById(R.id.passwordlogin);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetpassword();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void forgetpassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dailog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailBox.getText().toString();
                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            // Added log for debugging failed password reset attempts
                            Log.e("LoginActivity", "Password reset failed: " + task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }


    private void login() {
        String email1 = email.getText().toString();
        String pass1 = password.getText().toString();

        if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1)) {
            Toast.makeText(LoginActivity.this, "Enter All details", Toast.LENGTH_SHORT).show();
        } else if (pass1.length() < 6) {
            Toast.makeText(LoginActivity.this, "Incorrect Password ", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            auth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Added log for debugging failed login attempts
                        Log.e("LoginActivity", "Login failed: " + task.getException().getMessage());
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}