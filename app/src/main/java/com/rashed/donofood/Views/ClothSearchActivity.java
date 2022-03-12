package com.rashed.donofood.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rashed.donofood.Models.ClothDonation;
import com.rashed.donofood.R;
import com.rashed.donofood.Utils.ClothDonationAdapter;


import java.io.Serializable;
import java.util.ArrayList;

public class ClothSearchActivity extends AppCompatActivity {
    Query databaseReference;
    ClothDonationAdapter clothDonationAdapter;
    ListView listView;
    Spinner donateFoodType;
    Button donationSearchButton;
    EditText donationSearchInput;
    TextView donationNotFoundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth_search);

        donationSearchInput = findViewById(R.id.donation_search_input);
        donationNotFoundView = findViewById(R.id.donation_not_found_id);

        donationSearchButton = findViewById(R.id.donation_search_button);
        listView = findViewById(R.id.donation_list_view);


        donateFoodType = findViewById(R.id.donation_search_option_spinner);
        donateFoodType.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Summer", "Winter"}));

        //Initializing adapter for listview
        clothDonationAdapter = new ClothDonationAdapter(ClothSearchActivity.this, new ArrayList<ClothDonation>());
        listView.setAdapter(clothDonationAdapter);
        clothDonationAdapter.notifyDataSetChanged();

        //Database references for doing query operation on Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Donations").child("Cloth").orderByChild("clothType")
                .equalTo(donateFoodType.getSelectedItem().toString());

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Add products to adapter to show on MainActivity
                clothDonationAdapter.add(dataSnapshot.getValue(ClothDonation.class));
                //Notify the ListView to update changes
                clothDonationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                clothDonationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                clothDonationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                clothDonationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ClothSearchActivity.this, "Something Wrong!", Toast.LENGTH_LONG).show();
            }
        };

        //Adding listener to a specific database end point
        databaseReference.addChildEventListener(listener);

        //Open the list item in which user clicked on
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            ClothDonation donation = clothDonationAdapter.getItem(i);
            Intent intent = new Intent(ClothSearchActivity.this, ClothDonationViewActivity.class);
            intent.putExtra("selectedClothDonation", (Serializable) donation);
            startActivity(intent);
        });

        //Adding search option feature to the SEARCH button
        donationSearchButton.setOnClickListener(view -> {
            //Remove the previous listener
            databaseReference.removeEventListener(listener);
            //Clearing old arrayList and adapter
            clothDonationAdapter.clear();

            String donationSearchParam = donationSearchInput.getText().toString().trim();
            //Check if donation search query exist then do query on that parameter
            if(donationSearchParam.length() != 0) {
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Donations").child("Cloth").orderByChild("location")
                        .equalTo(donationSearchInput.getText().toString().trim());
            }else {
                //Updating the database reference with new option selected
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Donations").child("Cloth").orderByChild("clothType")
                        .equalTo(donateFoodType.getSelectedItem().toString());
            }

            //Updating the listView according to selection of Location
            ChildEventListener queryListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.exists()){
                        //Hide NOT FOUND message if query exist
                        listView.setVisibility(View.VISIBLE);
                        donationNotFoundView.setVisibility(View.GONE);

                        //Getting the current search object
                        ClothDonation donation = dataSnapshot.getValue(ClothDonation.class);

                        //If search param exist and is equal to "foodType" field
                        if(donationSearchParam.length() != 0 &&
                                donateFoodType.getSelectedItem().toString().
                                        equals(donation != null ? donation.getClothType() : null)){
                            clothDonationAdapter.add(dataSnapshot.getValue(ClothDonation.class));
                            clothDonationAdapter.notifyDataSetChanged();
                        }
                        //If no search param found then query throw "foodType" option
                        else if(donationSearchParam.length() == 0){
                            //Add products to adapter to show on MainActivity
                            clothDonationAdapter.add(dataSnapshot.getValue(ClothDonation.class));
                            //Notify the ListView to update changes
                            clothDonationAdapter.notifyDataSetChanged();
                        }
                    }else {
                        //Show NOT FOUND message if no query result found
                        listView.setVisibility(View.GONE);
                        donationNotFoundView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    clothDonationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    clothDonationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    clothDonationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ClothSearchActivity.this, "Something Wrong!", Toast.LENGTH_LONG).show();
                }
            };
            databaseReference.addChildEventListener(queryListener);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, DashboardActivity.class));
    }
}
