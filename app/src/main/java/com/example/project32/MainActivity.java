package com.example.project32;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;
import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.cardview.widget.CardView;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout usernameLayout, emailLayout, passwordLayout, confirmPasswordLayout, phoneLayout;
    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etPhone;
    private Button btnLogin, btnSignup;
    private Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]{4,10}$");
    private Pattern emailPattern = Pattern.compile( "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private Pattern phonenumberPattern = Pattern.compile("^[0-9]{11}$");
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private Spinner spinner;
    private String role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.pbar);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        usernameLayout = findViewById(R.id.et_username);
        emailLayout = findViewById(R.id.et_email);
        passwordLayout = findViewById(R.id.et_password);
        confirmPasswordLayout = findViewById(R.id.et_con_password);
        phoneLayout = findViewById(R.id.et_number);

        etUsername = usernameLayout.getEditText();
        etEmail = emailLayout.getEditText();
        etPhone = phoneLayout.getEditText();
        etPassword = passwordLayout.getEditText();
        etConfirmPassword = confirmPasswordLayout.getEditText();
        spinner = findViewById(R.id.spinner);

        btnLogin = findViewById(R.id.btn_login);
        btnSignup = findViewById(R.id.btn_signup);

        String[] items = new String[]{"Select Role", "User", "Owner"};
        spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                role = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String phone = etPhone.getText().toString();
                role = spinner.getSelectedItem().toString();
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(username)) {
                    usernameLayout.setError("Username is required");
                    usernameLayout.requestFocus();
                } else if (!usernamePattern.matcher(username).matches()) {
                    usernameLayout.setError("Username contains letters and numbers only");
                    usernameLayout.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    emailLayout.setError("Email is required");
                    emailLayout.requestFocus();
                } else if (!emailPattern.matcher(email).matches()) {
                    emailLayout.setError("wrong email!");
                    emailLayout.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passwordLayout.setError("Password is required");
                    passwordLayout.setError(null);
                } else if (password.length() < 6) {
                    passwordLayout.setError("Password must be at least 6 characters");
                    passwordLayout.requestFocus();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    confirmPasswordLayout.setError("Confirm Password is required");
                    confirmPasswordLayout.requestFocus();
                } else if (!password.equals(confirmPassword)) {
                    confirmPasswordLayout.setError("Passwords do not match");
                    confirmPasswordLayout.requestFocus();
                } else if (TextUtils.isEmpty(phone)) {
                    phoneLayout.setError("Phone number is required");
                    phoneLayout.requestFocus();
                } else if (!phonenumberPattern.matcher(phone).matches()) {
                    phoneLayout.setError("incorrect number");
                    phoneLayout.requestFocus();
                } else if (Objects.equals(role, "Select Role")){
                    Toast.makeText(getApplicationContext(), "Please Select Role", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                FirebaseUser user = auth.getCurrentUser();
                                sendEmailVerification(user);  // Send email verification

                                assert user != null;
                                DocumentReference df = firestore.collection("Users").document(user.getUid());
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("email", email);
                                userInfo.put("name", username);
                                userInfo.put("phone", phone);
                                userInfo.put("role", role);
                                userInfo.put("uid", user.getUid());
                                df.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user profile is created for"+ user);

                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Successfully Registered!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            } else{
                                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(getApplicationContext(), "User Already Exist!!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
    private void sendEmailVerification(FirebaseUser user) {
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error sending verification email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}