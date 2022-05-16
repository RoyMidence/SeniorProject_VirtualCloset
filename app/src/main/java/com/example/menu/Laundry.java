package com.example.menu;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Laundry extends Fragment implements NameAdapter.itemClickInterface {
    RecyclerView laundryRecyclerView;
    private List<ClothingItem> clothingItems = new ArrayList<>();
    DatabaseHelper mDatabaseHelper;
    Button laundryButton;

    public Laundry() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_laundry, container, false);

        mDatabaseHelper =  new DatabaseHelper(getContext());
        laundryRecyclerView = v.findViewById(R.id.recyclerViewLaundry);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        laundryRecyclerView.setLayoutManager(layoutManager);
        laundryRecyclerView.setAdapter(new NameAdapter(clothingItems,this));
        storeValuesInArray(v);
        laundryButton = v.findViewById(R.id.laundryButton);

        laundryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i <clothingItems.size(); i++){
                    mDatabaseHelper.updateClothingStatus(String.valueOf(clothingItems.get(i).getClothingID()),"Available");
                    FragmentTransaction ft =getParentFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,new Laundry());
                    ft.commit();
                }
            }
        });
        return v;
    }

    private void storeValuesInArray(View v){
        TextView laundryEmpty = v.findViewById(R.id.textViewLaundryEmpty);
        ClothingItem CI;
        Cursor cursor = mDatabaseHelper.readClothingTypeLaundry();
        if (cursor.getCount() == 0) {
            laundryEmpty.setVisibility(v.VISIBLE);
        }
        else {
            laundryEmpty.setVisibility(v.GONE);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                clothingItems.add(CI);
            }

            cursor.close();
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}