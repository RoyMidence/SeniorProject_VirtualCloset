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

        String fullname = user.getText().toString();
        String username = user.getText().toString();
        String password = pass.getText().toString();

        // public boolean addUser(String fullname, String userName, String password)
        db.addUser(fullname, username, password);

        // public String checkLogin(String userName, String password)
        String result = db.checkLogin(username, password);

        // login the user
        db.logginUser(result);
        finish();

    }
}