package com.rashed.donofood.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rashed.donofood.R;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    ImageView fullImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        //Hide the toolbar to provide full screen
        if(getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        fullImageView = findViewById(R.id.fullImageView);

        String imageFileName = getIntent().getStringExtra("imageFileName");

        //Retrieve image URL from database and show
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("images/" + imageFileName);
        storage.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString())
                    .placeholder(R.drawable.progress_animation).into(fullImageView);
        });
    }
}