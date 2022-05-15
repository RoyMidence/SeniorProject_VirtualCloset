package com.example.menu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddEvent extends AppCompatActivity implements OutfitNameAdapter.itemClickInterface{
    EditText title, location;
    TextView add, cancel, startDate, endDate, sRealDate, sEndDate;
    private DatabaseHelper mDatabaseHelper;
    private List<ClothingItem> clothingItems = new ArrayList<>();


    private ArrayList<String> outfitId = new ArrayList<>();
    private ArrayList<String> clothesId = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();

    ActivityResultLauncher<Intent> otherActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        title = (EditText) findViewById(R.id.editTextTitle);
        location = (EditText) findViewById(R.id.editTextLocation);
        startDate = findViewById(R.id.textViewStartDate);
        endDate = findViewById(R.id.textViewEndDate);
        sRealDate = findViewById(R.id.textViewRealDate);
        sEndDate = findViewById(R.id.textViewERealDate);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAddItems);
        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        configureClickables();

        otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 0) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {
                                String date = resultIntent.getStringExtra("date");
                                sRealDate.setText(date);

                            }
                        }
                        if (result.getResultCode() == 1) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {
                                String date = resultIntent.getStringExtra("date");
                                sEndDate.setText(date);

                            }
                        }
                        if (result.getResultCode() == 2) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {
                                names.add(resultIntent.getStringExtra("name"));
                                outfitId.add(resultIntent.getStringExtra("id"));
                                String type = resultIntent.getStringExtra("type");
                                RecyclerView.LayoutManager layoutManager =
                                        new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                for(int i =0; i<names.size();i++) {

                                }
                            }
                        }
                    }
                });
        recyclerView.setAdapter(new OutfitNameAdapter(getApplicationContext(),names, outfitId, this));

    }





    private void configureClickables(){
        add = (TextView) findViewById(R.id.createEvent);
        cancel = (TextView) findViewById(R.id.eventCancel);
        Button selectClothes = findViewById(R.id.buttonSelectClothes);
        Button selectOutfits = findViewById(R.id.buttonSelectOutfits);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(title.getText()).equals("")||String.valueOf(location.getText()).equals("") || String.valueOf(sRealDate.getText()).equals("MM-DD-YYYY")) {
                    toastMessage("Please enter a title and location.");
                }
                else {
                    mDatabaseHelper.addEvent(String.valueOf(title.getText()), String.valueOf(location.getText()), String.valueOf(sRealDate.getText()), String.valueOf(sEndDate.getText()));

                    Cursor cursor = mDatabaseHelper.readUserOutfits();
                    Cursor cursorClothes = mDatabaseHelper.readUsersClothing();
                    if (cursor.getCount() == 0) {

                    } else {
                        while (cursor.moveToNext()) {
                            String id = cursor.getString(0);
                            String name = cursor.getString(1);
                            for (int i = 0; i < names.size(); i++) {
                                if (id.equals(outfitId.get(i)) && name.equals(names.get(i))) {
                                    mDatabaseHelper.addOutfitToEvent(outfitId.get(i));
                                }
                            }

                        }
                    }
                    if (cursorClothes.getCount() == 0) {

                    } else {
                        while (cursorClothes.moveToNext()) {
                            String id = cursorClothes.getString(0);
                            String name = cursorClothes.getString(1);
                            for (int i = 0; i < names.size(); i++) {
                                if (id.equals(outfitId.get(i)) && name.equals(names.get(i))) {
                                    mDatabaseHelper.addClothingToEvent(outfitId.get(i));
                                }
                            }
                        }
                    }
                    Intent resultIntent = new Intent();
                    setResult(7, resultIntent);
                    finish();
                }
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(AddEvent.this,StartCalendar.class);
                otherActivityLauncher.launch(go);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(String.valueOf(sRealDate.getText()).equals("MM-DD-YYYY")){
                    toastMessage("Please enter a start date first");
                }
                else {
                    Intent go = new Intent(AddEvent.this, EndCalendar.class);
                    go.putExtra("date", String.valueOf(sRealDate.getText()));
                    otherActivityLauncher.launch(go);
                }
            }
        });
        selectClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(AddEvent.this, SelectClothesEvents.class);
                otherActivityLauncher.launch(go);
            }
        });
        selectOutfits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(AddEvent.this, SelectOutfitEvents.class);
                otherActivityLauncher.launch(go);
            }
        });
        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }));
    }

    @Override
    public void onItemClick(ArrayList<String> name, int position) {

    }

    private void toastMessage(String Message) {
        Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_SHORT).show();
    }
}
