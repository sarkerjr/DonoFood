package com.rashed.donofood.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rashed.donofood.Models.User;
import com.rashed.donofood.R;

import java.util.Objects;

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
        if(mAuth.getCurrentUser() != null) checkUserRole();

        loginBtn.setOnClickListener(view -> userSignIn());
        registerBtn.setOnClickListener(view -> startActivity(new Intent(this, RegisterUserActivity.class)));
    }

    void userSignIn(){
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                        checkUserRole();
                    else
                        Toast.makeText(SignInUserActivity.this, "Incorrect email or password!",
                                Toast.LENGTH_SHORT).show();
                });
    }

    void checkUserRole() {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        //If can not retrieve USER ROLE from database, restart activity
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }else {
                        User user = Objects.requireNonNull(task.getResult()).getValue(User.class);
                        assert user != null;

                        if(user.getRole().equals("USER"))
                            startActivity(new Intent(SignInUserActivity.this, DashboardActivity.class));
                        else
                            startActivity(new Intent(SignInUserActivity.this, NgoDashboardActivity.class));
                    }
                });
    }
}