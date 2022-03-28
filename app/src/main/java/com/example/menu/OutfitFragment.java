package com.example.menu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

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
    private List<String> outfit_name = new ArrayList<>();
    private DatabaseHelper mDatabaseHelper;
    private TextView textViewEmptyCloset;




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
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewOutfit);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new OutfitAdapter(OutfitFragment.this, getContext(),outfit_name,this));
    return v;
    }
    private void configureFabButton() {


        FloatingActionButton fab_add_outfits = (FloatingActionButton) v.findViewById(R.id.add_outfits);
        fab_add_outfits.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddOutfit.class);
                startActivity(intent);
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

    }
}