package com.example.testfirebase.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testfirebase.MainActivity;
import com.example.testfirebase.R;
import com.example.testfirebase.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class InputNameActivity extends AppCompatActivity {

    private EditText inputName;
    private Button btnToDate;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_name);

        inputName = findViewById(R.id.inputName);
        btnToDate = findViewById(R.id.btnContinue);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToTinder();
            }
        });
    }
    private void continueToTinder() {
        final String name = inputName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                // Save user's name to Firestore
                DocumentReference userRef = firestore.collection("users").document(currentUser.getUid());
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // User document already exists, update the name
                                userRef.update("name", name)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Name successfully updated in Firestore, proceed to Tinder or main activity
                                                    // For example, navigate to Tinder activity:
                                                    Intent intent = new Intent(InputNameActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(InputNameActivity.this, "Failed to update name. Please try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // User document doesn't exist, create a new document with the name
                                Users user = new Users(); // Replace "User" with your custom user class if you have one
                                userRef.set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Name successfully saved to Firestore, proceed to Tinder or main activity
                                                    // For example, navigate to Tinder activity:
                                                    Intent intent = new Intent(InputNameActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(InputNameActivity.this, "Failed to save name. Please try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(InputNameActivity.this, "Failed to fetch user data. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // If there is no signed-in user, navigate to the sign-in or registration screen first.
                // For example, navigate to sign-in activity:
                Intent intent = new Intent(InputNameActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

}