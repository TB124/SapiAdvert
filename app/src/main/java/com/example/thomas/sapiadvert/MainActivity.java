package com.example.thomas.sapiadvert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView currentUserTextView;
    private Button signOutButton;
    private FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 123;

    private Button editProfilButton;

    //Recycle view
    private List<Advertisment> advertismentList;
    private List<String> advertismentKeyList;
    private RecyclerView advertismentRecyclerView;
    private RecyclerView.Adapter adapter;
    ///
    //Database
    private DatabaseReference databaseReference;
    private String tempProfilePictureUri=new String();
    //
    //search
    private EditText searchEditText;
    private List<Advertisment> searchedAdvertismentList;
    private List<String> searchedAdvertismentKeyList;
    //
    //profile picture
    private ImageView profilePicture;
    //
    private TextView addNewAdvertismentButton;
    //for teszt only
    private Button logOutButton;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //profile piture
        profilePicture=findViewById(R.id.ad_read_profilePictureImageView);
        logOutButton=findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if ( currentUser == null ) {
            backToLoginPage();
            return;
        }



        /*
        currentUserTextView = (TextView) findViewById(R.id.currentUserTextView);
        currentUserTextView.setText("Hello "+currentUser.getEmail()+"!");

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
*/

        editProfilButton = (Button) findViewById(R.id.editProfileButton);
        editProfilButton.setOnClickListener(this);

        //RECYCLER VIEW
        advertismentRecyclerView=findViewById(R.id.advertismentRecylerView);
        advertismentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        advertismentList=new ArrayList<>();
        advertismentKeyList=new ArrayList<>();
        //search
        searchEditText=findViewById(R.id.searchTextView);
        searchedAdvertismentList=new ArrayList<>();
        searchedAdvertismentKeyList=new ArrayList<>();
        adapter=new MyAdapter(searchedAdvertismentList,this);
        advertismentRecyclerView.setAdapter(adapter);

        ///Database
        databaseReference= FirebaseDatabase.getInstance().getReference();

        (databaseReference.child("Advertisments")).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("PROBAAAA","MIIIERRRRRRRRRRRRRRT");

               final AdvertismentInDatabase tempAd=dataSnapshot.getValue(AdvertismentInDatabase.class);
                final String key=dataSnapshot.getKey();
                databaseReference.child("Users").
                        child(tempAd.CreatedBy).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserInDatabase t=dataSnapshot.getValue(UserInDatabase.class);
                                tempProfilePictureUri=t.ProfilePicture;
                                Log.i("CreatedBy",tempAd.CreatedBy);
                                Log.i("Title",tempAd.Title);
                                Log.i("Details",tempAd.Details);
                                Log.i("ProfilePicture",tempProfilePictureUri);
                                advertismentList.add(new Advertisment(
                                        tempAd.Title,
                                        tempAd.Details,
                                        tempAd.CreatedBy,
                                        tempProfilePictureUri,
                                        tempAd.MainPicture,
                                        tempAd.Longitude,
                                        tempAd.Latitude
                                        )
                                );
                                advertismentKeyList.add(key);
                                //myHandler.post(updateRunnable);
                               // updateUI();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.i("PROBAAAA","FAILED");
                                tempProfilePictureUri=null;

                            }
                        });

                //
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AdvertismentInDatabase tempAd=dataSnapshot.getValue(AdvertismentInDatabase.class);
                String key=dataSnapshot.getKey();
                int index=advertismentKeyList.indexOf(key);
                Advertisment ad=advertismentList.get(index);
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

                index=searchedAdvertismentKeyList.indexOf(key);
                if(-1!=index){
                    searchedAdvertismentList.remove(index);
                    searchedAdvertismentKeyList.remove(index);
                }
                updateUI();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        searchEditText.setOnEditorActionListener( new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Toast.makeText(MainActivity.this,"ENTER\n", Toast.LENGTH_LONG).show();
                    String s=".*"+textView.getText().toString()+".*";
                    search(s);
                }
                return false;
            }
        });

        addNewAdvertismentButton=findViewById(R.id.addNewAdvertismentButton);
        if ( currentUser == null ){
            // backToLoginPage();
            profilePicture.setVisibility(View.GONE);
            addNewAdvertismentButton.setVisibility(View.GONE);
        }
        else{
            databaseReference.child("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInDatabase u=dataSnapshot.getValue(UserInDatabase.class);
                    Glide.with(MainActivity.this).load(u.ProfilePicture).into(profilePicture);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // startActivity(new Intent(this,));
                }
            });
            addNewAdvertismentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // finish();
                    startActivity(new Intent(MainActivity.this, AdvertismentUploadActivity.class));
                }
            });
        }

    }

    @Override
    public void onClick(View view) {

        if (view == logOutButton ){
            finish();
            Advertisment ad=new Advertisment("Teszhirdetes","Desztdetails","HPThqE2j4WXeAeKcrjVINyvPnvi1",
                    "https://firebasestorage.googleapis.com/v0/b/sapiadvert.appspot.com/o/ProfilePictures%2FHPThqE2j4WXeAeKcrjVINyvPnvi1?alt=media&token=31c35abb-31d2-4e17-a05c-cff78f740374",
                    "https://firebasestorage.googleapis.com/v0/b/sapiadvert.appspot.com/o/AdvertisementPictures%2F-L1NTkRfNqHRe3mSqXLJ?alt=media&token=3af2561c-d45d-45e6-83d9-4fdfeda49027"
                    ,0,0
                    );
            Intent intent=new Intent(this, AdvertismentModifyActivity.class);
            intent.putExtra("Advertisment",ad);
            intent.putExtra("AdvertismentKey","-L1NTkRfNqHRe3mSqXLJ");
            startActivity(intent);

            // Signing out
           // firebaseAuth.signOut();
           // backToLoginPage();
        }

        if (view == editProfilButton) {
            // Switch view
            goToEditPage();
        }

    }

    private void backToLoginPage(){
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void search(String s){
        searchedAdvertismentList.clear();
        searchedAdvertismentKeyList.clear();
        int i=0;
        for (Advertisment temp : advertismentList) {
            if(Pattern.matches(s, temp.getTitle())||Pattern.matches(s, temp.getDetails())){
                Toast.makeText(MainActivity.this,temp.getTitle(), Toast.LENGTH_LONG).show();
                searchedAdvertismentList.add(temp);
                searchedAdvertismentKeyList.add(advertismentKeyList.get(i));
                Log.i("SEARCH",temp.getTitle());
            }
            ++i;
        }

        updateUI();
    }

    private void goToEditPage(){
        finish();
        startActivity(new Intent(this, EditProfileActivity.class));
    }
    //
    private void updateUI(){
        adapter.notifyDataSetChanged();
    }
}
