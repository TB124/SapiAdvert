package com.example.thomas.sapiadvert;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This is a dialog view, that will pop up, when we want to change our password.
 * The view has three input fields (old password, new password and confirm password)
 * and two buttons (cancel and change).
 * The dialog will be displayed if the show() method of the class is called.
 * @author Bondor Tamas
 * @author Kovacs Szabolcs
 */
public class ChangePassword extends Dialog implements View.OnClickListener {
    private static final String TAG = "ChangePassword";
    // View Components
    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Button changePasswordButton;
    private Button backToEditButton;
    private TextView passwordErrorTextView;

    /**
     * Class constructor
     * @param context The context where he dialog is created.
     */
    public ChangePassword(Context context) {
        super (context);
    }

    /**
     * Setting up the view and linking the view components.
     * @param savedInstanceState Saved instances.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Change Password");
        setContentView(R.layout.change_password);

        passwordErrorTextView = findViewById(R.id.passwordErrorTextView);
        oldPassword = findViewById(R.id.oldPasswordInput);
        newPassword = findViewById(R.id.newPasswordInput);
        confirmNewPassword = findViewById(R.id.confirmNewPasswordInput);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(this);
        backToEditButton = findViewById(R.id.backToEditButton);
        backToEditButton.setOnClickListener(this);
        passwordErrorTextView.setText(" ");
    }

    /**
     * Get the strings from the input fields, check if they are correct
     * and display a message if any errors occurred.
     * Create a Credential with the old password and the users email address,
     * check if it is valid, if any error occurs the password was incorrect
     * so display a message, otherwise change the password.
     */
    @SuppressLint("SetTextI18n")
    private void changePassword(){
        String oldPW = oldPassword.getText().toString().trim();
        final String newPW = newPassword.getText().toString().trim();
        String confirmPW = confirmNewPassword.getText().toString().trim();
        passwordErrorTextView.setTextColor(Color.RED);
        if (TextUtils.isEmpty(oldPW)){
            passwordErrorTextView.setText("Please enter old password");
            return;
        }
        if (TextUtils.isEmpty(newPW)){
            passwordErrorTextView.setText("Please enter new password");
            return;
        }
        if (!confirmPW.equals(newPW)){
            passwordErrorTextView.setText("Passwords are not the same");
            return;
        }
        passwordErrorTextView.setText(" ");
        // check password and change it
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPW);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPW).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        passwordErrorTextView.setTextColor(Color.BLUE);
                                        passwordErrorTextView.setText("Password updated");
                                    }
                                }
                            });
                        } else {
                            passwordErrorTextView.setText("Old password is not correct");
                        }
                    }
                });
        // back to edit page
    }
    /**
     * OnClick listeners for the two buttons.
     * @param view the vew which was clicked.
     */
    @Override
    public void onClick(View view) {
        if ( view == changePasswordButton) {
            // change password
            changePassword();
        }

        if ( view == backToEditButton ) {
            // back to edit page
            dismiss();
        }
    }
}
