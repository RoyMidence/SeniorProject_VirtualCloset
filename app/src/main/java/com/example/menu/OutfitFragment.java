package com.example.menu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class OutfitFragment extends Fragment implements OutfitAdapter.itemClickInterface{
    private View v;
    private String name;
    private ArrayList<String> outfit_name = new ArrayList<>();
    private DatabaseHelper mDatabaseHelper;
    private TextView textViewEmptyCloset;
    private OutfitAdapter outfitAdapter;
    ActivityResultLauncher<Intent> otherActivityLauncher;


    public OutfitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v =inflater.inflate(R.layout.fragment_outfit, container, false);
        TextView tv_outfit= (TextView)v.findViewById(R.id.outfitname);
        mDatabaseHelper =  new DatabaseHelper(getContext());
        textViewEmptyCloset = (TextView) v.findViewById(R.id.textViewEmpty_Outfit);

        configureFabButton();
        fillDB();
        storeValuesInArray();


        // Method for getting ready to receive data
        // For Kieran this will go in the onCreate method in addOutfit
        otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 0) {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                ArrayList<String> newList =  resultIntent.getStringArrayListExtra("list");
                                outfitAdapter.setData(newList);
                            }
                        }
                    }
                });

        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewOutfit);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        outfitAdapter = new OutfitAdapter(OutfitFragment.this, getContext(),outfit_name,this);
        recyclerView.setAdapter(outfitAdapter);
    return v;
    }

    private void configureFabButton() {


        FloatingActionButton fab_add_outfits = (FloatingActionButton) v.findViewById(R.id.add_outfits);
        fab_add_outfits.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddOutfit.class);
                intent.putStringArrayListExtra("list",outfit_name);
                otherActivityLauncher.launch(intent);
            }

        });
    }

    private void storeValuesInArray(){
        outfit_name.clear();
        Cursor cursor = mDatabaseHelper.readUserOutfits();
        if (cursor.getCount() == 0) {
            textViewEmptyCloset.setVisibility(v.VISIBLE);
        } else {

            textViewEmptyCloset.setVisibility(v.GONE);

            while (cursor.moveToNext()) {
                String id = cursor.getString(1);
                outfit_name.add(id);
            }
        }
    }

    private void fillDB(){
        if(mDatabaseHelper.outfitTableEmpty()) {
            mDatabaseHelper.addOutfit("Kieran's Outfit");
            mDatabaseHelper.addOutfit("Hadia's Outfit");
            mDatabaseHelper.addOutfit("Roy's Outfit");
            mDatabaseHelper.addOutfit("Luis's Outfit");
            mDatabaseHelper.addOutfit("Trevor's Outfit");
            mDatabaseHelper.addOutfit("inside Outfit");
            mDatabaseHelper.addOutfit("Outside Outfit");
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(),UpdateOutfit.class);
        intent.putExtra("outfitID","1");
        intent.putStringArrayListExtra("list",outfit_name);
        otherActivityLauncher.launch(intent);
    }
}