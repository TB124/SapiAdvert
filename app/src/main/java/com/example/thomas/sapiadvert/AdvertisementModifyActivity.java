package com.example.thomas.sapiadvert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Activity for modifying an existing advertisement
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */
public class AdvertisementModifyActivity extends AppCompatActivity{

    private final String tag="Ad-ModifyActivity";
    private static final int GALLERY_INTENT =123 ;
    private static final int PLACE_PICKER_REQUEST = 1;
    private AdvertisementMy advertisement;
    private AdvertisementInDatabase advertisementInDatabase;
    private String advertisementKey;
    private EditText titleEditText;
    private EditText detailssEditText;
    private ImageView mainPictureImageView;
    private Button modifyAdvertisementButton;
    private Button selectLocationButton;
    private Button deleteAdvertisementButton;
    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Uri mainImage=null;
    private boolean profileModified=false;


    /**
     * Initializing the activity
     * Reading the information about the advertisement from the intent
     * @param savedInstanceState Saved instances
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_modify);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        advertisement= getIntent().getExtras().getParcelable("Advertisement");
        advertisementKey=getIntent().getStringExtra("AdvertisementKey");
        advertisementInDatabase =new AdvertisementInDatabase(
                firebaseAuth.getCurrentUser().getUid(),
                advertisement.getTitle(),
                advertisement.getDetails(),
                advertisement.getMainPictureUri(),
                advertisement.getLongitude(),
                advertisement.getLatitude()
        );

        titleEditText=findViewById(R.id.ad_modify_titleEditText);
        detailssEditText=findViewById(R.id.ad_modify_detailsEditText);
        mainPictureImageView=findViewById(R.id.ad_modify_mainPictureImageView);
        modifyAdvertisementButton=findViewById(R.id.ad_modify_modifyAdvertisementButton);
        selectLocationButton=findViewById(R.id.ad_modify_selectLocationButton);
        deleteAdvertisementButton=findViewById(R.id.ad_modify_deleteAdvertisementButton);

        titleEditText.setText(advertisement.getTitle());
        detailssEditText.setText(advertisement.getDetails());
        Glide.with(AdvertisementModifyActivity.this).load(advertisement.getMainPictureUri()).into(mainPictureImageView);


        deleteAdvertisementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        AdvertisementModifyActivity.this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        firebaseStorage.child(advertisementKey).delete();
                        databaseReference.child("Advertisements").child(advertisementKey).removeValue();
                        finish();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    } }).create();

                // Setting Dialog Title
                alertDialog.setTitle("Alert Dialog");

                // Setting Dialog Message
                alertDialog.setMessage("Welcome to AndroidHive.info");

                // Setting OK Button


                // Showing Alert Message
                alertDialog.show();
            }
        });

        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AdvertisementModifyActivity.this), PLACE_PICKER_REQUEST);
                }
                catch(Throwable ex){
                    Log.e(tag, ex.toString());
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
        modifyAdvertisementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdvertisementModifyActivity.this, "MODIFY !", Toast.LENGTH_LONG).show();
                advertisementInDatabase.Title=titleEditText.getText().toString().trim();
                advertisementInDatabase.Details=detailssEditText.getText().toString().trim();

                if(TextUtils.isEmpty(advertisementInDatabase.Title)){
                    Toast.makeText(AdvertisementModifyActivity.this,"Please enter a title !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(advertisementInDatabase.Details)){
                    Toast.makeText(AdvertisementModifyActivity.this,"Please enter some details !",Toast.LENGTH_LONG).show();
                    return;
                }





                if(profileModified) {
                    if(mainImage==null){
                        Toast.makeText(AdvertisementModifyActivity.this,"Please select a picture !",Toast.LENGTH_LONG).show();
                        return;
                    }
                    StorageReference filepath = firebaseStorage.child("AdvertisementPictures").child(advertisementKey);
                    filepath.putFile(mainImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            advertisementInDatabase.MainPicture=taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(AdvertisementModifyActivity.this, "Main pic upload succes !", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().
                                    child("Advertisements")
                                    .child(advertisementKey).setValue(advertisementInDatabase);
                            finish();

                            //startMainActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdvertisementModifyActivity.this, "Error!:" + e, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(AdvertisementModifyActivity.this, "Profil nem valtozott !", Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance().getReference().
                            child("Advertisements")
                            .child(advertisementKey).setValue(advertisementInDatabase);
                    finish();
                }
            }
        });


    }

    /**
     * function to return to the main activity
     */
    private void returnToMain(){
        finish();
        startActivity( new Intent(this, MainActivity.class));

    }

    /**
     * helper function to get result from an image selection
     * or to get the result from a google maps select location event
     * @param requestCode intent code
     * @param resultCode result code
     * @param data result
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case GALLERY_INTENT:{
                if (resultCode == RESULT_OK) {
                    mainImage=data.getData();
                    profileModified=true;
                    Glide.with(AdvertisementModifyActivity.this).load(mainImage).into(mainPictureImageView);
                }
            }
            case PLACE_PICKER_REQUEST:{
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    if(place!=null) {
                        String toastMsg = String.format("Place: %s", place.getName());
                        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                        advertisementInDatabase.Longitude = place.getLatLng().longitude;
                        advertisementInDatabase.Latitude = place.getLatLng().latitude;
                    }
                }

                break;
            }

        }
    }
}
