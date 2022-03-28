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

        if (!result.equals("failed")) {
            db.logginUser(result);
            finish();
        } else {
            Toast.makeText(this, "Username or password does not exist", Toast.LENGTH_SHORT).show();
        }
    }
}

