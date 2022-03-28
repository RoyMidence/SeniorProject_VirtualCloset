package com.example.menu;

import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TheClothesFragment extends Fragment implements ClothingAdapter.itemClickInterface {
    private DatabaseHelper mDatabaseHelper;

    private ImageView emptyImageView;
    private TextView textViewEmptyCloset;
    private SearchView searchViewClothing;

    private List<ClothingItem> clothingItems = new ArrayList<>();
    private ClothingAdapter list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_thecloset,container,false);

        mDatabaseHelper = new DatabaseHelper(getContext());
        emptyImageView = view.findViewById(R.id.emptyImageView);
        textViewEmptyCloset = view.findViewById(R.id.textViewEmptyCloset);
        searchViewClothing = view.findViewById(R.id.searchViewClothing);
        searchViewClothing.clearFocus();

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

        fillDB();
        setUpRecycler(view);

        return view;
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
        }

        if (rawList.isEmpty()) {

        } else {
            for (ClothingItem item :  rawList) {
                if (!filteredList.contains(item))
                    filteredList.add(item);
            }
            list.setData(filteredList);
        }


    }

    private void storeValuesInArrays() {
        ClothingItem CI;
        clothingItems.clear();


        Cursor cursor = mDatabaseHelper.readUsersClothing(mDatabaseHelper.loggedUserID());
        if (cursor.getCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            textViewEmptyCloset.setVisibility(View.VISIBLE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            textViewEmptyCloset.setVisibility(View.GONE);

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

    public void AddData(String item, String brand, String pattern,
                        String c1, String c2, String fit, String type, String size, String material, String desc) {



        boolean insertData = mDatabaseHelper.addClothing(item, brand, pattern, c1, c2, fit, type, size,material,desc);

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

        if (mDatabaseHelper.userTableEmpty()) {
            mDatabaseHelper.addUser("Roy Midence","Roy", "abcdef");
            mDatabaseHelper.addUser("Trevor Ross","Trevor", "abcdef");
            mDatabaseHelper.addUser("Hadia Majed","Hadia", "abcdef");
            mDatabaseHelper.addUser("Luis Garcia","Luis", "abcdef");
            mDatabaseHelper.addUser("Kieran Sylvestre","Kieran", "abcdef");
        }

        int cl; // Exists for testing purposes

        if (mDatabaseHelper.clothingTableEmpty()) { // Fill Clothing Table
            AddData("Yellow Jippy","Fruit of The Loom","Solid", "Yellow", "", "Mens", "Shirt","L","Cotton","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Comfy Jeans","Lucky Brand", "Solid","Blue", "","Mens","Pants","34X34","Denim","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("White Destiny Boots","Palladium","Solid","White","","Mens", "Shoes","10","No Clue","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Formal", cl);
            mDatabaseHelper.addTag("Spring", cl);
            mDatabaseHelper.addTag("Summer", cl);
            mDatabaseHelper.addTag("Winter", cl);
            AddData("Fish Hat","No Clue","Solid","Blue", "","Unisex", "Hat","One Size","Polyester","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Die for Succ","Diesel","Graphic","Red", "Black","Mens", "Shirt","L","Cotton","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Fall", cl);
            mDatabaseHelper.addTag("Winter", cl);
            mDatabaseHelper.addTag("Summer", cl);
            AddData("Tight Jeans","Lucky Brand","Solid","Blue", "","Mens", "Pants","34X32","Denim","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Olive Boots","Palladium","Solid", "Green", "", "Mens", "Shoes","10","Polyester","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
        }
    }

    private void setUpRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewClothing);
        storeValuesInArrays();

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        list = new ClothingAdapter(clothingItems, this);
        recyclerView.setAdapter(list);
    }
}
