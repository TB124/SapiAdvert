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

public class AdvertismentUploadActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int GALLERY_INTENT =123 ;
    private EditText titleEditText;
    private EditText detailssEditText;
    private ImageView mainPictureImageView;
    private Button postAdvertismentButton;
    private Button backToMainButton;
    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;

    private String title;
    private  String details;
    private Uri mainImage=null;
    private   HashMap<String,String> datas;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisment_upload);

        titleEditText=findViewById(R.id.titleEditText);
        detailssEditText=findViewById(R.id.detailsEditText);
        mainPictureImageView=findViewById(R.id.mainPictureImageView);
        postAdvertismentButton=findViewById(R.id.postAdvertismentButton);
        backToMainButton=findViewById(R.id.backToMainButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance().getReference();
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
                title=titleEditText.getText().toString().trim();
                details=detailssEditText.getText().toString().trim();

                if(TextUtils.isEmpty(title)){
                    Toast.makeText(AdvertismentUploadActivity.this,"Please enter a title !",Toast.LENGTH_LONG);
                    return;
                }
                if(TextUtils.isEmpty(details)){
                    Toast.makeText(AdvertismentUploadActivity.this,"Please enter some details !",Toast.LENGTH_LONG);
                    return;
                }
                if(mainImage==null){
                    Toast.makeText(AdvertismentUploadActivity.this,"Please select a picture !",Toast.LENGTH_LONG);
                    return;
                }

                FirebaseUser currentUser=firebaseAuth.getCurrentUser();
                datas=new HashMap<>();
                datas.put("CreatedBy",currentUser.getUid());
                datas.put("Title",title);
                datas.put("Details",details);

                final String key=FirebaseDatabase.getInstance().getReference().
                        child("Advertisments")
                        .push().getKey();

                StorageReference filepath=firebaseStorage.child("AdvertisementPictures").child(key);
                filepath.putFile(mainImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        datas.put("MainPicture",taskSnapshot.getDownloadUrl().toString());
                        Toast.makeText(AdvertismentUploadActivity.this,"Main pic upload succes !",Toast.LENGTH_LONG).show();

                        FirebaseDatabase.getInstance().getReference().
                                child("Advertisments")
                                .child(key).setValue(datas);
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
        getLocationPermission();
    }
    private void returnToMain(){
        finish();
        startActivity( new Intent(this, MainActivity.class));

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GALLERY_INTENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mainImage=data.getData();
                // StorageReference filepath=firebaseStorage.child("ProfilePictures").
                //Glide.with(this).load(uri).into(profilePictureInput);
                Glide.with(AdvertismentUploadActivity.this).load(mainImage).into(mainPictureImageView);
                //profilePictureInput.setImageURI(uri);

            }
        }
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(AdvertismentUploadActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
}