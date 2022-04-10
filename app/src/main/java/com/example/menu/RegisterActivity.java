package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void sendMessage(View view) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        EditText name = (EditText) findViewById(R.id.editTextFullName);
        EditText user = (EditText) findViewById(R.id.editTextUsername);
        EditText pass = (EditText) findViewById(R.id.editTextPassword);
        EditText hot = (EditText) findViewById(R.id.editHot);
        EditText freezing = (EditText) findViewById(R.id.editFreezing);
        EditText warm = (EditText) findViewById(R.id.editWarm);
        EditText cold = (EditText) findViewById(R.id.editCold);


        String fullname = name.getText().toString();
        String username = user.getText().toString();
        String password = pass.getText().toString();
        String hotTemp = hot.getText().toString();
        String freezingTemp = freezing.getText().toString();
        String warmTemp = warm.getText().toString();
        String coldTemp = cold.getText().toString();



        // public boolean addUser(String fullname, String userName, String password)
        db.addUser(fullname, username, password,hotTemp,freezingTemp,warmTemp,coldTemp);

        // public String checkLogin(String userName, String password)
        String result = db.checkLogin(username, password);

        // login the user
        db.logginUser(result);
        finish();

    }
    public void CheckTemp(){

    }
}