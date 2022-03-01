package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rashed.donofood.Models.FoodDonation;
import com.rashed.donofood.R;

public class FoodDonationViewActivity extends AppCompatActivity {

    ImageView donationPic;
    TextView donationName;
    TextView donationType;
    TextView donationQty;
    TextView donationArea;
    TextView donationPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation_view);

        //Get selected Donation object from previous activity
        FoodDonation donation = (FoodDonation) getIntent().getSerializableExtra("selectedFoodDonation");

        donationPic = findViewById(R.id.showDonationPic);
        donationName = findViewById(R.id.showDonationTitle);
        donationType = findViewById(R.id.showDonationType);
        donationQty = findViewById(R.id.showDonationQuantity);
        donationArea = findViewById(R.id.showDonationArea);
        donationPhone = findViewById(R.id.showDonationPhone);

        Toast.makeText(this, donation.getFoodName().toString(), Toast.LENGTH_SHORT).show();
    }

    void ShowDonation() {

    }
}