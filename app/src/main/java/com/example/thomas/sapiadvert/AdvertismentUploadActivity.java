package com.example.thomas.sapiadvert;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdvertismentUploadActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT =123 ;
    private static final int PLACE_PICKER_REQUEST = 1;
    private EditText titleEditText;
    private EditText detailssEditText;
    private ImageView mainPictureImageView;
    private Button postAdvertismentButton;
    private Button backToMainButton;
    private Button selectLocationButton;
    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;


    private Uri mainImage=null;

    private static final String TAG = "MapActivity";

    private boolean locationSelected=false;

///
AdvertismentInDatabase ad;
    ///
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisment_upload);

        ad=new AdvertismentInDatabase();
        titleEditText=findViewById(R.id.titleEditText);
        detailssEditText=findViewById(R.id.detailsEditText);
        mainPictureImageView=findViewById(R.id.mainPictureImageView);
        postAdvertismentButton=findViewById(R.id.postAdvertismentButton);
        backToMainButton=findViewById(R.id.backToMainButton);
        selectLocationButton=findViewById(R.id.selectLocationButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance().getReference();

        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AdvertismentUploadActivity.this), PLACE_PICKER_REQUEST);
                }
                catch(Throwable ex){

                }
            }
        });

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMain();
            }
        });
        mainPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        postAdvertismentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.Title=titleEditText.getText().toString().trim();
                ad.Details=detailssEditText.getText().toString().trim();

                if(TextUtils.isEmpty(ad.Title)){
                    Toast.makeText(AdvertismentUploadActivity.this,"Please enter a title !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(ad.Details)){
                    Toast.makeText(AdvertismentUploadActivity.this,"Please enter some details !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(mainImage==null){
                    Toast.makeText(AdvertismentUploadActivity.this,"Please select a picture !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!locationSelected){
                    Toast.makeText(AdvertismentUploadActivity.this,"Please select a location !",Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseUser currentUser=firebaseAuth.getCurrentUser();

                ad.CreatedBy=currentUser.getUid();

                final String key=FirebaseDatabase.getInstance().getReference().
                        child("Advertisments")
                        .push().getKey();

                StorageReference filepath=firebaseStorage.child("AdvertisementPictures").child(key);
                filepath.putFile(mainImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ad.MainPicture=taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(AdvertismentUploadActivity.this,"Main pic upload succes !",Toast.LENGTH_LONG).show();
                        FirebaseDatabase.getInstance().getReference().
                                child("Advertisments")
                                .child(key).setValue(ad);
                        returnToMain();

                        //startMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdvertismentUploadActivity.this,"Error!:"+e,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    private void returnToMain(){
        finish();
        startActivity( new Intent(this, MainActivity.class));

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode){
            case GALLERY_INTENT:{
                if (resultCode == RESULT_OK) {
                    mainImage = data.getData();
                    Glide.with(AdvertismentUploadActivity.this).load(mainImage).into(mainPictureImageView);
                    break;
                }
            }
            case PLACE_PICKER_REQUEST:{
                if (resultCode == RESULT_OK) {
                    locationSelected=true;
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    ad.Longitude=place.getLatLng().longitude;
                    ad.Latitude=place.getLatLng().latitude;


                }

                break;
            }

        }

    }


}
