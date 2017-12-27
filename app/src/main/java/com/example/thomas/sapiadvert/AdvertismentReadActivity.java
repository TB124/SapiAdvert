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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvertismentReadActivity extends AppCompatActivity {

    private Advertisment advertisment;
    private TextView titleTextView;
    private TextView detailsTextView;
    private ImageView mainPictureImageView;
    private ImageView profilePictureImageView;
    private Button callButton;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisment_read);
        advertisment= getIntent().getExtras().getParcelable("Advertisment");

        titleTextView=findViewById(R.id.ad_read_titleTextView);
        detailsTextView=findViewById(R.id.ad_read_detailsTextView);
        mainPictureImageView=findViewById(R.id.ad_read_mainPictureImageView);
        profilePictureImageView=findViewById(R.id.ad_read_profilePictureImageView);
        callButton=findViewById(R.id.ad_read_callButton);

        databaseReference= FirebaseDatabase.getInstance().getReference();

        titleTextView.setText(advertisment.getTitle());
        detailsTextView.setText(advertisment.getDetails());
        Glide.with(AdvertismentReadActivity.this).load(advertisment.getMainPictureUri()).into(mainPictureImageView);
        Glide.with(AdvertismentReadActivity.this).load(advertisment.getProfilePictureUri()).into(profilePictureImageView);

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO jerry profilnezo intetjet meghivni
                //finish();
               // startActivity(new Intent(this, MainActivity.class));
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Users").
                        child(advertisment.getCreatedBy()).
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


    }
}
