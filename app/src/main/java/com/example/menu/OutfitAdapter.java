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
public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.MyViewHolder> {

    private Context context;
    Fragment fragment;

    private List<String> Outfitname;
    private itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtviewOutfitName;

       itemClickInterface itemClickInterface;
        CardView mainLayout;

        public MyViewHolder(View view, itemClickInterface itemClickInterface) {
            super(view);
            txtviewOutfitName = (TextView) view.findViewById(R.id.outfitname);
            mainLayout = itemView.findViewById(R.id.outfit_layout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) { itemClickInterface.onItemClick(getBindingAdapterPosition());}
    } // end MyViewHolder
    public OutfitAdapter(Fragment fragment, Context context, List<String> outfitname, itemClickInterface itemClickInterface) {
        this.fragment = fragment;
        this.context = context;
        this.Outfitname= outfitname;
        this.mItemClickInterface = itemClickInterface;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outfit_recycleview_item, parent, false);
        return new MyViewHolder(itemView, mItemClickInterface);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = Outfitname.get(position);

        holder.txtviewOutfitName.setText(name);

    }
    @Override
    public int getItemCount() {
        return Outfitname.size();
    }
    public interface itemClickInterface {
        void onItemClick(int position);
    }
}
