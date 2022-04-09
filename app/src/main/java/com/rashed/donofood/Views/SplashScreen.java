package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rashed.donofood.Models.User;
import com.rashed.donofood.R;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000; //in millisecond

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the SignInUserActivity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                    checkUserRole();
                else {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent intent = new Intent(SplashScreen.this, SignInUserActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
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
                            startActivity(new Intent(this, DashboardActivity.class));
                        else
                            startActivity(new Intent(this, NgoDashboardActivity.class));
                    }
                });
    }
}