package com.example.thomas.sapiadvert;

import android.content.Intent;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

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

import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView currentUserTextView;
    private Button signOutButton;
    private FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 123;

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
    //seeach
    private EditText searchEditText;
    private List<Advertisment> searchedAdvertismentList;
    private List<String> searchedAdvertismentKeyList;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if ( firebaseAuth.getCurrentUser() == null ){
            backToLoginPage();
        }
        currentUser = firebaseAuth.getCurrentUser();

        currentUserTextView = (TextView) findViewById(R.id.currentUserTextView);
        currentUserTextView.setText("Hello "+currentUser.getEmail()+"!");
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);

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
                                        tempAd.MainPicture)
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

    }

    @Override
    public void onClick(View view) {
        if (view == signOutButton ){
            // Signing out
            firebaseAuth.signOut();
            backToLoginPage();
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
    //
    private void updateUI(){
        adapter.notifyDataSetChanged();
    }
}
