package com.example.thomas.sapiadvert;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

/**
 * This Activity will show the logged in users' profile, and give him the possibility
 * to edit the data. In this activity the user can list his own advertisements, change
 * his password, and go back to the main activity.
 */
public class EditProfileActivity extends Activity implements View.OnClickListener {
    private final String tag = "EditProfile Activity:";
    private final String defaultProfilePicture = "https://firebasestorage.googleapis.com/v0/b/sapiadvert.appspot.com/o/ProfilePictures%2Fprofile.png?alt=media&token=b2e66197-1724-49b4-8b30-077f50ae72c1";
    private static final int GALLERY_INTENT =3 ;
    // Buttons
    private Button saveButton;
    private Button cancelButton;
    private Button changePasswordButton;
    private Button viewMyAdvertisementsButton;
    // Change password dialog
    private ChangePassword changePasswordDialog;
    // Edit Texts
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneNumberInput;
    private EditText emailInput;
    private ImageView profilePictureInput;
    private String profilePictureTemp;
    // User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    // Database
    private DatabaseReference databaseReference;
    // Storage
    private StorageReference firebaseStorage;
    private Uri profileURI;
    /**
     * Setting up the view, linking the view components, setting up Firebase,
     * and loading the Data of the logged in user.
     * @param savedInstanceState Saved instances.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // Buttons
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(this);
        viewMyAdvertisementsButton = findViewById(R.id.viewMyAdvertisementsButton);
        viewMyAdvertisementsButton.setOnClickListener(this);
        // EditTexts
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        profilePictureInput= findViewById(R.id.profilePictureInput);
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
                        if (temp != null ) {
                            firstNameInput.setText(temp.FirstName);
                            lastNameInput.setText(temp.LastName);
                            emailInput.setText(temp.EmailAddress);
                            phoneNumberInput.setText(temp.PhoneNumber);
                            if (temp.ProfilePicture != null ) {
                                Glide.with(EditProfileActivity.this).load(temp.ProfilePicture).into(profilePictureInput);
                                profilePictureTemp = temp.ProfilePicture;
                            } else {
                                Glide.with(EditProfileActivity.this).load(defaultProfilePicture).into(profilePictureInput);
                                profilePictureTemp = defaultProfilePicture;
                            }
                        } else {
                            Glide.with(EditProfileActivity.this).load(defaultProfilePicture).into(profilePictureInput);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle
                        startMainActivity();
                    }
                });
    }

    /**
     * Setting the new Data for the user, according to the input fields.
     */
    private void updateProfile(){
        try {
            final UserInDatabase data = new UserInDatabase(
                    emailInput.getText().toString().trim(),
                    firstNameInput.getText().toString().trim(),
                    lastNameInput.getText().toString().trim(),
                    phoneNumberInput.getText().toString().trim(),
                    profilePictureTemp
            );

            final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().
                    child("Users")
                    .child(currentUser.getUid());

            if ( profileURI != null ) {
                StorageReference filepath = firebaseStorage.child("ProfilePictures").child(currentUser.getUid());
                filepath.putFile(profileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        data.ProfilePicture = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.setValue(data);
                    }
                });
            } else {
                databaseReference.setValue(data);
            }

            Toast.makeText(this, "Profile updated!",Toast.LENGTH_SHORT).show();
        }
        catch(Error e){
            Toast.makeText(this, "Failed to update Profile!",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Closing the current activity, and starting the main activity.
     */
    private void startMainActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
    /**
     * Closing the current activity, and list the users advertisements.
     */
    private void viewMyAdvertisements(){
        finish();
        startActivity(new Intent(this, ViewMyAdvertismentsActivity.class));
    }
    /**
     * OnClick listeners for the elements.
     * @param view the vew which was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view == cancelButton ){
            startMainActivity();
        }

        if (view == saveButton) {
            updateProfile();
        }

        if (view == changePasswordButton) {
            changePasswordDialog = new ChangePassword(this);
            changePasswordDialog.show();
        }
        if (view == viewMyAdvertisementsButton){
            viewMyAdvertisements();
        }
    }

    /**
     * The result of the Gallery intent, to get a picture input form the user.
     * @param requestCode request type (Gallery intent in this case)
     * @param resultCode result (the result of the intent)
     * @param data returned value.
     */
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
