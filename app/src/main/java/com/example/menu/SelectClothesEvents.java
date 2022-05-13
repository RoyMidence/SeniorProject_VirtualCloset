package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectClothesEvents extends AppCompatActivity implements  NameAdapter.itemClickInterface{
    private DatabaseHelper mDatabaseHelper;
    private List<ClothingItem> clothingItems = new ArrayList<>();
    TextView empty;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_clothes_events);
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewClothing_outfit2);
        empty = findViewById(R.id.selectClothesEmpty);
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
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                    CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                            cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),cursor.getString(13),
                            mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                    clothingItems.add(CI);


            }
        }
    }
    @Override
    public void onItemClick(int position) {

        Intent resultIntent = new Intent();


        String name = String.valueOf(clothingItems.get(position).getName());
        String id = String.valueOf(clothingItems.get(position).getClothingID());
        String type = String.valueOf(clothingItems.get(position).getType());


        resultIntent.putExtra("name", name);
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("type", type);
        setResult(2, resultIntent);
        finish();
    }
}