package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class UpdateOutfit extends AppCompatActivity implements ClothingAdapter.itemClickInterface {
    String outfitID;

    private EditText editTextOutfitName;
    private Button buttonAddToOutfit;

    private ArrayList<String> list;

    private RecyclerView recyclerViewUpdateClothing;
    private ClothingAdapter clothingAdapter;
    private List<ClothingItem> outfitClothing;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_outfit);

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        editTextOutfitName = findViewById(R.id.editTextOutfitName);
        buttonAddToOutfit = findViewById(R.id.buttonAddToOutfit);


        outfitID = getIntent().getExtras().getString("outfitID");
        list = getIntent().getExtras().getStringArrayList("list");


        setUpRecycler();

    }

    private void setUpRecycler() {
        recyclerViewUpdateClothing = findViewById(R.id.recyclerViewUpdateClothing);
        storeValuesInArrays();

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerViewUpdateClothing.setLayoutManager(layoutManager);
        clothingAdapter = new ClothingAdapter(outfitClothing, this);
        recyclerViewUpdateClothing.setAdapter(clothingAdapter);
    }

    private void storeValuesInArrays() {
        ClothingItem CI;
        outfitClothing.clear();


        Cursor cursor = mDatabaseHelper.readOutfitClothing(outfitID);
        while (cursor.moveToNext()) {
            // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIR : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
            String id = cursor.getString(0);
            CI = new ClothingItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                    cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),
                    mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id));
            outfitClothing.add(CI);
        }
    }

    @Override
    public void onItemClick(int position) {

    }


}