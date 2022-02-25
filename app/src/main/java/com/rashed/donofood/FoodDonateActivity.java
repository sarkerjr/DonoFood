package com.rashed.donofood;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rashed.donofood.Models.FoodDonation;

import java.io.IOException;
import java.util.UUID;

public class FoodDonateActivity extends AppCompatActivity {
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    Spinner foodTypeSpinner;
    ImageView uploadFoodImage;
    Button submitDonationBtn;
    EditText food_name_id;
    EditText food_quantity_id;
    EditText area_id;
    EditText phone_id;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_add);

        food_name_id = findViewById(R.id.input_food_name);
        food_quantity_id = findViewById(R.id.input_food_quantity_id);
        area_id = findViewById(R.id.input_area_id);
        phone_id = findViewById(R.id.input_phone_id);


        //Populating "Food Type" drop down menu
        foodTypeSpinner = findViewById(R.id.food_type_spinner);
        foodTypeSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{"Veg", "Non-Veg"}));

        //Handling donation image
        uploadFoodImage = findViewById(R.id.upload_food_image_id);
        submitDonationBtn = findViewById(R.id.submit_food_donation_btn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        uploadFoodImage.setOnClickListener(new View.OnClickListener() {
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
                uploadFoodImage.setImageBitmap(bitmap);
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
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            //Get image file name from result
                            String fileName = taskSnapshot.getStorage().getName().toString();
                            uploadToDatabase(fileName);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(FoodDonateActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    //Start uploading data to firebase database after receiving image url
    private void uploadToDatabase(String imageFileName) {
        String foodName = String.valueOf(food_name_id.getText());
        String foodType = foodTypeSpinner.getSelectedItem().toString();
        float quantity = Float.parseFloat(String.valueOf(food_quantity_id.getText()));
        String area = String.valueOf(area_id.getText());
        String phone = String.valueOf(phone_id.getText());

        writeDatabase(foodName, foodType, quantity, area, phone, imageFileName);
    }

    //Write data on database
    private void writeDatabase(String foodName, String foodType, float quantity, String area, String phone, String imageFileName) {
        //Get UUID from current logged in user
        String uuid = UUID.randomUUID().toString(); //TODO: implement real id retrieving code
        //creating object to store it on database
        FoodDonation donation = new FoodDonation(uuid, foodName, foodType, quantity, area, phone, imageFileName);
        //getting firebase reference
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Donations").child("Food").push().setValue(donation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(FoodDonateActivity.this, "Uploaded successfully.",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(FoodDonateActivity.this, DashboardActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FoodDonateActivity.this, "Upload Failed! Try again.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}