package com.example.menu;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class UserPreferences extends Fragment {

    private View v;
    private DatabaseHelper mDatabaseHelper;
    ActivityResultLauncher<Intent> otherActivityLauncher;
    private boolean isReveal;
    public UserPreferences() {
        // Required empty public constructor
    }
TextView txtFullName, txtUserName, txtPassword, txtHot, txtwarm, txtcold,txtfreezing;

    EditText edFullName,edUserName,edPassword, edhot,edwarm,edcold,edfreezing;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_preferences, container, false);
        mDatabaseHelper = new DatabaseHelper(getContext());
        txtFullName = v.findViewById(R.id.textViewFullNamePref);
        txtUserName = v.findViewById(R.id.textViewUserName);
        txtPassword = v.findViewById(R.id.textViewPassword);
        txtHot = v.findViewById(R.id.textViewHotPref);
        txtcold = v.findViewById(R.id.textViewColdPref);
        txtwarm = v.findViewById(R.id.textViewWarmPref);
        txtfreezing = v.findViewById(R.id.textViewFreezingPref);

        edFullName = v.findViewById(R.id.editTextFullNamePref);
        edUserName = v.findViewById(R.id.editTextUsernamePreferences);
        edPassword = v.findViewById(R.id.editTextPasswordPreferences);
        edhot = v.findViewById(R.id.editTextHotPref);
        edwarm = v.findViewById(R.id.editTextWarmPref);
        edcold = v.findViewById(R.id.editTextColdPref);
        edfreezing = v.findViewById(R.id.editTextFreezingPref);

        edFullName.setText(mDatabaseHelper.getUserName());
        edUserName.setText(mDatabaseHelper.getUserUserName());
        edPassword.setText(mDatabaseHelper.getUserPassword());
        edhot.setText(mDatabaseHelper.getUserHotTemp());
        edwarm.setText(mDatabaseHelper.getUserWarmTemp());
        edcold.setText(mDatabaseHelper.getUserColdTemp());
        edfreezing.setText(mDatabaseHelper.getUserFreezingTemp());

        isReveal = true;
        edFullName.setEnabled(false);
        edUserName.setEnabled(false);
        edPassword.setEnabled(false);


        setUpChangePassword();


        return v;
    }
    public void setUpChangePassword(){

        TextView changePassword = v.findViewById(R.id.textViewChangePassword);
        ImageButton revealPassword = v.findViewById(R.id.revealPassword);
        Button saveTemp = v.findViewById(R.id.saveTemperature);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangePassword.class);
                startActivity(i);

            }
        });
        revealPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(isReveal){
                   edPassword.setTransformationMethod(null);
                   revealPassword.setImageResource(R.drawable.ic_key_hide);
                   isReveal = false;
               }else {
                   edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                   revealPassword.setImageResource(R.drawable.ic_key_reveal);
                   isReveal= true;
               }
            }
        });
        saveTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hot, warm, freezing, cold;
                hot = String.valueOf(edhot.getText());
                cold = String.valueOf(edcold.getText());
                warm = String.valueOf(edwarm.getText());
                freezing = String.valueOf(edfreezing.getText());

                if (String.valueOf(edhot.getText()).equals("")) {
                    toastMessage("Please enter a Hot Temperature!");
                } else if (String.valueOf(edfreezing.getText()).equals("")) {
                    toastMessage("Please enter a Freezing Temperature!");
                } else if (String.valueOf(edcold.getText()).equals("")) {
                    toastMessage("Please enter a Cold Temperature!");
                } else if (String.valueOf(edwarm.getText()).equals("")) {
                    toastMessage("Please enter a Warm Temperature!");
                } else {
                    // once all values have been entered then it gets checked to see if its in a reasonable range
                    if (Integer.parseInt(hot) > 100 || Integer.parseInt(hot) <= 80) {
                        edhot.setText("0");
                        toastMessage("Hot Temperature between 99 and 80");
                    } else if (Integer.parseInt(warm) > 80 || Integer.parseInt(warm) <= 60) {
                        edwarm.setText("0");
                        toastMessage("Warm Temperature between 79 and 60");
                    } else if (Integer.parseInt(cold) > 60 || Integer.parseInt(cold) <= 40) {
                        edcold.setText("0");
                        toastMessage("Cold Temperature between 59 and 40");
                    } else if (Integer.parseInt(freezing) > 40 || Integer.parseInt(freezing) <= 10) {
                        edfreezing.setText("0");
                        toastMessage("Freezing Temperature between 39 and 10");
                    } else {

                        mDatabaseHelper.updateAllUserTemp(hot, warm, cold, freezing);
                        edhot.setText(mDatabaseHelper.getUserHotTemp());
                        edwarm.setText(mDatabaseHelper.getUserWarmTemp());
                        edcold.setText(mDatabaseHelper.getUserColdTemp());
                        edfreezing.setText(mDatabaseHelper.getUserFreezingTemp());
                    }
                }
            }
        });
    }

    private void toastMessage(String Message) {
        Toast.makeText(getContext(), Message, Toast.LENGTH_SHORT).show();
    }
}

