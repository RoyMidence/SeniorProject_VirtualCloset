package com.example.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.menu.databinding.ActivityAddOutfitBinding;

public class AddOutfit extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddOutfitBinding binding;
    private EditText ed_name;
    private TextView shirt,pants,socks,shoes;
    private String type,brand,name,id_of_shirt,id_of_pants,id_of_socks,id_of_shoes;
    private DatabaseHelper mDatabaseHelper;
    ActivityResultLauncher<Intent> otherActivityLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddOutfitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ed_name = (EditText) findViewById(R.id.ed_outfit_name);
        shirt = (TextView)  findViewById(R.id.add_outfit_tv_shirt);
        pants = (TextView) findViewById(R.id.add_outfit_tv_pant);
        socks = (TextView) findViewById(R.id.add_outfit_tv_socks);
        shoes = (TextView) findViewById(R.id.add_outfit_tv_shoes);
        FloatingActionButton fab_add = (FloatingActionButton)findViewById(R.id.add_outfits_fab);
        configureButtons();
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
         otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 0) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                 name = resultIntent.getStringExtra("name");
                                 id_of_shirt = resultIntent.getStringExtra("id");
                                 shirt.setText(name);
                            }
                        }else if (result.getResultCode() == 1) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                name = resultIntent.getStringExtra("name");
                                id_of_pants = resultIntent.getStringExtra("id");
                                pants.setText(name);
                            }

                        }
                        else if (result.getResultCode() == 2) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                name = resultIntent.getStringExtra("name");
                                id_of_shoes = resultIntent.getStringExtra("id");
                                shoes.setText(name);
                            }

                        }
                        else if (result.getResultCode() == 3) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                name = resultIntent.getStringExtra("name");
                                id_of_socks = resultIntent.getStringExtra("id");
                                socks.setText(name);
                            }

                        }
                    }
                });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if (String.valueOf(ed_name.getText()).equals("")) {
               toastMessage("Please enter a name!");
               } else if (id_of_shirt == null || id_of_pants == null||
                      id_of_shoes == null){
                toastMessage("Please make a choice for all fields.");
             }else{
                  mDatabaseHelper.addOutfit(String.valueOf(ed_name.getText()));
                    if(id_of_socks != null){
                        mDatabaseHelper.addClothingToOutfit(id_of_socks);
                    }
                    mDatabaseHelper.addClothingToOutfit(id_of_shirt);
                    mDatabaseHelper.addClothingToOutfit(id_of_pants);
                    mDatabaseHelper.addClothingToOutfit(id_of_shoes);
                    finish();

               }



            }
        });

    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void configureButtons() {
        Button btn_shirt = (Button) findViewById(R.id.add_outfit_btn_shirt);
        Button btn_pants = (Button) findViewById(R.id.add_outfit_btn_pant);
        Button btn_socks = (Button) findViewById(R.id.add_outfit_btn_socks);
        Button btn_shoes = (Button) findViewById(R.id.add_outfit_btn_shoes);
        FloatingActionButton fab_back = (FloatingActionButton)findViewById(R.id.previous_screen_outfit);


        btn_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type","Shirt");
                    otherActivityLauncher.launch(intent);

                }
            }
        });
        btn_pants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type","Pants");
                    otherActivityLauncher.launch(intent);


                }
            }
        });
        btn_socks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type","Socks");
                    otherActivityLauncher.launch(intent);

                }
            }
        });btn_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type","Shoes");
                    otherActivityLauncher.launch(intent);


                }
            }
        });
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

    }
    private void toastMessage(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

}