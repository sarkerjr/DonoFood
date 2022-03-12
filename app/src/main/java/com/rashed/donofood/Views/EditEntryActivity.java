package com.rashed.donofood.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rashed.donofood.Models.ClothDonation;
import com.rashed.donofood.Models.FoodDonation;
import com.rashed.donofood.R;
import com.rashed.donofood.Utils.ClothDonationAdapter;
import com.rashed.donofood.Utils.FoodDonationAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class EditEntryActivity extends AppCompatActivity {

    Spinner donationType;
    Button donationSearch;
    ListView donationList;
    TextView donationNotFound;

    FoodDonationAdapter foodDonationAdapter;
    ClothDonationAdapter clothDonationAdapter;

    Query databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        donationType = findViewById(R.id.donation_type_search_spinner);
        donationSearch = findViewById(R.id.donation_type_search_button);
        donationList = findViewById(R.id.donation_list_view);
        donationNotFound = findViewById(R.id.donation_not_found_id);

        donationType.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Food", "Cloth"}));

        //By default initializing food donations first
        initializeFoodDonationList();

        //Adding search according to selected donation type
        donationSearch.setOnClickListener(view -> {
            //Check if  the input is "Food" or "Cloth"
            if(donationType.getSelectedItem().toString().equals("Food")){
                initializeFoodDonationList();
            }else {
                initializeClothDonationList();
            }
        });
    }

    void initializeFoodDonationList() {
        //This arrayList is for listView onItemClickListener
        foodDonationAdapter = new FoodDonationAdapter(EditEntryActivity.this, new ArrayList<>());
        donationList.setAdapter(foodDonationAdapter);
        foodDonationAdapter.notifyDataSetChanged();

        //Doing query according to logged in user's "UID"
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Donations").child("Food").orderByChild("uid")
                .equalTo(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        databaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Add products to adapter to show on MainActivity
                foodDonationAdapter.add(snapshot.getValue(FoodDonation.class));
                //Notify the ListView to update changes
                foodDonationAdapter.notifyDataSetChanged();
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
        });

        //Setting up list click listener for food donation
        initializeFoodDonationListListener();
    }

    void initializeClothDonationList() {
        //This arrayList is for listView onItemClickListener
        clothDonationAdapter = new ClothDonationAdapter(EditEntryActivity.this, new ArrayList<>());
        donationList.setAdapter(clothDonationAdapter);
        clothDonationAdapter.notifyDataSetChanged();

        //Doing query according to logged in user's "UID"
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Donations").child("Cloth").orderByChild("uid")
                .equalTo(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        databaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Add products to adapter to show on MainActivity
                clothDonationAdapter.add(snapshot.getValue(ClothDonation.class));
                //Notify the ListView to update changes
                clothDonationAdapter.notifyDataSetChanged();
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
        });

        //Setting up list click listener for cloth donation
        initializeClothDonationListListener();
    }

    void initializeFoodDonationListListener() {
        donationList.setOnItemClickListener((adapterView, view, i, l) -> {
            FoodDonation donation = foodDonationAdapter.getItem(i);
            Intent intent = new Intent(EditEntryActivity.this, FoodDonationEditActivity.class);
            intent.putExtra("passedDonation", donation);
            startActivity(intent);
        });
    }

    void initializeClothDonationListListener() {
        donationList.setOnItemClickListener((adapterView, view, i, l) -> {
            ClothDonation donation = clothDonationAdapter.getItem(i);
            Intent intent = new Intent(EditEntryActivity.this, ClothDonationEditActivity.class);
            intent.putExtra("passedDonation", donation);
            startActivity(intent);
        });
    }
}