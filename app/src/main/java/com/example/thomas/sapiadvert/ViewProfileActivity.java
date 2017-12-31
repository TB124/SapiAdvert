package com.example.thomas.sapiadvert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This activity's purpose is to show the data
 * of a user. The users ID is received though intent and then
 * loads the data from Firebase, with the help of the ID.
 */
public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private final String tag="ViewProfileActivity";
    // View elements
    private EditText firstNameTextView;
    private EditText lastNameTextView;
    private ImageView profilePhotoDisplay;
    private EditText phoneNumberTextView;
    private EditText emailTextView;
    private Button backButton;
    /**
     * Setting up the view, linking the view components, setting up Firebase,
     * and loading the Data of the given user.
     * @param savedInstanceState Saved instances.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        // VIew Components
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        profilePhotoDisplay = findViewById(R.id.profilePhotoDisplay);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        emailTextView = findViewById(R.id.emailTextView);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        // Get UserId from intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String userID = (String) bundle.get("UserID");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Users").child(userID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInDatabase temp = dataSnapshot.getValue(UserInDatabase.class);
                            firstNameTextView.setText(temp.FirstName);
                            lastNameTextView.setText(temp.LastName);
                            emailTextView.setText(temp.EmailAddress);
                            phoneNumberTextView.setText(temp.PhoneNumber);
                            Glide.with(ViewProfileActivity.this).load(temp.ProfilePicture).into(profilePhotoDisplay);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle
                            startMainActivity();
                        }
                    });
        }
    }
    /**
     * Close current Activity and start the Main Activity.
     */
    private void startMainActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
    /**
     * OnClick listeners for the elements.
     * @param view the vew which was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view == backButton ){
            startMainActivity();
        }
    }
}
