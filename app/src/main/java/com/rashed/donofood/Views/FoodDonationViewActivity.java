package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.rashed.donofood.Models.FoodDonation;
import com.rashed.donofood.R;
import com.squareup.picasso.Picasso;

public class FoodDonationViewActivity extends AppCompatActivity {

    ImageView donationPic;
    TextView donationName;
    TextView donationType;
    TextView donationQty;
    TextView donationArea;
    TextView donationPhone;
    CardView donationContainerView;
    ProgressBar donationLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation_view);

        donationPic = findViewById(R.id.showDonationPic);
        donationName = findViewById(R.id.showDonationTitle);
        donationType = findViewById(R.id.showDonationType);
        donationQty = findViewById(R.id.showDonationQuantity);
        donationArea = findViewById(R.id.showDonationArea);
        donationPhone = findViewById(R.id.showDonationPhone);
        donationLoadingIndicator = findViewById(R.id.donationLoadingIndicator);

        //Get selected Donation object from previous activity
        FoodDonation donation = (FoodDonation) getIntent().getSerializableExtra("selectedFoodDonation");

        showDonation(donation);
    }

    void showDonation(FoodDonation donation) {
        donationLoadingIndicator.setVisibility(View.VISIBLE);
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("images/" + donation.getImageFileName().trim());
        storage.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString())
                    .placeholder(R.drawable.progress_animation).into(donationPic);
            donationLoadingIndicator.setVisibility(View.GONE);
            donationName.setText(donation.getFoodName());
            donationType.setText(donation.getFoodType());
            donationQty.setText(String.valueOf(donation.getQuantity()));
            donationArea.setText(donation.getLocation());
            donationPhone.setText(donation.getPhone());
        });
    }
}