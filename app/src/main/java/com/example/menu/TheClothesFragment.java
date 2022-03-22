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
            mDatabaseHelper.addToTagsTable("Solid"); // ID's: 1
            mDatabaseHelper.addToTagsTable("Striped"); // 2
            mDatabaseHelper.addToTagsTable("Dotted"); // 3
            mDatabaseHelper.addToTagsTable("Floral"); // 4
            mDatabaseHelper.addToTagsTable("Graphic"); // 5
            mDatabaseHelper.addToTagsTable("Plaid"); // 6
            mDatabaseHelper.addToTagsTable("Two Colors"); // 7
            mDatabaseHelper.addToTagsTable("Formal"); // 8
            mDatabaseHelper.addToTagsTable("Casual");// 9
            mDatabaseHelper.addToTagsTable("Athletic"); // 10
            mDatabaseHelper.addToTagsTable("Winter");// 11
            mDatabaseHelper.addToTagsTable("Spring");// 12
            mDatabaseHelper.addToTagsTable("Summer");// 13
            mDatabaseHelper.addToTagsTable("Fall"); // 14
            mDatabaseHelper.addToTagsTable("All"); // 15
        }

        int cl; // Exists for testing purposes

        if (mDatabaseHelper.clothingTableEmpty()) { // Fill Clothing Table
            AddData("Yellow Jippy","Fruit of The Loom", "Shirt","L","Cotton","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Yellow",cl);
            mDatabaseHelper.addTag("Solid",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Comfy Jeans","Lucky Brand", "Pants","34X34","Denim","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Blue",cl);
            mDatabaseHelper.addTag("Solid",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("White Destiny Boots","Palladium", "Shoes","10","No Clue","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("White",cl);
            mDatabaseHelper.addTag("Solid",cl);
            mDatabaseHelper.addTag("Formal", cl);
            mDatabaseHelper.addTag("Spring", cl);
            mDatabaseHelper.addTag("Summer", cl);
            mDatabaseHelper.addTag("Winter", cl);
            AddData("Fish Hat","No Clue", "Hat","One Size","Polyester","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Blue",cl);
            mDatabaseHelper.addTag("Solid",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Die for Succ","Diesel", "Shirt","L","Cotton","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Red",cl);
            mDatabaseHelper.addColor("Black", cl);
            mDatabaseHelper.addTag("Graphic",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("Fall", cl);
            mDatabaseHelper.addTag("Winter", cl);
            mDatabaseHelper.addTag("Summer", cl);
            AddData("Tight Jeans","Lucky Brand", "Pants","34X32","Denim","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Blue",cl);
            mDatabaseHelper.addTag("Solid",cl);
            mDatabaseHelper.addTag("Casual", cl);
            mDatabaseHelper.addTag("All", cl);
            AddData("Olive Boots","Palladium", "Shoes","10","Polyester","");
            cl = mDatabaseHelper.getLatestItem();
            mDatabaseHelper.addColor("Green",cl);
            mDatabaseHelper.addTag("Solid",cl);
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
        Cursor cursor = mDatabaseHelper.readAllData();
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

    public void AddData(String item, String brand, String type, String size, String material, String desc) {
        boolean insertData = mDatabaseHelper.addClothing(item, brand,type, size,material,desc);

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
