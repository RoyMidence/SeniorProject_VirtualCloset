package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getApplicationContext());

        if (mDatabaseHelper.userTableEmpty()) {
            mDatabaseHelper.addUser("The Admin", "admin", "admin", "90", "38", "62");
        }

    }
    @Override
    public void onBackPressed () {

    }



    public void register(View view){
      Intent go = new Intent(LoginActivity.this,RegisterActivity.class);
      startActivity(go);
     finish();
    }

    public void login(View view) {
        EditText user = (EditText) findViewById(R.id.editTextUsername);
        EditText pass = (EditText) findViewById(R.id.editTextPassword);
        String username = user.getText().toString();
        String password = pass.getText().toString();

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        String result = db.checkLogin(username, password);

        if (result != null) {
            db.logginUser(result);
            finish();
        }else {
            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Username or password does not exist", Toast.LENGTH_SHORT).show());
        }
    }
}

