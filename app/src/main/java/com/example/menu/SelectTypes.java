package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class SelectTypes extends AppCompatActivity implements TypeAdapter.itemClickInterface{
    DatabaseHelper mDatabaseHelper;

    RecyclerView recyclerViewListTypes, recyclerViewAddedTypes;
    List<String> listTypes = new ArrayList<>();
    List<String> AddedTypes = new ArrayList<>();

    private TypeAdapter listTypeAdapter;
    private TypeAdapter addedTypes;

    Button buttonSaveTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_types);

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        buttonSaveTypes = findViewById(R.id.buttonSaveTypes);
        recyclerViewListTypes = findViewById(R.id.recyclerViewListTypes);
        recyclerViewAddedTypes = findViewById(R.id.recyclerViewAddedTypes);

        setUpRecycler();

        buttonSaveTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> sendBack = new ArrayList<>(); // Cant send back list
                for (int i = 0; i < AddedTypes.size(); i++) {
                    sendBack.add(AddedTypes.get(i));
                }
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("types",sendBack);
                setResult(0,resultIntent);
                finish();
            }
        });


    }

    private void storeValuesInArrays() {
        listTypes.clear();
        Cursor cursor = mDatabaseHelper.readUsersClothing();

        while (cursor.moveToNext()) {
            // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIR : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
            if(!listTypes.contains(cursor.getString(3))) {
                listTypes.add(cursor.getString(3));
            }
        }
    }

    private void setUpRecycler() {
        storeValuesInArrays();

        recyclerViewListTypes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listTypeAdapter = new TypeAdapter(listTypes, this);
        recyclerViewListTypes.setAdapter(listTypeAdapter);

        recyclerViewAddedTypes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addedTypes = new TypeAdapter(AddedTypes,this);
        recyclerViewAddedTypes.setAdapter(addedTypes);


    }

    @Override
    public void onItemClick(int position) {
        if (!AddedTypes.contains(listTypes.get(position))) {
            AddedTypes.add(listTypes.get(position));
            addedTypes.notifyDataSetChanged();
        }
    }
}
