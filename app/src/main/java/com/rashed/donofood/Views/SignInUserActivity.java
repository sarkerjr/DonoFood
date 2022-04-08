package com.rashed.donofood.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rashed.donofood.R;

public class SignInUserActivity extends AppCompatActivity {

    EditText loginEmail;
    EditText loginPassword;
    Button registerBtn;
    Button loginBtn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_user);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        registerBtn = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            startActivity(new Intent(this, DashboardActivity.class));

        loginBtn.setOnClickListener(view -> userSignIn());
        registerBtn.setOnClickListener(view -> startActivity(new Intent(this, RegisterUserActivity.class)));
    }

    void userSignIn(){
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                        startActivity(new Intent(SignInUserActivity.this, DashboardActivity.class));
                    else
                        Toast.makeText(SignInUserActivity.this, "Incorrect email or password!",
                                Toast.LENGTH_SHORT).show();
                });
    }
}