package com.example.menu;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TheClothesFragment extends Fragment implements ClothingAdapter.itemClickInterface {
    private DatabaseHelper mDatabaseHelper;

    private ImageView emptyImageView;
    private TextView textViewEmptyCloset;
    private SearchView searchViewClothing;
    private FloatingActionButton floatingActionButtonSharedClosets;
    private PopupMenu popupMenu;
    RecyclerView recyclerView;
    private List<ClothingItem> clothingItems = new ArrayList<>();
    private List<String> sharedUserNames = new ArrayList<>();
    private List<String> sharedUserID = new ArrayList<>();
    private ClothingAdapter list;
    private View view;
    String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_thecloset,container,false);

        mDatabaseHelper = new DatabaseHelper(getContext());
        emptyImageView = view.findViewById(R.id.emptyImageView);
        textViewEmptyCloset = view.findViewById(R.id.textViewEmptyCloset);
        searchViewClothing = view.findViewById(R.id.searchViewClothing);
        floatingActionButtonSharedClosets = view.findViewById(R.id.floatingActionButtonSharedClosets);
        recyclerView = view.findViewById(R.id.recyclerViewClothing);
        fillDB();
        setUpRecycler(view);
        checkAndSetShared();

        searchViewClothing.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

        floatingActionButtonSharedClosets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu = new PopupMenu(getContext(), floatingActionButtonSharedClosets);
                for (int i = 0; i < sharedUserNames.size(); i++) {
                        popupMenu.getMenu().add(0,i,Menu.NONE,sharedUserNames.get(i));
                }
                popupMenu.getMenu().add(0,sharedUserNames.size(),Menu.NONE,mDatabaseHelper.getUserName());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // DO SOMETHING
                        if (item.getItemId() == sharedUserNames.size()) {
                            setUpRecycler(view);
                        } else {
                            for (int i = 0; i < sharedUserNames.size() + 1; i++) {
                                if (i == item.getItemId()) {
                                    // DO HERE
                                    setUpRecycler(view, sharedUserID.get(i));
                                }
                            }
                        }


                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        return view;
    }

    private void storeValuesInArrays() {
        ClothingItem CI;
        clothingItems.clear();


        Cursor cursor = mDatabaseHelper.readUsersClothing();
        if (cursor.getCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            textViewEmptyCloset.setVisibility(View.VISIBLE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            textViewEmptyCloset.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIR : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : FAVOR: DATE : userID
                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getString(4),
                        cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                        cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                clothingItems.add(CI);
            }
            cursor.close();
        }


    }

    public void AddData(String item, String brand, String pattern,
                        String c1, String c2, String fit, String type, String size, String material, String desc,String date) {



        boolean insertData = mDatabaseHelper.addClothing(item, brand, pattern, c1, c2, fit, type, size,material,desc,currentDate);

        if (insertData)
            toastMessage("Data Successfully Inserted!");
        else
            toastMessage("Something went wrong");
    }

    private void toastMessage(String Message) {
        Toast.makeText(getContext(),Message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("id",String.valueOf(clothingItems.get(position).getClothingID()));
        bundle.putString("name", String.valueOf(clothingItems.get(position).getName()));
        bundle.putString("brand", String.valueOf(clothingItems.get(position).getBrand()));
        bundle.putString("type",String.valueOf(clothingItems.get(position).getType()));
        bundle.putString("fit", String.valueOf(clothingItems.get(position).getFit()));
        UpdateFragment frag = new UpdateFragment();
        frag.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                frag).addToBackStack(null).commit();
    }

    private void filterList(String s) {
        s = s.trim(); // gets rid of extra spaces
        while (s.indexOf(" ") != -1) { // Theres multiple words
            s = s.substring(s.indexOf(" "));
            s = s.trim();
        }

        List<ClothingItem> rawList = new ArrayList<>(); // Might grab duplicates
        List<ClothingItem> filteredList = new ArrayList<>(); // Will have no duplicates
        for (ClothingItem item : clothingItems) {
            if (item.getName().toLowerCase().contains(s.toLowerCase())) {
                rawList.add(item);
            }
            if (item.getBrand().toLowerCase().contains(s.toLowerCase())) {
                rawList.add(item);
            }
            if (item.getType().toLowerCase().contains(s.toLowerCase())) {
                rawList.add(item);
            }
            if (item.getStatus().toLowerCase().contains(s.toLowerCase())) {
                rawList.add(item);
            }        }

        if (rawList.isEmpty()) {

        } else {
            for (ClothingItem item :  rawList) {
                if (!filteredList.contains(item))
                    filteredList.add(item);
            }
            list.setData(filteredList);
        }
        }

    private void fillDB() {
        // I use this to fill database
        // Checking if my lap top is connected to github
        if (mDatabaseHelper.tagTableEmpty()) {
            mDatabaseHelper.addToTagsTable("Formal"); // 1
            mDatabaseHelper.addToTagsTable("Casual");// 2
            mDatabaseHelper.addToTagsTable("Athletic"); // 3
            mDatabaseHelper.addToTagsTable("Winter");// 4
            mDatabaseHelper.addToTagsTable("Spring");// 5
            mDatabaseHelper.addToTagsTable("Summer");// 6
            mDatabaseHelper.addToTagsTable("Fall"); // 7
            mDatabaseHelper.addToTagsTable("All"); // 8
        }

       /* if(mDatabaseHelper.eventTableEmpty()){
            mDatabaseHelper.addEvent("Event1","Loc1",currentDate,currentDate);
            mDatabaseHelper.addEvent("Event2","Loc2",currentDate,currentDate);
            mDatabaseHelper.addEvent("Event3","Loc3",currentDate,currentDate);
            mDatabaseHelper.addEvent("Event4","Loc4",currentDate,currentDate);
            mDatabaseHelper.addEvent("Event5","Loc5","08-30-2022","09-29-2022");
            mDatabaseHelper.addEvent("Event6","Loc6","10-15-2022","10-18-2022");
            mDatabaseHelper.addEvent("Event7","Loc7","01-15-2023","01-16-2023");
            mDatabaseHelper.addEvent("Event8","Loc7","06-21-2025","06-27-2025");
        }*/

        int cl; // Exists for testing purposes

        if (mDatabaseHelper.clothingTableEmpty()) { // Fill Clothing Table
            AddData("Yellow Jippy","Fruit of The Loom","Solid", "Yellow", "", "Mens", "T-Shirt","L","Cotton","Chill and comfy",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Die for Succ","Diesel","Graphic","Black", "Red","Mens", "T-Shirt","L","Cotton","Has paint on it",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Fall", cl);
            mDatabaseHelper.addTag("Spring", cl);
            mDatabaseHelper.addTag("Summer", cl);
            AddData("Big Brain Shirt","Gildan","Graphic", "Black", "Green", "Mens", "Long Sleeved Shirt","L","Cotton","Pewdiepie Merch",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Winter", cl);
            mDatabaseHelper.addTag("Fall", cl);
            mDatabaseHelper.addTag("Spring", cl);
            AddData("Cheap Flamingos","Unknown","Graphic", "Black", "Pink", "Mens", "Button Down Shirt","L","Cotton/Polyester Blend","Feels like paper wtf",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Spring", cl);
            mDatabaseHelper.addTag("Summer", cl);
            AddData("Nice Button Down","Calvin Klein","Solid", "Blue", "", "Mens", "Button Down Shirt","41/16","Cotton/Polyester Blend","Goes with suit",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Formal", cl);
            mDatabaseHelper.addTag("All", cl);

            // Pants
            AddData("Comfy Jeans","Lucky Brand", "Solid","Blue", "","Mens","Pants","34X34","Denim","",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Tight Jeans","Lucky Brand","Solid","Blue", "","Mens", "Pants","34X32","Denim","",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Winter Jeans","Wrangler","Solid","Blue", "","Mens", "Pants","32X32","Denim","Super Comfy",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Winter", cl);
            mDatabaseHelper.addTag("Fall", cl);
            AddData("Black Jeans","Polo","Solid","Black", "","Mens", "Pants","34X32","Denim","A little wide",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Fancy Pants","Ralph Lauren","Solid","Black", "","Mens", "Pants","34X32","Cotton/Polyester Blend","For very formal occasions",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Formal", cl);
            mDatabaseHelper.addTag("All", cl);

            // Shoes
            AddData("White Destiny Boots","Palladium","Solid","White","","Mens", "Shoes","10","No Clue","",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Spring", cl);
            mDatabaseHelper.addTag("Summer", cl);
            mDatabaseHelper.addTag("Winter", cl);
            AddData("Olive Boots","Palladium","Solid", "Green", "", "Mens", "Shoes","10","Polyester","",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Work Boots","Timberland","Solid", "Brown", "", "Mens", "Shoes","10","Something Tough","Look like shit lol",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Fancy Shoes","Ralph Lauren","Solid", "Black", "", "Mens", "Shoes","10","Shiny","Pretty comfy actually",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Formal", cl);
            mDatabaseHelper.addTag("All", cl);

            AddData("Fish Hat","No Clue","Solid","Blue", "","Unisex", "Hat","One Size","Polyester","",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);

            // sandals
            AddData("Crocs","No Clue","Solid","Red", "","Unisex", "Sandals","One Size","Polyester","",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);

            // socks
            AddData("Black Socks","No Clue","Solid","Black", "","Unisex", "Socks","One Size","Polyester","",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Fancy", cl);
            mDatabaseHelper.addTag("All", cl);

            //scarf
            AddData("Long Scarf","Nike","Stripped","Red", "White","Unisex", "Scarf","One Size","Polyester","it looks nice",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);

            //Gloves
            AddData("Mittens","Nautica","Solid","Black", "","Unisex", "Gloves","One Size","Polyester","its warm",currentDate);
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);

        }
    }

    private void setUpRecycler(View view) {
        storeValuesInArrays();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ClothingAdapter(getContext() ,clothingItems, this);
        recyclerView.setAdapter(list);
    }

    public void checkAndSetShared() {
        if (!mDatabaseHelper.isSharing()) {
            floatingActionButtonSharedClosets.setVisibility(View.VISIBLE);
            Cursor cursor = mDatabaseHelper.getSharedUsers();
            while(cursor.moveToNext()) {
                sharedUserNames.add(cursor.getString(0));
                sharedUserID.add(cursor.getString(1));
            }
            cursor.close();
        }
    }

    private void storeValuesInArrays(String userID) {
        ClothingItem CI;
        clothingItems.clear();


        Cursor cursor = mDatabaseHelper.readSharedCloset(userID);
        if (cursor.getCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            textViewEmptyCloset.setVisibility(View.VISIBLE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            textViewEmptyCloset.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIR : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),cursor.getString(13),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                clothingItems.add(CI);
            }
            cursor.close();
        }
    }

    private void setUpRecycler(View view, String userID) {
        storeValuesInArrays(userID);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ClothingAdapter(getContext() ,clothingItems, this);
        recyclerView.setAdapter(list);
    }


}
