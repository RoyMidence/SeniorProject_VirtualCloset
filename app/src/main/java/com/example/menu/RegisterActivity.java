package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
EditText name,user,pass,hot,freezing,warm,cold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         name = (EditText) findViewById(R.id.editTextFullName);
        user = (EditText) findViewById(R.id.editTextUsername);
        pass = (EditText) findViewById(R.id.editTextPassword);
        hot = (EditText) findViewById(R.id.editHot);
        freezing = (EditText) findViewById(R.id.editFreezing);
       warm = (EditText) findViewById(R.id.editWarm);
         cold = (EditText) findViewById(R.id.editCold);




    }

    public void sendMessage(View view) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());



        String fullname = name.getText().toString();
        String username = user.getText().toString();
        String password = pass.getText().toString();
        String hotTemp = hot.getText().toString();
        String freezingTemp = freezing.getText().toString();
        String warmTemp = warm.getText().toString();
        String coldTemp = cold.getText().toString();
        //check to see if there are values.
        if (String.valueOf(hot.getText()).equals("")) {
            toastMessage("Please enter a Hot Temperature!");
        } else if (String.valueOf(freezing.getText()).equals("")) {
            toastMessage("Please enter a Freezing Temperature!");
        } else if (String.valueOf(cold.getText()).equals("")) {
            toastMessage("Please enter a Cold Temperature!");
        } else if (String.valueOf(warm.getText()).equals("")) {
            toastMessage("Please enter a Warm Temperature!");
        }else {
// once all values have been entered then it gets checked to see if its in a reasonable range
            if (Integer.parseInt(hotTemp) > 100 || Integer.parseInt(hotTemp) <= 80) {
                hot.setText("0");
                toastMessage("Hot Temperature between 99 and 80");
            } else if (Integer.parseInt(warmTemp) > 80 || Integer.parseInt(warmTemp) <= 60) {
                warm.setText("0");
                toastMessage("Warm Temperature between 79 and 60");
            } else if (Integer.parseInt(coldTemp) > 60 || Integer.parseInt(coldTemp) <= 40) {
                cold.setText("0");
                toastMessage("Cold Temperature between 59 and 40");
            } else if (Integer.parseInt(freezingTemp) > 40 || Integer.parseInt(freezingTemp) <= 10) {
                freezing.setText("0");
                toastMessage("Freezing Temperature between 39 and 10");
            } else {


                // public boolean addUser(String fullname, String userName, String password)
                db.addUser(fullname, username, password, hotTemp, freezingTemp, warmTemp, coldTemp);

                // public String checkLogin(String userName, String password)
                String result = db.checkLogin(username, password);

                // login the user
                db.logginUser(result);
                finish();
            }
        }

    }
        private void toastMessage(String Message) {
            Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
        }
}