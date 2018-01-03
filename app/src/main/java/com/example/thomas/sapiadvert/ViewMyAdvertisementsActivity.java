package com.example.thomas.sapiadvert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to view the advertisements created by the curently logged in user
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */
public class ViewMyAdvertisementsActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    private List<AdvertisementMy> advertisementList;
    private List<String> advertisementKeyList;
    private RecyclerView.Adapter adapter;

    private RecyclerView myAdvertisementsRecyclerView;
    private Button backButton;

    /**
     * initialising the activity
     * -firebase database connection setup
     * -firebase child listeners-> downloading the advertisement created by th current user
     * @param savedInstanceState savedInstance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_advertisements);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        myAdvertisementsRecyclerView=findViewById(R.id.view_my_ad_recyclerView);
        myAdvertisementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        advertisementList=new ArrayList<>();
        advertisementKeyList=new ArrayList<>();

        adapter=new MyAdvertisementsAdapter(advertisementList, advertisementKeyList, this);


        myAdvertisementsRecyclerView.setAdapter(adapter);
/*
        String k=currentUser.getUid();
        int i=0;
        for(Advertisement ad:MainActivity.advertisementList){
            if(ad.getCreatedBy().equals(k)){

                advertisementKeyList.add(MainActivity.advertisementKeyList.get(i));
                advertisementList.add(new AdvertisementMy(
                        ad.getTitle(),
                        ad.getDetails(),
                        ad.getMainPictureUri(),
                        ad.getLongitude(),
                        ad.getLatitude()
                ));

            }
            ++i;
        }
        updateUI();
        */

        databaseReference.child("Advertisements").orderByChild("CreatedBy").equalTo(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AdvertisementInDatabase adInDatabase =dataSnapshot.getValue(AdvertisementInDatabase.class);

               // Toast.makeText(ViewMyAdvertisementsActivity.this,adInDatabase.toString(), Toast.LENGTH_LONG).show();
                advertisementList.add(new AdvertisementMy(
                        adInDatabase.Title,
                        adInDatabase.Details,
                        adInDatabase.MainPicture,
                        adInDatabase.Longitude,
                        adInDatabase.Latitude
                ));
                advertisementKeyList.add(dataSnapshot.getKey());
                updateUI();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AdvertisementInDatabase tempAd=dataSnapshot.getValue(AdvertisementInDatabase.class);
                String key=dataSnapshot.getKey();
                int index=advertisementKeyList.indexOf(key);
                AdvertisementMy ad=advertisementList.get(index);
                ad.setDetails(tempAd.Details);
                ad.setTitle(tempAd.Title);
                ad.setMainPictureUri(tempAd.MainPicture);
                updateUI();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key=dataSnapshot.getKey();
                int index=advertisementKeyList.indexOf(key);
                advertisementList.remove(index);
                advertisementKeyList.remove(index);
                updateUI();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    /**
    upating the UI
     */
    private void updateUI(){
        adapter.notifyDataSetChanged();
    }

    /**
    back to edit page
     */
    private void backToEditPage() {
        finish();
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    /**
     *  processing clikc on the backbutton
     * @param view the viewo where the click event happened
     */
    @Override
    public void onClick(View view) {
        if (view == backButton){
            backToEditPage();
        }
    }
}
