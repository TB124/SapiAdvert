package com.example.thomas.sapiadvert;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

public class EditProfileActivity extends Activity implements View.OnClickListener {

    private static final int GALLERY_INTENT =3 ;
    // Buttons
    private Button saveButton;
    private Button cancelButton;
    private Button changePasswordButton;
    // Edit Texts
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneNumberInput;
    private EditText emailInput;
    private ImageView profilePictureInput;
    // User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    // Database
    private DatabaseReference databaseReference;
    // Storage
    private StorageReference firebaseStorage;
    private Uri profileURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // Buttons
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(this);
        // EditTexts
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        phoneNumberInput = (EditText) findViewById(R.id.phoneNumberInput);
        profilePictureInput=(ImageView) findViewById(R.id.profilePictureInput);
        profilePictureInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        // Firebase
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if ( firebaseAuth.getCurrentUser()  == null ){
            // Handle
            startMainActivity();
        }
        currentUser=firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInDatabase temp = dataSnapshot.getValue(UserInDatabase.class);
                        firstNameInput.setText(temp.FirstName);
                        lastNameInput.setText(temp.LastName);
                        emailInput.setText(temp.EmailAddress);
                        phoneNumberInput.setText(temp.PhoneNumber);
                        Glide.with(EditProfileActivity.this).load(temp.ProfilePicture).into(profilePictureInput);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle
                        startMainActivity();
                    }
                });
    }

    private void updateProfile(){
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Users").child(currentUser.getUid()).setValue(
                    new UserInDatabase(
                            emailInput.getText().toString().trim(),
                            firstNameInput.getText().toString().trim(),
                            lastNameInput.getText().toString().trim(),
                            phoneNumberInput.getText().toString().trim(),
                            profileURI.toString()
                    )
            );
            Toast.makeText(this, "Profile updated!",Toast.LENGTH_SHORT).show();
        }
        catch(Error e){
            Toast.makeText(this, "Failed to update Profile!",Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        if (view == cancelButton ){
            startMainActivity();
        }

        if (view == saveButton) {
            updateProfile();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GALLERY_INTENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                profileURI=data.getData();
                Glide.with(EditProfileActivity.this).load(profileURI).into(profilePictureInput);
            }
        }
    }
}
