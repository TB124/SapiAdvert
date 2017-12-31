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
 * Activity to view the advertisments created by the curently logged in user
 */
public class ViewMyAdvertismentsActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    private List<AdvertismentMy> advertismentList;
    private List<String> advertismentKeyList;
    private RecyclerView.Adapter adapter;

    private RecyclerView myAdvertismentsRecyclerView;
    private Button backButton;

    /**
     * initialising the activity
     * -firebase databse connection setup
     * -firebase child listeners-> downloading the advertisment created by th curent user
     * @param savedInstanceState savedInstance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_advertisments);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        myAdvertismentsRecyclerView=findViewById(R.id.view_my_ad_recyclerView);
        myAdvertismentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        advertismentList=new ArrayList<>();
        advertismentKeyList=new ArrayList<>();

        adapter=new MyAdvertismentsAdapter(advertismentList, advertismentKeyList, this);


        myAdvertismentsRecyclerView.setAdapter(adapter);
/*
        String k=currentUser.getUid();
        int i=0;
        for(Advertisment ad:MainActivity.advertismentList){
            if(ad.getCreatedBy().equals(k)){

                advertismentKeyList.add(MainActivity.advertismentKeyList.get(i));
                advertismentList.add(new AdvertismentMy(
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

        databaseReference.child("Advertisments").orderByChild("CreatedBy").equalTo(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AdvertismentInDatabase adInDatabase =dataSnapshot.getValue(AdvertismentInDatabase.class);

               // Toast.makeText(ViewMyAdvertismentsActivity.this,adInDatabase.toString(), Toast.LENGTH_LONG).show();
                advertismentList.add(new AdvertismentMy(
                        adInDatabase.Title,
                        adInDatabase.Details,
                        adInDatabase.MainPicture,
                        adInDatabase.Longitude,
                        adInDatabase.Latitude
                ));
                advertismentKeyList.add(dataSnapshot.getKey());
                updateUI();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AdvertismentInDatabase tempAd=dataSnapshot.getValue(AdvertismentInDatabase.class);
                String key=dataSnapshot.getKey();
                int index=advertismentKeyList.indexOf(key);
                AdvertismentMy ad=advertismentList.get(index);
                ad.setDetails(tempAd.Details);
                ad.setTitle(tempAd.Title);
                ad.setMainPictureUri(tempAd.MainPicture);
                updateUI();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key=dataSnapshot.getKey();
                int index=advertismentKeyList.indexOf(key);
                advertismentList.remove(index);
                advertismentKeyList.remove(index);
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
