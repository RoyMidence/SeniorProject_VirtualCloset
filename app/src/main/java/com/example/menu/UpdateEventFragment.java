package com.example.menu;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class UpdateEventFragment extends Fragment implements OutfitNameAdapter.itemClickInterface{
String title,id,location,start,end;
private DatabaseHelper mDatabaseHelper;
    private List<ClothingItem> clothingItems = new ArrayList<>();
    private ArrayList<String> clothingName = new ArrayList<>();
    private ArrayList<String> clothingID = new ArrayList<>();
    private ArrayList<String> outfitName = new ArrayList<>();
    private ArrayList<String> outfitID = new ArrayList<>();
    public UpdateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_update_event, container, false);
        Bundle bundle = this.getArguments();
        EditText editTitle = v.findViewById(R.id.editTextEditEventTitle);
        EditText editlocation = v.findViewById(R.id.editTextEditEventLocation);
        TextView startEdit = v.findViewById(R.id.textViewEditEventStart);
        TextView endEdit = v.findViewById(R.id.textViewEditEventEnd);
        TextView emptyClothes = v.findViewById(R.id.textViewEventClothesEmpty);
        TextView emptyOutfits = v.findViewById(R.id.textViewEventOutfitsEmpty);

        RecyclerView eventClothes  =v.findViewById(R.id.editEventsClothesRecycle);
        RecyclerView eventOutfit = v.findViewById(R.id.editEventOutfitRecycle);
        mDatabaseHelper = new DatabaseHelper(getContext());
        id = bundle.getString("id");
        title= bundle.getString("title");
        location = bundle.getString("location");
        start = bundle.getString("start");
        end = bundle.getString("end");
        editTitle.setText(title);
        editlocation.setText(location);
        startEdit.setText(start);
        endEdit.setText(end);
        configureButtons(v);
        setUpRecycler(v,eventClothes,eventOutfit);
        storeValuesInArrays(emptyClothes,emptyOutfits,id);
        return v;
    }


    private void configureButtons(View v){
        FloatingActionButton updateButton = v.findViewById(R.id.floatingActionButtonUpdateEdit);
        FloatingActionButton deleteButtton = v.findViewById(R.id.floatingActionButtonDeleteEdit);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        deleteButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete This Event?");
        builder.setMessage("Are you sure you want to delete ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(getContext());

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EventFragment()).commit();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    private void storeValuesInArrays(TextView emptyClothes, TextView emptyOutfits, String ID) {
        ClothingItem CI;
        clothingItems.clear();
        clothingName.clear();
        clothingID.clear();
        outfitName.clear();
        outfitID.clear();

        Cursor cursor = mDatabaseHelper.readEventClothing(ID);
        if (cursor.getCount() == 0) {
            emptyClothes.setVisibility(View.VISIBLE);

        } else {
            emptyClothes.setVisibility(View.GONE);


            while (cursor.moveToNext()) {

                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                clothingItems.add(CI);
                clothingName.add(cursor.getString(1));
                clothingID.add(cursor.getString(0));
            }
            cursor.close();
        }
        Cursor cursor2 = mDatabaseHelper.readEventOutfit(ID);
        if (cursor2.getCount() == 0) {

            emptyOutfits.setVisibility(View.VISIBLE);
        } else {

            emptyOutfits.setVisibility(View.GONE);

            while (cursor2.moveToNext()) {
                outfitID.add(cursor2.getString(0));
                outfitName.add(cursor2.getString(1));
            }
            cursor2.close();
        }
    }
    private void setUpRecycler(View view,RecyclerView eventClothes,RecyclerView eventOutfits) {

        eventClothes.setLayoutManager(new LinearLayoutManager(getContext()));
        OutfitNameAdapter list = new OutfitNameAdapter(getContext(),clothingName,clothingID,this);
        eventClothes.setAdapter(list);

        eventOutfits.setLayoutManager(new LinearLayoutManager(getContext()));
        OutfitNameAdapter list2 = new OutfitNameAdapter(getContext(),outfitName,outfitID,this);
        eventOutfits.setAdapter(list2);
    }

    @Override
    public void onItemClick(int position) {

    }
}