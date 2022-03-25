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

import org.w3c.dom.Text;

import java.util.List;

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.MyViewHolder>{

    private Context context;
    Fragment fragment;

    private List<String> clothingID, clothingName, clothingBrand, clothingType;
    private itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtviewClothingName;
        public TextView textViewBrand;
        public TextView textViewType;
        itemClickInterface itemClickInterface;
        CardView mainLayout;

        public MyViewHolder(View view, itemClickInterface itemClickInterface) {
            super(view);
            txtviewClothingName = (TextView) view.findViewById(R.id.txtViewClothingName);
            textViewBrand = (TextView) view.findViewById(R.id.textViewBrand);
            textViewType = (TextView) view.findViewById(R.id.textViewType);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickInterface.onItemClick(getAdapterPosition());
        }
    } // end MyViewHolder

    public ClothingAdapter(Fragment fragment, Context context, List<String> clothingID, List<String> clothingName, List<String> clothingBrand, List<String> clothingType, itemClickInterface itemClickInterface) {
        this.fragment = fragment;
        this.context = context;
        this.clothingID = clothingID;
        this.clothingName = clothingName;
        this.clothingBrand = clothingBrand;
        this.clothingType = clothingType;
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
        String brand = clothingBrand.get(position);
        String type = clothingType.get(position);

        holder.txtviewClothingName.setText(clothing);
        holder.textViewType.setText(type);
        holder.textViewBrand.setText(brand);
    }

    @Override
    public int getItemCount() {
        return clothingName.size();
    }

    public void setData(List<String> clothingID, List<String> clothingName, List<String> clothingBrand, List<String> clothingType) {
        this.clothingID = clothingID;
        this.clothingName = clothingName;
        this.clothingBrand = clothingBrand;
        this.clothingType = clothingType;
        notifyDataSetChanged();
    }

    // Get name value at index
    public String getName(int pos) {
        return clothingName.get(pos);
    }
    // Get brand value at index
    public String getBrand(int pos) {
        return clothingBrand.get(pos);
    }

    // Get type value at index
    public String getType(int pos) {
        return clothingType.get(pos);
    }

    // Attempting to solve above issue using interface
    public interface itemClickInterface {
        void onItemClick(int position);
    }

    // other code
    // goes here…
}
