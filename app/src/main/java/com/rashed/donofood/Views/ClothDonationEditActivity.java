package com.rashed.donofood.Views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rashed.donofood.Models.ClothDonation;
import com.rashed.donofood.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ClothDonationEditActivity extends AppCompatActivity {

    ImageView donate_image_id;
    Button edit_donation_btn;
    Spinner food_type_id;
    EditText food_name_id;
    EditText food_quantity_id;
    EditText area_id;
    EditText phone_id;
    Spinner loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation_edit);

        donate_image_id = findViewById(R.id.upload_food_image_id);
        food_name_id = findViewById(R.id.input_food_name);
        food_type_id = findViewById(R.id.food_type_spinner);
        food_quantity_id = findViewById(R.id.input_food_quantity_id);
        area_id = findViewById(R.id.input_area_id);
        phone_id = findViewById(R.id.input_phone_id);
        edit_donation_btn = findViewById(R.id.submit_food_donation_btn);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        //Get selected Donation object from previous activity
        ClothDonation donation = (ClothDonation) getIntent().getSerializableExtra("passedDonation");

        //Initializing food type spinner
        food_type_id.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Summer", "Winter"}));

        food_name_id.setText(donation.getClothName());
        food_type_id.setSelection(getSpinnerPosition(donation.getClothType()));
        food_quantity_id.setText(String.valueOf(donation.getQuantity()));
        area_id.setText(donation.getLocation());
        phone_id.setText(donation.getPhone());

        //Retrieving image from firebase storage
        StorageReference storage = FirebaseStorage.getInstance().getReference()
                .child("images/" + donation.getImageFileName().trim());
        storage.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString())
                    .placeholder(R.drawable.progress_animation).into(donate_image_id);
        });

        edit_donation_btn.setOnClickListener(view -> {
            String clothName = String.valueOf(food_name_id.getText());
            String clothType = food_type_id.getSelectedItem().toString();
            int quantity = Integer.parseInt(String.valueOf(food_quantity_id.getText()));
            String area = String.valueOf(area_id.getText());
            String phone = String.valueOf(phone_id.getText());

            ClothDonation newDonation =
                    new ClothDonation(donation.getUid(), clothName, clothType, quantity, area, phone,
                            donation.getImageFileName());

            //Check if fields are updated
            if (!clothName.equals(donation.getClothName()) || !clothType.equals(donation.getClothType())
                    || quantity != donation.getQuantity() || !area.equals(donation.getLocation())
                    || !phone.equals(donation.getPhone())) {
                editDonation(donation, newDonation);
            }
        });
    }

    //getting the position of the selected foodType value
    int getSpinnerPosition(String location) {
        switch (location){
            case "Summer":
                return 0;
            case "Winter":
                return 1;
        }
        return 0;
    }

    void editDonation(ClothDonation oldDonation, ClothDonation newDonation) {
        loadingIndicator.setVisibility(View.VISIBLE);

        Query databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Donations").child("Cloth").orderByChild("imageFileName")
                .equalTo(oldDonation.getImageFileName());

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String nodeKey = snapshot.getKey();
                assert nodeKey != null;
                FirebaseDatabase.getInstance().getReference().child("Donations").child("Cloth")
                        .child(nodeKey).setValue(newDonation).addOnSuccessListener(view -> {
                    loadingIndicator.setVisibility(View.GONE);
                    Toast.makeText(ClothDonationEditActivity.this, "Edit Successfully!", Toast.LENGTH_LONG).show();
                    //Passing the newly created donation to search activity
                    Intent intent = new Intent(ClothDonationEditActivity.this, EditEntryActivity.class);
                    startActivity(intent);
                }).addOnFailureListener(view -> {
                    loadingIndicator.setVisibility(View.GONE);
                    Toast.makeText(ClothDonationEditActivity.this,
                            "Something went wrong! Please try again!",Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onChildAdded: " + view.getMessage());
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addChildEventListener(childEventListener);
    }
}