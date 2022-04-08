package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.rashed.donofood.R;

public class NgoDashboardActivity extends AppCompatActivity {

    CardView searchOptionView;
    CardView contactUsView;
    CardView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_dashboard);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivity(new Intent(this, SignInUserActivity.class));

        searchOptionView = findViewById(R.id.search_option_id);
        contactUsView = findViewById(R.id.contact_us_id);
        logout = findViewById(R.id.logout_id);

        searchOptionView.setOnClickListener(view -> {
            openSearchOptionDialog();
        });

        logout.setOnClickListener(view -> AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(unused -> startActivity(
                        new Intent(this, SignInUserActivity.class))));
    }

    public void openSearchOptionDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.donation_seach_option_title);
        alertDialogBuilder.setPositiveButton("Cloth",
                (arg0, arg1) -> startActivity(new Intent(NgoDashboardActivity.this, ClothSearchActivity.class)));
        alertDialogBuilder.setNegativeButton("Food",
                (arg0, arg1) -> startActivity(new Intent(NgoDashboardActivity.this, FoodSearchActivity.class)));
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}