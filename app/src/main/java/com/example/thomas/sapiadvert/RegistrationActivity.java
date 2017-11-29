package com.example.thomas.sapiadvert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView signInTextView;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

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
    }

    @Override
    public void onClick(View view) {
        if ( view == signInTextView ){
            //Login
        }

        if ( view == registerButton ){
            //Register
            registerUser();
        }
    }

    private void registerUser() {
        String emailAddress = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(emailAddress)){
            // email field is empty -- display message and return
            Toast.makeText(this, "Please enter an email address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            // password field is empty -- display message and return
            Toast.makeText(this, "Please enter a password!",Toast.LENGTH_SHORT).show();
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
                            startMainActivity();
                        }
                        else{
                            // Registration was not successful
                            Toast.makeText(RegistrationActivity.this, "Registration failed!",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void startMainActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
