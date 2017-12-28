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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdvertismentModifyActivity extends AppCompatActivity{

    private static final int GALLERY_INTENT =123 ;
    private static final int PLACE_PICKER_REQUEST = 1;

    private Advertisment advertisment;
    private AdvertismentInDatabase advertismentInDatabase;
    private String advertismentKey;

    private EditText titleEditText;
    private EditText detailssEditText;
    private ImageView mainPictureImageView;
    private Button modifyAdvertismentButton;
    private Button selectLocationButton;

    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;

    private Uri mainImage=null;
    private boolean profileModified=false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisment_modify);
        advertisment= getIntent().getExtras().getParcelable("Advertisment");
        advertismentKey=getIntent().getStringExtra("AdvertismentKey");
        advertismentInDatabase=new AdvertismentInDatabase(
                advertisment.getCreatedBy(),
                advertisment.getTitle(),
                advertisment.getDetails(),
                advertisment.getMainPictureUri(),
                advertisment.getLongitude(),
                advertisment.getLatitude()
        );

        titleEditText=findViewById(R.id.ad_modify_titleEditText);
        detailssEditText=findViewById(R.id.ad_modify_detailsEditText);
        mainPictureImageView=findViewById(R.id.ad_modify_mainPictureImageView);
        modifyAdvertismentButton=findViewById(R.id.ad_modify_modifyAdvertismentButton);
        selectLocationButton=findViewById(R.id.ad_modify_selectLocationButton);

        titleEditText.setText(advertisment.getTitle());
        detailssEditText.setText(advertisment.getDetails());
        Glide.with(AdvertismentModifyActivity.this).load(advertisment.getMainPictureUri()).into(mainPictureImageView);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance().getReference();

        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AdvertismentModifyActivity.this), PLACE_PICKER_REQUEST);
                }
                catch(Throwable ex){

                }
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
        modifyAdvertismentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdvertismentModifyActivity.this, "MODIFY !", Toast.LENGTH_LONG).show();
                advertismentInDatabase.Title=titleEditText.getText().toString().trim();
                advertismentInDatabase.Details=detailssEditText.getText().toString().trim();

                if(TextUtils.isEmpty(advertismentInDatabase.Title)){
                    Toast.makeText(AdvertismentModifyActivity.this,"Please enter a title !",Toast.LENGTH_LONG);
                    return;
                }
                if(TextUtils.isEmpty(advertismentInDatabase.Details)){
                    Toast.makeText(AdvertismentModifyActivity.this,"Please enter some details !",Toast.LENGTH_LONG);
                    return;
                }





                if(profileModified) {
                    if(mainImage==null){
                        Toast.makeText(AdvertismentModifyActivity.this,"Please select a picture !",Toast.LENGTH_LONG);
                        return;
                    }
                    StorageReference filepath = firebaseStorage.child("AdvertisementPictures").child(advertismentKey);
                    filepath.putFile(mainImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            advertismentInDatabase.MainPicture=taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(AdvertismentModifyActivity.this, "Main pic upload succes !", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().
                                    child("Advertisments")
                                    .child(advertismentKey).setValue(advertismentInDatabase);
                            returnToMain();

                            //startMainActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdvertismentModifyActivity.this, "Error!:" + e, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(AdvertismentModifyActivity.this, "Profil nem valtozott !", Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance().getReference().
                            child("Advertisments")
                            .child(advertismentKey).setValue(advertismentInDatabase);
                    returnToMain();
                }
            }
        });


    }
    private void returnToMain(){
        finish();
        startActivity( new Intent(this, MainActivity.class));

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case GALLERY_INTENT:{
                if (resultCode == RESULT_OK) {
                    mainImage=data.getData();
                    profileModified=true;
                    Glide.with(AdvertismentModifyActivity.this).load(mainImage).into(mainPictureImageView);
                }
            }
            case PLACE_PICKER_REQUEST:{
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    if(place!=null) {
                        String toastMsg = String.format("Place: %s", place.getName());
                        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                        advertismentInDatabase.Longitude = place.getLatLng().longitude;
                        advertismentInDatabase.Latitude = place.getLatLng().latitude;
                    }
                }

                break;
            }

        }
    }
}
