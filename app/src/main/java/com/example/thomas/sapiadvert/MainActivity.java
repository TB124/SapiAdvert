package com.example.thomas.sapiadvert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView currentUserTextView;
    private Button signOutButton;
    private FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 123;


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
}
