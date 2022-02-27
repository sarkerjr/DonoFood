package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rashed.donofood.R;

public class SearchActivity extends AppCompatActivity {

    Spinner donationSearchOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        donationSearchOption = findViewById(R.id.donation_search_option_spinner);
        donationSearchOption.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Food", "Cloth"}));
    }
}