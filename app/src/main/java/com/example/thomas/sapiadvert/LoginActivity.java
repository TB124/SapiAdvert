package com.example.thomas.sapiadvert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * The Login Activity of the Application, it gives us three possibilities
 * Email login,Google login and guest login. It also gives us the option to reset
 * an account.
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String tag="LoginActivity";
    private static final int RC_SIGN_IN =123 ;
    private static final String TAG ="login" ;
    private Button loginButton;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView registerTextView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView forgetPasswordTextView;
    //google login
    private SignInButton googleSignInButton;
    GoogleApiClient googleApiClient;
    //
    private TextView loginAsGuestTextView;
    /**
     * Setting up the view, linking the view components, setting up Firebase,
     * and loading the Data of the logged in user.
     * @param savedInstanceState Saved instances.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null ){
            // Start User activities --> MainActivity
            startMainActivity();
        }
        loginButton = findViewById(R.id.login_ac_loginButton);
        emailInput = findViewById(R.id.login_ac_emailInput);
        passwordInput = findViewById(R.id.login_ac_passwordInput);
        registerTextView = findViewById(R.id.registerTextView);
        forgetPasswordTextView= findViewById(R.id.forgetPasswordTextView);
        progressDialog = new ProgressDialog(this);
        //
        loginButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        forgetPasswordTextView.setOnClickListener(this);
        ///google sign in
        googleSignInButton= findViewById(R.id.google_sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"Google login failed", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        //
        loginAsGuestTextView=findViewById(R.id.loginAsGuest);
        loginAsGuestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
    }
    /**
     * OnClick listeners for the two buttons.
     * @param view the vew which was clicked.
     */
    @Override
    public void onClick(View view) {
        if ( view == loginButton ){
            //Login
            loginUser();
        }
        if ( view == registerTextView ){
            //Register
           // finish();
            startActivity( new Intent(this, RegistrationActivity.class));
        }
        if ( view == forgetPasswordTextView ){
            ForgotPassword dialog = new ForgotPassword(this);
            dialog.show();
        }
    }

    /**
     * Get the strings from the email input fields, if they are empty
     * prompt an error message in a toast. Then check the credentials,
     * if it is valid login the user. While the authentication is
     * happening, a progress dialog is shown.
     */
    private void loginUser() {
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
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()){
                            //User successfully logged in
                            //Displaying message
                            Toast.makeText(LoginActivity.this, "You have successfully logged in!",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            // Start User activities --> MainActivity
                            startMainActivity();
                        }
                        else{
                            // Registration was not successful
                            Toast.makeText(LoginActivity.this, "Login failed!",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    /**
     * Close the Login activity and start the main Activity.
     */
    private void startMainActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Google sign in, calling Google's intent
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Get the result of the Google login.
     * @param requestCode the request.
     * @param resultCode the result of the request.
     * @param data returned result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    /**
     * Logging in with Google account.
     * @param acct Google account variable.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            startMainActivity();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this,"Google login failed !",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
