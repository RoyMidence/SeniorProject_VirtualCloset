package com.example.menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {
    private EditText newPassword,confirmPassword;
    private TextView errorPassword;
    private DatabaseHelper mDatabaseHelper;
    private boolean isNewReveal, isConfirmReveal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    //standard setup, connect the design.
        newPassword = findViewById(R.id.newPassword);
        confirmPassword=findViewById(R.id.confirmPassword);
        errorPassword=findViewById(R.id.errorPassword);

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
    isNewReveal= true;
    isConfirmReveal= true;
        configureButton();

    }

    private void configureButton(){
        Button savePassword = findViewById(R.id.savePassword);
        ImageButton revealNewPassword = findViewById(R.id.revealNewPassword);
        ImageButton revealConfirmPassword = findViewById(R.id.revealConfirmPassword);

        // save button action on click
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //making a string just to hold the new password
                String thePassword = String.valueOf(newPassword.getText());

                // test to see if new password and confirm password are empty
                if (String.valueOf(newPassword.getText()).equals("") && (String.valueOf(confirmPassword.getText()).equals(""))) {

                    toastMessage("Make a new password and confirm it.");

                }
                // test to see if only new password is empty
                else if(String.valueOf(newPassword.getText()).equals("")){

                    toastMessage("please enter a password.");

                }
                // test to see if only confirm password is empty
                else if(String.valueOf(confirmPassword.getText()).equals("")){

                    toastMessage("please confirm the password.");

                }
                else {
                    // now when both edit texts are filled check to see if they equal
                    if(String.valueOf(newPassword.getText()).equals(String.valueOf(confirmPassword.getText()))){

                        // if they do equal then make the update change to the database and end the activity

                        mDatabaseHelper.updateUserPassword(thePassword);
                        toastMessage("Your password has been changed");

                        finish();
                    }else{
                        // if they dont match then show the error message
                        errorPassword.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
            // setting up the reveal buttons
        revealNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNewReveal){
                    newPassword.setTransformationMethod(null);
                    revealNewPassword.setImageResource(R.drawable.ic_key_hide);
                    isNewReveal = false;
                }else {
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    revealNewPassword.setImageResource(R.drawable.ic_key_reveal);
                    isNewReveal= true;
                }

            }
        });
        revealConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmReveal) {
                    confirmPassword.setTransformationMethod(null);
                    revealConfirmPassword.setImageResource(R.drawable.ic_key_hide);
                    isConfirmReveal = false;
                } else {
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    revealConfirmPassword.setImageResource(R.drawable.ic_key_reveal);
                    isConfirmReveal = true;

                }
            }
        });

    }
    private void toastMessage(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }
}