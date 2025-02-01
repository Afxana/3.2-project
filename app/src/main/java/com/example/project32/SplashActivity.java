package com.example.project32;



//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//
//public class SplashActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run(){
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },3000);
//
//    }
//
//}

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Delay splash screen for 3 seconds
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                // User is logged in, get role from Firestore
                String userId = currentUser.getUid();
                firestore.collection("Users").document(userId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String role = document.getString("role");
                                    if (role != null && role.equals("Owner")) {
                                        // Navigate to Owner Home Activity
                                        Intent intent = new Intent(SplashActivity.this, OwnerHomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Navigate to User Home Activity
                                        Intent intent = new Intent(SplashActivity.this, UserHomeActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(SplashActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
                                    navigateToLogin();
                                }
                            } else {
                                Toast.makeText(SplashActivity.this, "Failed to load user data: " +
                                                (task.getException() != null ? task.getException().getMessage() : ""),
                                        Toast.LENGTH_SHORT).show();
                                navigateToLogin();
                            }
                            finish();
                        });
            } else {
                // User is not logged in, navigate to LoginActivity
                navigateToLogin();
            }
        }, 1000);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}