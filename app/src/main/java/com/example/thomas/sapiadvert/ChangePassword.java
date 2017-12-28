package com.example.thomas.sapiadvert;

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
 * Created by Thomas on 2017-12-28.
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

    public ChangePassword(Context context) {
        super (context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Change Password");
        setContentView(R.layout.change_password);

        passwordErrorTextView = (TextView) findViewById(R.id.passwordErrorTextView);
        oldPassword = (EditText) findViewById(R.id.oldPasswordInput);
        newPassword = (EditText) findViewById(R.id.newPasswordInput);
        confirmNewPassword = (EditText) findViewById(R.id.confirmNewPasswordInput);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(this);
        backToEditButton = (Button) findViewById(R.id.backToEditButton);
        backToEditButton.setOnClickListener(this);
        passwordErrorTextView.setText(" ");
    }

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
