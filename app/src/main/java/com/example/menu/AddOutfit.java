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
import androidx.appcompat.widget.PopupMenu;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddOutfit extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private EditText ed_name;
    private TextView shirt,pants,socks,shoes,acc;
    private String name,id_of_shirt,id_of_pants,id_of_socks,id_of_shoes,id_of_acc,name_of_acc;
    private DatabaseHelper mDatabaseHelper;
    private ArrayList<String> namelist;
    private ArrayList<String> idlist;
    private ArrayList<String> acc_id = new ArrayList<>();
    ActivityResultLauncher<Intent> otherActivityLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_outfit);

        ed_name = (EditText) findViewById(R.id.ed_outfit_name);
        shirt = (TextView)  findViewById(R.id.add_outfit_tv_shirt);
        pants = (TextView) findViewById(R.id.add_outfit_tv_pant);
        socks = (TextView) findViewById(R.id.add_outfit_tv_socks);
        shoes = (TextView) findViewById(R.id.add_outfit_tv_shoes);
        acc = findViewById(R.id.add_outfit_tv_acc);
        namelist = getIntent().getExtras().getStringArrayList("namelist");
        idlist = getIntent().getExtras().getStringArrayList("idlist");



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
                        } else if (result.getResultCode() == 1) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                name = resultIntent.getStringExtra("name");
                                id_of_pants = resultIntent.getStringExtra("id");
                                pants.setText(name);
                            }

                        } else if (result.getResultCode() == 2) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                name = resultIntent.getStringExtra("name");
                                id_of_shoes = resultIntent.getStringExtra("id");
                                shoes.setText(name);
                            }

                        } else if (result.getResultCode() == 3) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                name = resultIntent.getStringExtra("name");
                                id_of_socks = resultIntent.getStringExtra("id");
                                socks.setText(name);
                            }

                        } else if (result.getResultCode() == 4) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {  // If it isn't empty data
                                // Whatever you want to do with data goes here
                                // The line below is getting whatever data is sent back
                                // works like bundles, "mydata" is just some key we set in the next part
                                if (name_of_acc != null) {
                                    id_of_acc = resultIntent.getStringExtra("id");
                                    if(acc_id.contains(id_of_acc)){

                                    }else{

                                        name_of_acc += "/ " + resultIntent.getStringExtra("name");
                                        acc_id.add(id_of_acc);
                                    }
                                } else {
                                    name_of_acc = resultIntent.getStringExtra("name");
                                    id_of_acc = resultIntent.getStringExtra("id");
                                    acc_id.add(id_of_acc);
                                }


                                acc.setText(name_of_acc);
                            }
                        }
                    }
                });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

                if (String.valueOf(ed_name.getText()).equals("")) {
               toastMessage("Please enter a name!");
               } else if (id_of_shirt == null || id_of_pants == null||
                      id_of_shoes == null){
                toastMessage("Please make a choice for all fields.");
             }else{
                  mDatabaseHelper.addOutfit(String.valueOf(ed_name.getText()),currentDate);
                    if(id_of_socks != null){
                        mDatabaseHelper.addClothingToOutfit(id_of_socks);
                    }
                 if(name_of_acc != null){

                     for(int i = 0; i < acc_id.size(); i++){

                         mDatabaseHelper.addClothingToOutfit(acc_id.get(i));
                         System.out.println(acc_id.get(i));
                     }

                 }
                    mDatabaseHelper.addClothingToOutfit(id_of_shirt);
                    mDatabaseHelper.addClothingToOutfit(id_of_pants);
                    mDatabaseHelper.addClothingToOutfit(id_of_shoes);

                    namelist.add(String.valueOf(ed_name.getText()));
                    idlist.add(String.valueOf(mDatabaseHelper.getLatestOutfit()));

                    Intent resultIntent = new Intent();
                    resultIntent.putStringArrayListExtra("namelist",namelist);
                    resultIntent.putStringArrayListExtra("idlist",idlist);
                    setResult(0,resultIntent);
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
        Button btn_acc = findViewById(R.id.add_outfit_btn_acc);


        btn_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AddOutfit.this, v);
                popup.setOnMenuItemClickListener(AddOutfit.this);
                popup.inflate(R.menu.shirt_menu);
                popup.show();

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
                PopupMenu popup = new PopupMenu(AddOutfit.this, v);
                popup.setOnMenuItemClickListener(AddOutfit.this);
                popup.inflate(R.menu.shoe_type_menu);
                popup.show();




            }
        });
        btn_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AddOutfit.this, v);
                popup.setOnMenuItemClickListener(AddOutfit.this);
                popup.inflate(R.menu.accesory_menu);
                popup.show();

            }
        });


    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tshirt:

                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Shirt");
                    otherActivityLauncher.launch(intent);

                }

                break;
            case R.id.lsshirt:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Long Sleeve Shirt");
                    otherActivityLauncher.launch(intent);

                }
                break;
            case R.id.bdshirt:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Button Down Shirt");
                    otherActivityLauncher.launch(intent);

                }
                break;
            case R.id.hat:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Hat");
                    otherActivityLauncher.launch(intent);

                }
                break;
            case R.id.gloves:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Gloves");
                    otherActivityLauncher.launch(intent);

                }
                break;
            case R.id.scarf:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Scarf");
                    otherActivityLauncher.launch(intent);

                }
                break;
            case R.id.Sandals:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Sandals");
                    otherActivityLauncher.launch(intent);
                }
                break;
            case R.id.Shoes:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Shoes");
                    otherActivityLauncher.launch(intent);
                }
            case R.id.lightJacket:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Light Jacket");
                    otherActivityLauncher.launch(intent);
                }
                break;
            case R.id.heavyJacket:
                if (String.valueOf(ed_name.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    Intent intent = new Intent(AddOutfit.this, SelectClothes.class);
                    intent.putExtra("type", "Heavy Jacket");
                    otherActivityLauncher.launch(intent);
                }
        }
                return false;
        }

    private void toastMessage(String Message) {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

}