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

public class ForgotPassword extends Dialog implements View.OnClickListener {
    private static final String TAG = "ForgotPassword";
    // View Components
    private Button backToLoginButton;
    private Button resetPasswordButton;
    private EditText emailInput;
    private TextView errorTextView;

    ForgotPassword(Context context) {
        super (context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Change Password");
        setContentView(R.layout.forgot_password);

        errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setText(" ");
        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(this);
        backToLoginButton = findViewById(R.id.backToLoginButton);
        backToLoginButton.setOnClickListener(this);
    }

    private void resetPassword(){
        String email = emailInput.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            errorTextView.setTextColor(Color.RED);
            errorTextView.setText(R.string.enter_an_email_address);
            return;
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            errorTextView.setTextColor(Color.BLUE);
                            errorTextView.setText(R.string.email_sent);
                        } else {
                            errorTextView.setTextColor(Color.RED);
                            errorTextView.setText(R.string.password_reset_failed);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if ( view == resetPasswordButton) {
            // change password
            resetPassword();
        }

        if ( view == backToLoginButton ) {
            // back to edit page
            dismiss();
        }
    }
}
