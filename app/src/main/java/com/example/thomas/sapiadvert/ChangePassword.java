package com.example.thomas.sapiadvert;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
        String newPW = newPassword.getText().toString().trim();
        String confirmPW = confirmNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(oldPW)){
            passwordErrorTextView.setText("Please enter old password");
            return;
        }
        if (TextUtils.isEmpty(newPW)){
            passwordErrorTextView.setText("Please enter new password");
            return;
        }
        if (TextUtils.isEmpty(confirmPW)){
            passwordErrorTextView.setText("Please confirm password");
            return;
        }
        passwordErrorTextView.setText(" ");
        // check password

        // change password

        // back to edit page
        dismiss();
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
