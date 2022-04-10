package com.example.menu;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomizeOutfit extends Fragment  implements ClothingAdapter.itemClickInterface{

 private View v;
 private RecyclerView recyclerViewRandomOutfit;
    private List<ClothingItem> clothingItems = new ArrayList<>();

    private DatabaseHelper mDatabaseHelper;
    private ClothingAdapter list;
    EditText outfitNameRandom;
    ActivityResultLauncher<Intent> otherActivityLauncher;

    // Globals for filter parameters
    private ArrayList<String> types = new ArrayList<>();
    private String c1, c2;
    private boolean createOutfit;

    public RandomizeOutfit() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_randomize_outfit, container, false);

        // Set Basic Default Filter Parameters
        types.add("Shirt");
        types.add("Pants");
        types.add("Shoes");
        c1 = "Any";
        c2 = "Any";
        createOutfit = true;


        outfitNameRandom = (EditText) v.findViewById(R.id.randomOutfit);
        mDatabaseHelper = new DatabaseHelper(getContext());
        recyclerViewRandomOutfit = v.findViewById(R.id.recyclerViewRandom);
        configureButtons();
        setUpRecycler(v);

        otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 0) {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                types. clear();
                                types = resultIntent.getStringArrayListExtra("types");
                                c1 = resultIntent.getExtras().getString("c1");
                                c2 = resultIntent.getExtras().getString("c2");
                                createOutfit = resultIntent.getExtras().getBoolean("createOutfit");

                            }
                        }

                    }
                });


        return v;
    }

    private void setUpRecycler(View view) {
        recyclerViewRandomOutfit.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ClothingAdapter(getContext(), clothingItems, this);
        recyclerViewRandomOutfit.setAdapter(list);
    }

    private boolean chooseClothingItem(String type) {
        ClothingItem CI;
        List<ClothingItem> temp = new ArrayList<>(); // will hold all of a specific type


        Cursor cursor = mDatabaseHelper.readClothingType(type); // table of specified type
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIR : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id));
                temp.add(CI);
            }
        } // Now have a list with the specified types

        // Shorten list down to colors
        // Only doing for shirts for now
        if (type.equals("Shirt")) {
            for (int i = 0; i < temp.size(); i++) {
                if (!c1.equals("Any") && !c2.equals("Any")) { // Both colors, looking for specified combo
                    if (!temp.get(i).getColor1().equals(c1) || !temp.get(i).getColor2().equals(c2)) {
                        temp.remove(i);
                        i--;
                    }
                } else if (!c1.equals("Any")) { // Looking for primary color
                    if (!temp.get(i).getColor1().equals(c1)) {
                        temp.remove(i);
                        i--;
                    }
                } else if (!c2.equals("Any")) { // Looking for secondary color
                    if (!temp.get(i).getColor2().equals(c2)) {
                        temp.remove(i);
                        i--;
                    }
                } // No default. if there is no color filter then no need for work
            }
        } // Broke down list of shirts, should only be left with shirts of the correct color
        // But also might not have shirts at all, need to account for this

        if (temp.size() == 0) { // If nothing with that color exists
            return false;
        }

        Random random = new Random(); // use this to generate a random number
        clothingItems.add(temp.get(random.nextInt(temp.size()))); // adding a random clothing Item here
        return true;
    }

    private void chooseOutfit() {
        clothingItems.clear();
        ClothingItem CI;
        List<Outfit> of = new ArrayList<>();

        // Create Outfit Object
        // Create list of them
        // select one from list


        // Creating list of outfits
        Cursor cursor = mDatabaseHelper.readUserOutfits();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                // OUTFIT TABLE ID : NAME
                of.add(new Outfit(cursor.getString(0), cursor.getString(1)));
            }
        } // Now have a list with the outfits

        // SELECT OUTFIT
        Random random = new Random(); // use this to generate a random number
        int outfit = random.nextInt(of.size());
        String outfitID = of.get(outfit).getOutfitID();
        cursor = mDatabaseHelper.readOutfitClothing(outfitID); // reading the clothing of randomly selected outfit
        while (cursor.moveToNext()) {
            // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIT : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
            String id = cursor.getString(0);
            CI = new ClothingItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                    cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),
                    mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id));
            clothingItems.add(CI);
        }

        outfitNameRandom.setText(of.get(outfit).getOutfitName());
        // LIST CLOTHING
    }

    private void configureButtons() {
        Button randomButton = (Button) v.findViewById(R.id.randomizeButton);
        FloatingActionButton  filter = (FloatingActionButton)v.findViewById(R.id.filterFAB);
        ImageButton cancel = (ImageButton) v.findViewById(R.id.cancel);
        ImageButton save = (ImageButton) v.findViewById(R.id.save);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean worked = true;
                clothingItems.clear(); // new outfit everytime


                if (createOutfit) {
                    for (int i = 0; i < types.size(); i++) { // Will cycle through the types of clothing
                        if (!chooseClothingItem(types.get(i))) {
                            Toast.makeText(getContext(), "No shirt with that color", Toast.LENGTH_SHORT).show();
                            worked = false;
                            break;
                        }
                    }
                } else {
                    chooseOutfit();
                }

                if (worked) {
                    list.notifyDataSetChanged();
                    cancel.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                }


            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FilterChosenOutfit.class);
                intent.putStringArrayListExtra("types",types);
                intent.putExtra("c1",c1);
                intent.putExtra("c2",c2);
                otherActivityLauncher.launch(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clothingItems.clear();
                list.notifyDataSetChanged();
                cancel.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                outfitNameRandom.setText("");

            }
        });
    }
    @Override
    public void onItemClick(int position) {

    }
}