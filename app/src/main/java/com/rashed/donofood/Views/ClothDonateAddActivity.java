package com.rashed.donofood.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rashed.donofood.Models.ClothDonation;
import com.rashed.donofood.R;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ClothDonateAddActivity extends AppCompatActivity {
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    Spinner clothTypeSpinner;
    ImageView uploadClothImage;
    Button submitDonationBtn;
    EditText cloth_name_id;
    EditText cloth_quantity_id;
    EditText area_id;
    EditText phone_id;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth_donation);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivity(new Intent(ClothDonateAddActivity.this, SignInActivity.class));

        cloth_name_id = findViewById(R.id.input_cloth_name);
        cloth_quantity_id = findViewById(R.id.input_cloth_quantity_id);
        area_id = findViewById(R.id.input_area_id);
        phone_id = findViewById(R.id.input_phone_id);


        //Populating "Food Type" drop down menu
        clothTypeSpinner = findViewById(R.id.cloth_type_spinner);
        clothTypeSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Summer", "Winter"}));

        //Handling donation image
        uploadClothImage = findViewById(R.id.upload_cloth_image_id);
        submitDonationBtn = findViewById(R.id.submit_cloth_donation_btn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        uploadClothImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        submitDonationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadClothImage.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        //Get image file name from result
                        String fileName = taskSnapshot.getStorage().getName().toString();
                        uploadToDatabase(fileName);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ClothDonateAddActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    //Start uploading data to firebase database after receiving image FileName
    private void uploadToDatabase(String imageFileName) {
        String clothName = String.valueOf(cloth_name_id.getText());
        String clothType = clothTypeSpinner.getSelectedItem().toString();
        int quantity = Integer.parseInt(String.valueOf(cloth_quantity_id.getText()));
        String area = String.valueOf(area_id.getText());
        String phone = String.valueOf(phone_id.getText());

        writeDatabase(clothName, clothType, quantity, area, phone, imageFileName);
    }

    //Write data on database
    private void writeDatabase(String clothName, String clothType, int quantity, String area, String phone, String imageFileName) {
        //Get UUID from current logged in user
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        //creating object to store it on database
        ClothDonation donation = new ClothDonation(uid, clothName, clothType, quantity, area, phone, imageFileName);
        //getting firebase reference
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Donations").child("Cloth").push().setValue(donation)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(ClothDonateAddActivity.this, "Uploaded successfully.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ClothDonateAddActivity.this, DashboardActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(ClothDonateAddActivity.this, "Upload Failed! Try again.",
                        Toast.LENGTH_LONG).show());
    }
}