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
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class FilterChosenOutfit extends AppCompatActivity implements TypeAdapter.itemClickInterface{

    RecyclerView recyclerViewFilteredTypes;
    List<String> listTypes = new ArrayList<>();
    private TypeAdapter listTypeAdapter;

    ActivityResultLauncher<Intent> otherActivityLauncher;

    Button buttonAddTypes, buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_chosen_outfit);

        recyclerViewFilteredTypes = findViewById(R.id.recyclerViewFilteredTypes);
        buttonAddTypes = findViewById(R.id.buttonAddTypes);
        buttonDone = findViewById(R.id.buttonDone);
        setUpRecycler();



        otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 0) {
                        Intent resultIntent = result.getData();
                        if (resultIntent != null) {
                            ArrayList<String> newTypes = resultIntent.getStringArrayListExtra("types");
                            for (int i = 0; i < newTypes.size(); i++) {
                                if (!listTypes.contains(newTypes.get(i))) { // checks for duplicates
                                    listTypes.add(newTypes.get(i));
                                }
                            }
                            listTypeAdapter.notifyDataSetChanged(); // Should have any new types added, no repeats
                        }
                    }

                }
            });

        buttonAddTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterChosenOutfit.this,SelectTypes.class);
                otherActivityLauncher.launch(intent);
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();

                ArrayList<String> newTypes = new ArrayList<>();
                for (int i = 0; i < listTypes.size(); i++) {
                    newTypes.add(listTypes.get(i));
                }

                resultIntent.putStringArrayListExtra("types",newTypes);
                setResult(0,resultIntent);
                finish(); // Come back later, when other screen done
            }
        });

    }


    private void storeValuesInArrays() {
        listTypes.clear();

        ArrayList<String> newTypes = getIntent().getStringArrayListExtra("types");
        for (int i = 0; i < newTypes.size(); i++) {
            if (!listTypes.contains(newTypes.get(i))) { // checks for duplicates
                listTypes.add(newTypes.get(i));
            }
        }
    }

    private void setUpRecycler() {
        storeValuesInArrays();

        recyclerViewFilteredTypes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listTypeAdapter = new TypeAdapter(listTypes, this);
        recyclerViewFilteredTypes.setAdapter(listTypeAdapter);
    }

    @Override
    public void onItemClick(int position) {
        listTypes.remove(position);
        listTypeAdapter.notifyDataSetChanged();
    }
}