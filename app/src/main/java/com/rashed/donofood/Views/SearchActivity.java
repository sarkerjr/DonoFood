package com.rashed.donofood.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rashed.donofood.Models.FoodDonation;
import com.rashed.donofood.R;
import com.rashed.donofood.Utils.DonationAdapter;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Query databaseReference;
    DonationAdapter donationAdapter;
    ListView listView;
    Spinner donationSearchOption;
    Button donationSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        donationSearchButton = findViewById(R.id.donation_search_button);
        listView = findViewById(R.id.donation_list_view);


        donationSearchOption = findViewById(R.id.donation_search_option_spinner);
        donationSearchOption.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Veg", "Non-Veg"}));

        //This arrayList is for listView onItemClickListener
        donationAdapter = new DonationAdapter(SearchActivity.this, new ArrayList<FoodDonation>());
        listView.setAdapter(donationAdapter);
        donationAdapter.notifyDataSetChanged();

        //Database references for doing operation on Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Donations").child("Food").orderByChild("foodType")
                .equalTo(donationSearchOption.getSelectedItem().toString());

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Add products to adapter to show on MainActivity
                donationAdapter.add(dataSnapshot.getValue(FoodDonation.class));
                //Notify the ListView to update changes
                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Something Wrong!", Toast.LENGTH_LONG).show();
            }
        };

        //Adding listener to a specific database end point
        databaseReference.addChildEventListener(listener);

        //Open the list item in which user clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodDonation donation = (FoodDonation) donationAdapter.getItem(i);
//                Intent intent = new Intent(SearchActivity.this, ViewActivity.class);
//                intent.putExtra("selectedAdd", donations);
//                startActivity(intent);
                Toast.makeText(SearchActivity.this, donation.getImageFileName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Adding search option feature to the SEARCH button
        donationSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Remove the previous listener
                databaseReference.removeEventListener(listener);
                //Clearing old arrayList and adapter
                donationAdapter.clear();

                //Updating the database reference with new option selected
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("Donations").child("Food").orderByChild("foodType")
                        .equalTo(donationSearchOption.getSelectedItem().toString());

                //Updating the listView according to selection of Location
                ChildEventListener listener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //Add products to adapter to show on MainActivity
                        donationAdapter.add(dataSnapshot.getValue(FoodDonation.class));
                        //Notify the ListView to update changes
                        donationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        donationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        donationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        donationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SearchActivity.this, "Something Wrong!", Toast.LENGTH_LONG).show();
                    }
                };
                databaseReference.addChildEventListener(listener);
            }
        });
    }
}
