package com.example.menu;

import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TheClothesFragment extends Fragment implements ClothingAdapter.itemClickInterface {
    private DatabaseHelper mDatabaseHelper;

    private ImageView emptyImageView;
    private TextView textViewEmptyCloset;

    private List<String> clothingName = new ArrayList<>();
    private List<String> clothingID = new ArrayList<>();
    private List<String> clothingBrand = new ArrayList<>();
    private List<String> clothingType = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyImageView = (ImageView) getView().findViewById(R.id.emptyImageView);
        textViewEmptyCloset = (TextView) getView().findViewById(R.id.textViewEmptyCloset);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_thecloset,container,false);

        mDatabaseHelper = new DatabaseHelper(getContext());

        emptyImageView = view.findViewById(R.id.emptyImageView);
        textViewEmptyCloset = view.findViewById(R.id.textViewEmptyCloset);

        // I use this to fill database
        // Checking if my lap top is connected to github
        if (mDatabaseHelper.colorTableEmpty()) {
            mDatabaseHelper.addToColorTable("Red");
            mDatabaseHelper.addToColorTable("Black");
            mDatabaseHelper.addToColorTable("White");
            mDatabaseHelper.addToColorTable("Green");
            mDatabaseHelper.addToColorTable("Blue");
            mDatabaseHelper.addToColorTable("Yellow");
            mDatabaseHelper.addToColorTable("Pink");
            mDatabaseHelper.addToColorTable("Orange");
        }

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
            mDatabaseHelper.addUser("Person1", "abcdef", "abcd-efgh");
            mDatabaseHelper.addUser("Person2", "abcdef", "abcd-efgh");
            mDatabaseHelper.addUser("Person3", "abcdef", "abcd-efgh");
            mDatabaseHelper.addUser("Person4", "abcdef", "abcd-efgh");
        }

        if (mDatabaseHelper.loggedUserTableEmpty()) {
            mDatabaseHelper.logginUser("1");
        }


        int cl; // Exists for testing purposes

        if (mDatabaseHelper.clothingTableEmpty()) { // Fill Clothing Table
            AddData("Yellow Jippy","Fruit of The Loom","Solid","Mens", "Shirt","L","Cotton","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Yellow",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Comfy Jeans","Lucky Brand", "Solid","Mens","Pants","34X34","Denim","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Blue",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("White Destiny Boots","Palladium","Solid","Mens", "Shoes","10","No Clue","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("White",cl);
            mDatabaseHelper.addTag("Formal", cl);
            mDatabaseHelper.addTag("Spring", cl);
            mDatabaseHelper.addTag("Summer", cl);
            mDatabaseHelper.addTag("Winter", cl);
            AddData("Fish Hat","No Clue","Solid","Mens", "Hat","One Size","Polyester","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Blue",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Die for Succ","Diesel","Graphic","Mens", "Shirt","L","Cotton","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Red",cl);
            mDatabaseHelper.addColor("Black", cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Fall", cl);
            mDatabaseHelper.addTag("Winter", cl);
            mDatabaseHelper.addTag("Summer", cl);
            AddData("Tight Jeans","Lucky Brand","Solid","Mens", "Pants","34X32","Denim","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Blue",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Olive Boots","Palladium","Solid","Mens", "Shoes","10","Polyester","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Green",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
        }


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewClothing);
        storeValuesInArrays();

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ClothingAdapter(TheClothesFragment.this, getContext(), clothingID,clothingName, clothingBrand, clothingType, this));





        return view;
    }

    private void storeValuesInArrays() {
        Cursor cursor = mDatabaseHelper.readUsersClothing(mDatabaseHelper.loggedUserID());
        if (cursor.getCount() == 0) {
            emptyImageView.setVisibility(View.VISIBLE);
            textViewEmptyCloset.setVisibility(View.VISIBLE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            textViewEmptyCloset.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                clothingID.add(cursor.getString(0));
                clothingName.add(cursor.getString(1));
                clothingBrand.add(cursor.getString(2));
                clothingType.add(cursor.getString(3));
            }
        }
    }

    public void AddData(String item, String brand, String pattern, String fit, String type, String size, String material, String desc) {
        boolean insertData = mDatabaseHelper.addClothing(item, brand, pattern, fit, type, size,material,desc);

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
        bundle.putString("id",clothingID.get(position));
        bundle.putString("name", clothingName.get(position));
        bundle.putString("brand", clothingBrand.get(position));
        bundle.putString("type",clothingType.get(position));
        UpdateFragment frag = new UpdateFragment();
        frag.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                frag).commit();
    }
}
