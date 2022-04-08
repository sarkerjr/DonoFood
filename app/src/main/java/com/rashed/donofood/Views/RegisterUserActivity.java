package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.rashed.donofood.Models.User;
import com.rashed.donofood.R;


public class RegisterUserActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private TextView warningMessage;
    private Spinner userRoleSelectSpinner;

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

        userRoleSelectSpinner = findViewById(R.id.userRoleSelect);
        userRoleSelectSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"USER", "NGO"}));

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
                            setUserRole();
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

    void setUserRole() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user.getUid())
                .setValue(new User(userRoleSelectSpinner.getSelectedItem().toString().trim()));
    }
}