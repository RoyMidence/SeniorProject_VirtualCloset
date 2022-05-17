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

public class SelectOutfitEvents extends AppCompatActivity implements  OutfitNameAdapter.itemClickInterface{
    private DatabaseHelper mDatabaseHelper;
    private ArrayList<String> outfit_name = new ArrayList<>();
    private ArrayList<String> outfitId = new ArrayList<>();
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_outfit_events);
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewClothing_outfit3);
        empty = findViewById(R.id.selectOutfitEmpty);
       storeValuesInArray();
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new OutfitNameAdapter(getApplicationContext(),outfit_name,outfitId,this));
    }
    private void storeValuesInArray(){
        outfit_name.clear();
        outfitId.clear();

        Cursor cursor = mDatabaseHelper.readUserOutfits();
        if (cursor.getCount() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                outfit_name.add(name);
                outfitId.add(id);
            }
            cursor.close();
        }
    }
    @Override
    public void onItemClick(ArrayList<String> names, int position) {
        Intent resultIntent = new Intent();


        String name = String.valueOf(outfit_name.get(position));
        String id = String.valueOf(outfitId.get(position));


        resultIntent.putExtra("name", name);
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("type","NONE");
        setResult(2, resultIntent);
        finish();
    }

}