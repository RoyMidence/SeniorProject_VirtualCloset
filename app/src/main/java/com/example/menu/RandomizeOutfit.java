package com.example.menu;

import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class RandomizeOutfit extends Fragment  implements ClothingAdapter.itemClickInterface{

 private View v;
 private RecyclerView recyclerViewRandomOutfit;
    private List<ClothingItem> clothingItems = new ArrayList<>();
    private DatabaseHelper mDatabaseHelper;
    private ClothingAdapter list;
    EditText outfitNameRandom;

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
         outfitNameRandom = (EditText) v.findViewById(R.id.randomOutfit);
        mDatabaseHelper = new DatabaseHelper(getContext());
        recyclerViewRandomOutfit = v.findViewById(R.id.recyclerViewRandom);
        configureButtons();
        setUpRecycler(v);
        return v;
    }

    private void setUpRecycler(View view) {
        storeValuesInArrays();

        recyclerViewRandomOutfit.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ClothingAdapter(getContext(), clothingItems, this);
        recyclerViewRandomOutfit.setAdapter(list);
    }
    private void storeValuesInArrays() {
        ClothingItem CI;
        clothingItems.clear();
       //list.notifyDataSetChanged();

        Cursor cursor = mDatabaseHelper.readUsersClothing();
        if (cursor.getCount() == 0) {

        } else {

            while (cursor.moveToNext()) {
                // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIR : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                        cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id));
                clothingItems.add(CI);
            }
        }


    }
    private void configureButtons() {
        Button randomButton = (Button) v.findViewById(R.id.randomizeButton);
        FloatingActionButton  filter = (FloatingActionButton)v.findViewById(R.id.filterFAB);
        ImageButton cancel = (ImageButton) v.findViewById(R.id.cancel);
        ImageButton save = (ImageButton) v.findViewById(R.id.save);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);


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