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

    private List<ClothingItem> mClothingList;
    private itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtviewClothingName, textViewBrand, textViewType;
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
        public void onClick(View view) { itemClickInterface.onItemClick(getBindingAdapterPosition());}
    } // end MyViewHolder

    public ClothingAdapter(List<ClothingItem> clothingItems, itemClickInterface itemClickInterface) {
        mClothingList = clothingItems;
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
        String clothing = mClothingList.get(position).getName();
        String brand = mClothingList.get(position).getBrand();
        String type = mClothingList.get(position).getType();

        holder.txtviewClothingName.setText(clothing);
        holder.textViewType.setText(type);
        holder.textViewBrand.setText(brand);
    }

    @Override
    public int getItemCount() {
        return mClothingList.size();
    }

    public void setData(List<ClothingItem> clothingItems) {
        mClothingList = clothingItems;
        notifyDataSetChanged();
    }

    // Attempting to solve above issue using interface
    public interface itemClickInterface {
        void onItemClick(int position);
    }

    // other code
    // goes here…
}
