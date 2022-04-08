package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.rashed.donofood.R;


public class RegisterUserActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private TextView warningMessage;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regConfirmPassword = findViewById(R.id.regPasswordConfirm);
        warningMessage = findViewById(R.id.warningMessage);
        Button signupButton = findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();
        signupButton.setOnClickListener(view -> registerUser());
    }

    void registerUser() {
        String email = regEmail.getText().toString().trim();
        String password = regPassword.getText().toString().trim();
        String confirmPassword = regConfirmPassword.getText().toString().trim();

        if(password.equals(confirmPassword)) {
            warningMessage.setVisibility(View.GONE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterUserActivity.this, "Registration Successful!",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterUserActivity.this, SignInUserActivity.class));
                        } else {
                            warningMessage.setVisibility(View.VISIBLE);
                            warningMessage.setText("Something went wrong! Please try again!");
                        }
                    });
        } else{
            warningMessage.setVisibility(View.VISIBLE);
            warningMessage.setText("Password do not match!");
        }
    }
}