package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.rashed.donofood.R;

public class DashboardActivity extends AppCompatActivity {

    CardView addDonationFoodView;
    CardView addDonationClothView;
    CardView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivity(new Intent(DashboardActivity.this, SignInActivity.class));

        addDonationFoodView = findViewById(R.id.donate_food_id);
        addDonationClothView = findViewById(R.id.donate_cloth_id);
        logout = findViewById(R.id.logout_id);

        addDonationFoodView.setOnClickListener(view -> startActivity(
                new Intent(DashboardActivity.this, FoodDonateActivity.class)));

        addDonationClothView.setOnClickListener(view ->
                startActivity(new Intent(DashboardActivity.this, ClothDonationActivity.class)));

        logout.setOnClickListener(view -> AuthUI.getInstance()
                .signOut(DashboardActivity.this)
                .addOnSuccessListener(unused -> startActivity(
                        new Intent(DashboardActivity.this, SignInActivity.class))));
    }
}