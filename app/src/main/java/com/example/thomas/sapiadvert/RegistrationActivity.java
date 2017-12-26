package com.example.thomas.sapiadvert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_INTENT =3 ;
    private Button registerButton;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView signInTextView;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    ///
    private ImageView profilePictureInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneNumberInput;
    private EditText confirmPasswordInput;

    private String emailAddress;
    private String password;
    private String passwordConfirm;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    ///
    //Storage
    private StorageReference firebaseStorage;
    private Uri uri;
    ///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null ){
            // Start User activities --> MainActivity
            startMainActivity();
        }

        registerButton = (Button) findViewById(R.id.registerButton);
        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        signInTextView = (TextView) findViewById(R.id.signInTextView);

        progressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);

        ///
        profilePictureInput=(ImageView) findViewById(R.id.profilePictureInput);
        firstNameInput=(EditText) findViewById(R.id.firstNameInput);
        lastNameInput=(EditText) findViewById(R.id.lastNameInput);
        phoneNumberInput=(EditText) findViewById(R.id.phoneNumberInput);
        confirmPasswordInput=(EditText) findViewById(R.id.confirmPasswordInput);

        profilePictureInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        ///Storage
        firebaseStorage= FirebaseStorage.getInstance().getReference();

    }

    @Override
    public void onClick(View view) {
        if ( view == signInTextView ){
            //Login
            backToLoginPage();
        }

        if ( view == registerButton ){
            //Register
            registerUser();
        }
    }

    private void registerUser() {
        emailAddress = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        passwordConfirm=confirmPasswordInput.getText().toString().trim();
        firstName=firstNameInput.getText().toString().trim();
        lastName=lastNameInput.getText().toString().trim();
        phoneNumber=phoneNumberInput.getText().toString().trim();

        if(uri ==null){
            Toast.makeText(this, "Please select a profile picture !",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please enter your first name!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please enter your last name!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(emailAddress)||(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())){
            // email field is empty -- display message and return
            Toast.makeText(this, "Please enter a valid email address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)|| (!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber))){
            Toast.makeText(this, "Please enter a valid phone number!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            // password field is empty -- display message and return
            Toast.makeText(this, "Please enter a password!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(passwordConfirm)){
            Toast.makeText(this, "Password confirmation failed !",Toast.LENGTH_SHORT).show();
            return;
        }

        // Display progress dialog to the User
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()){
                            //User successfully registered and logged in
                            //Displaying message
                            Toast.makeText(RegistrationActivity.this, "You have successfully registered!",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            //Database
                            FirebaseUser currentUser=firebaseAuth.getCurrentUser();
                            HashMap<String,String> datas=new HashMap<>();
                            datas.put("FirstName",firstName);
                            datas.put("LastName",lastName);
                            datas.put("EmailAddress",emailAddress);
                            datas.put("PhoneNumber",phoneNumber);
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().
                                    child("Users")
                                    .child(currentUser.getUid());

                            StorageReference filepath=firebaseStorage.child("ProfilePictures").child(currentUser.getUid());
                            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(RegistrationActivity.this,"Profile pic upload succes !",Toast.LENGTH_LONG).show();
                                }
                            });

                            datas.put("ProfilePicture",filepath.getDownloadUrl().toString());
                            databaseReference.setValue(datas);
                            ///
                            startMainActivity();
                        }
                        else{
                            // Registration was not successful

                            Toast.makeText(RegistrationActivity.this, "Registration failed! Error:"+ task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void backToLoginPage(){
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void startMainActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
    //Image select
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GALLERY_INTENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                uri=data.getData();
               // StorageReference filepath=firebaseStorage.child("ProfilePictures").
                //Glide.with(this).load(uri).into(profilePictureInput);
                Glide.with(RegistrationActivity.this).load(uri).into(profilePictureInput);
                //profilePictureInput.setImageURI(uri);

            }
        }
    }
}
