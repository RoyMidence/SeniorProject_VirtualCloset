package com.example.menu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FilterChosenOutfit extends AppCompatActivity implements TypeAdapter.itemClickInterface{

    RecyclerView recyclerViewFilteredTypes;
    List<String> listTypes = new ArrayList<>();
    private TypeAdapter listTypeAdapter;

    ActivityResultLauncher<Intent> otherActivityLauncher;

    RadioButton radioButtonCreateOutfit;
    EditText editTextFilterColor1, editTextFilterColor2;
    Button buttonAddTypes, buttonDone, buttonOccasion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_chosen_outfit);

        recyclerViewFilteredTypes = findViewById(R.id.recyclerViewFilteredTypes);
        radioButtonCreateOutfit = findViewById(R.id.radioButtonCreateOutfit);
        editTextFilterColor1 = findViewById(R.id.editTextFilterColor1);
        editTextFilterColor2 = findViewById(R.id.editTextFilterColor2);
        buttonOccasion = findViewById(R.id.buttonOccasion);
        buttonAddTypes = findViewById(R.id.buttonAddTypes);
        buttonDone = findViewById(R.id.buttonDone);

        //Pass in old filters
        setUpRecycler();
        editTextFilterColor1.setText(getIntent().getExtras().getString("c1"));
        editTextFilterColor2.setText(getIntent().getExtras().getString("c2"));
        buttonOccasion.setText(getIntent().getExtras().getString("Occasion"));



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

                // Values for things getting filtered
                ArrayList<String> newTypes = new ArrayList<>();
                String c1 = "Any";
                String c2 = "Any";

                // Get list of Types

                for (int i = 0; i < listTypes.size(); i++) {
                    newTypes.add(listTypes.get(i));
                }

                // Get Colors
                if (!editTextFilterColor1.equals(null))
                    c1 = editTextFilterColor1.getText().toString();

                if (!editTextFilterColor2.equals(null))
                    c2 = editTextFilterColor2.getText().toString();

                // pack them up
                resultIntent.putStringArrayListExtra("types",newTypes);
                resultIntent.putExtra("c1",c1);
                resultIntent.putExtra("c2",c2);
                resultIntent.putExtra("Occasion",buttonOccasion.getText());
                resultIntent.putExtra("createOutfit", radioButtonCreateOutfit.isChecked());
                setResult(0,resultIntent);
                finish(); // Come back later, when other screen done
            }
        });

        buttonOccasion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(FilterChosenOutfit.this, buttonOccasion);

                popupMenu.getMenu().add(0,0, Menu.NONE,"Formal");
                popupMenu.getMenu().add(0,1,Menu.NONE,"Casual");
                popupMenu.getMenu().add(0,2,Menu.NONE,"Any");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // DO SOMETHING
                        if (item.getItemId() == 0) {
                            buttonOccasion.setText("Formal");
                        } else if (item.getItemId() == 1){
                            buttonOccasion.setText("Casual");
                        } else {
                            buttonOccasion.setText("Any");
                        }
                        return false;
                    }
                });
                popupMenu.show();
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