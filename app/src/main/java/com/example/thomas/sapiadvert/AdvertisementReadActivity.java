package com.example.thomas.sapiadvert;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Activity for reading an advertisement
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */
public class AdvertisementReadActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Advertisement advertisement;
    private TextView titleTextView;
    private TextView detailsTextView;
    private ImageView mainPictureImageView;
    private ImageView profilePictureImageView;
    private Button callButton;

    private DatabaseReference databaseReference;

    /**
     * Initialising the activity
     * Reading the information about the advertisement from the intent
     * Setting up listeners for the apropiate buttons,views
     * @param savedInstanceState Saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_read);
        advertisement = getIntent().getExtras().getParcelable("Advertisement");

        titleTextView=findViewById(R.id.ad_read_titleTextView);
        detailsTextView=findViewById(R.id.ad_read_detailsTextView);
        mainPictureImageView=findViewById(R.id.ad_read_mainPictureImageView);
        profilePictureImageView=findViewById(R.id.main_ac_profilePictureImageView);
        callButton=findViewById(R.id.ad_read_callButton);

        databaseReference= FirebaseDatabase.getInstance().getReference();

        titleTextView.setText(advertisement.getTitle());
        detailsTextView.setText(advertisement.getDetails());
        Glide.with(AdvertisementReadActivity.this).load(advertisement.getMainPictureUri()).into(mainPictureImageView);
        Glide.with(AdvertisementReadActivity.this).load(advertisement.getProfilePictureUri()).into(profilePictureImageView);

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdvertisementReadActivity.this,ViewProfileActivity.class);
                intent.putExtra("UserID", advertisement.getCreatedBy());
                startActivity(intent);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Users").
                        child(advertisement.getCreatedBy()).
                        child("PhoneNumber").
                        addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String phoneNumber=dataSnapshot.getValue(String.class);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ad_read_map);
        mapFragment.getMapAsync(this);
    }

    /**
     * setting up the google maps fragment,creating a market to the location of the advertisement
     * @param googleMap googleMaps reference
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
       // Toast.makeText(AdvertisementReadActivity.this,advertisement.getLongitude()+":"+advertisement.getLatitude(),Toast.LENGTH_LONG).show();

       LatLng loc = new LatLng(advertisement.getLatitude(), advertisement.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(loc)
                .title("Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

       // Toast.makeText(AdvertisementReadActivity.this,advertisement.getLongitude()+":"+advertisement.getLatitude(),Toast.LENGTH_LONG).show();
    }
}
