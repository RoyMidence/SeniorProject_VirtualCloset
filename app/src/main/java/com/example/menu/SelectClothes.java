package com.example.menu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
//kieran from the mac 2
public class SelectClothes extends AppCompatActivity implements NameAdapter.itemClickInterface {
    private DatabaseHelper mDatabaseHelper;


    private ImageView emptyImageView;
    private TextView textViewEmptyCloset;
    private String type;

    private List<ClothingItem> clothingItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_clothes);
        emptyImageView = (ImageView) findViewById(R.id.emptyImageView_Outfit);
        textViewEmptyCloset = findViewById(R.id.textViewEmpty_select);
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewClothing_outfit);
        type = getIntent().getExtras().getString("type");
        storeValuesInArrays();
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new NameAdapter(clothingItems,this));
    }
    private void storeValuesInArrays() {
        ClothingItem CI;
        Cursor cursor = mDatabaseHelper.readUsersClothing();

            if (cursor.getCount() == 0) {

            } else {
                emptyImageView.setVisibility(View.GONE);
                textViewEmptyCloset.setVisibility(View.GONE);

                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    if(mDatabaseHelper.getClothingType(id).equals(type)) {
                        CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),cursor.getString(13),
                                mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                        clothingItems.add(CI);
                    } else if(type.equals("all")) {
                        CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),cursor.getString(13),
                                mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                        clothingItems.add(CI);
                    }



                }
            }
            cursor.close();
        if(clothingItems.size() ==0) {
            emptyImageView.setVisibility(View.VISIBLE);
            textViewEmptyCloset.setVisibility(View.VISIBLE);
        }
        }

    @Override
    public void onItemClick(int position) {

        Intent resultIntent = new Intent();

        // Make sure you grabbed any data you need (ie. clothing info)
        String name = String.valueOf(clothingItems.get(position).getName());
        String id= String.valueOf(clothingItems.get(position).getClothingID());
        // pack the data and give it some key like "shirtName" or something
        // This might take you multiple lines


        // after you're done and have all the info you need
        // set the code for the info and the info to go with it
        if(type.equals("T-Shirt")||type.equals("Long Sleeve Shirt")|| type.equals("Button Down Shirt")){
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("id", id);
            setResult(0,resultIntent);
            finish();
        }else if (type.equals("Pants")) {
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("id", id);
            setResult(1, resultIntent);
            finish();
        }
        else if (type.equals("Shoes")|| type.equals("Sandals")){
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("id", id);
            setResult(2, resultIntent);
            finish();
        }
        else if (type.equals("Socks")){
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("id", id);
            setResult(3, resultIntent);
            finish();

        } else if(type.equals("Hat")|| type.equals("Gloves")||type.equals("Scarf") ||type.equals("Light Jacket") ||type.equals("Heavy Jacket")){


            resultIntent.putExtra("name", name);
            resultIntent.putExtra("id", id);
            setResult(4,resultIntent);
            finish();

        }
        else {
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("id", id);
            setResult(0,resultIntent);
            finish();
        }
        // use this to end the activity. Important for memory purposes

    }
}