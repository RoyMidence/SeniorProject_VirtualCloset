package com.example.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.MyViewHolder>{

    private Context context;
    Fragment fragment;

    private List<String> ClothingID, clothingName;
    private itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtviewClothingName;
        itemClickInterface itemClickInterface;
        CardView mainLayout;

        public MyViewHolder(View view, itemClickInterface itemClickInterface) {
            super(view);
            txtviewClothingName = (TextView) view.findViewById(R.id.txtViewClothingName);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickInterface.onItemClick(getAdapterPosition());
        }
    } // end MyViewHolder

    public ClothingAdapter(Fragment fragment, Context context, List<String> clothingID, List<String> clothingName, itemClickInterface itemClickInterface) {
        this.fragment = fragment;
        this.context = context;
        this.ClothingID = clothingID;
        this.clothingName = clothingName;
        this.mItemClickInterface = itemClickInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clothing_recyclerview_item, parent, false);
        return new MyViewHolder(itemView, mItemClickInterface);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String clothing = clothingName.get(position);
        String ID = ClothingID.get(position);
        holder.txtviewClothingName.setText(clothing);
        /*
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id",ID);
                bundle.putString("name",clothing);
                UpdateFragment frag = new UpdateFragment();
                frag.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.);

                // This is old cold I'm trying to replace
                // Worked for Activities but not for fragments


                Intent intent = new Intent(context, UpdateFragment.class);
                intent.putExtra("id", ID);

                intent.putExtra("name", String.valueOf(clothing));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddClothesFragment()).commit();

            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return clothingName.size();
    }

    public void setData(List<String> clothingName) {
        this.clothingName = clothingName;
        notifyDataSetChanged();
    }

    // Attempting to solve above issue using interface
    public interface itemClickInterface {
        void onItemClick(int position);
    }


    // other code
    // goes here…
}
