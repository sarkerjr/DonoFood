package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.storage.FirebaseStorage;
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
//    Button editDonationBtn;


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

        //Get selected Donation object from previous activity
        FoodDonation donation = (FoodDonation) getIntent().getSerializableExtra("selectedFoodDonation");

        showDonation(donation);

        //Passing image name to ViewImageActivity to show full image
        donationPic.setOnClickListener(view -> {
            Intent intent = new Intent(this, ViewImageActivity.class);
            intent.putExtra("imageFileName", donation.getImageFileName());
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FoodDonationViewActivity.this, FoodSearchActivity.class));
    }

    void showDonation(FoodDonation donation) {
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("images/" + donation.getImageFileName().trim());
        storage.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString())
                    .placeholder(R.drawable.progress_animation).into(donationPic);
            donationName.setText(donation.getFoodName());
            donationType.setText(donation.getFoodType());
            donationQty.setText(String.valueOf(donation.getQuantity()));
            donationArea.setText(donation.getLocation());
            donationPhone.setText(donation.getPhone());
        });
    }
}