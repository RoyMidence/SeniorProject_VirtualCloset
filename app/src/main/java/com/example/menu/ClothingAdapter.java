package com.example.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.List;

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.MyViewHolder>{

    private Context context;

    private List<ClothingItem> mClothingList;
    private itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtviewClothingName, textViewBrand, textViewType;
        ImageButton imageButtonStatus;
        CheckBox checkBoxFavorite;
        itemClickInterface itemClickInterface;
        CardView mainLayout;

        public MyViewHolder(View view, itemClickInterface itemClickInterface) {
            super(view);
            txtviewClothingName = (TextView) view.findViewById(R.id.txtViewClothingName);
            textViewBrand = (TextView) view.findViewById(R.id.textViewBrand);
            textViewType = (TextView) view.findViewById(R.id.textViewType);
            imageButtonStatus = (ImageButton) view.findViewById(R.id.imageButtonStatus);
            checkBoxFavorite = (CheckBox) view.findViewById(R.id.checkBoxFavorite);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { itemClickInterface.onItemClick(getBindingAdapterPosition());}
    } // end MyViewHolder

    public ClothingAdapter(Context c,List<ClothingItem> clothingItems, itemClickInterface itemClickInterface) {
        context = c;
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
        String pattern = mClothingList.get(position).getPattern();
        String type = mClothingList.get(position).getType();
        String status = mClothingList.get(position).getStatus();
        String id = String.valueOf(mClothingList.get(position).getClothingID());
        boolean fave = mClothingList.get(position).isFavorite();

        holder.txtviewClothingName.setText(clothing);
        holder.textViewType.setText(type);
        holder.textViewBrand.setText(pattern);

        holder.imageButtonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(context);
                PopupMenu popupMenu = new PopupMenu(context, holder.imageButtonStatus);

                popupMenu.getMenu().add(0,0,Menu.NONE,"Available");
                popupMenu.getMenu().add(0,1,Menu.NONE,"Unavailable");
                popupMenu.getMenu().add(0,2,Menu.NONE,"Borrowed");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // DO SOMETHING
                        if (item.getItemId() == 0) {
                            db.updateClothingStatus(id,"Available");
                            holder.imageButtonStatus.setImageResource(R.drawable.ic_hanger);
                            holder.imageButtonStatus.setBackgroundColor(Color.parseColor("#90EE90"));
                        } else if (item.getItemId() == 1){
                            db.updateClothingStatus(id,"Unavailable");
                            holder.imageButtonStatus.setImageResource(R.drawable.ic_logout);
                            holder.imageButtonStatus.setBackgroundColor(Color.parseColor("#DC143C"));
                        } else {
                            db.updateClothingStatus(id,"Borrowed");
                            holder.imageButtonStatus.setImageResource(R.drawable.ic_borrowed);
                            holder.imageButtonStatus.setBackgroundColor(Color.parseColor("#ADD8E6"));
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.checkBoxFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(context);
                db.updateClothingFavorite(id, String.valueOf(holder.checkBoxFavorite.isChecked()));
            }
        });

        if (status.equals("Unavailable")) {
            holder.imageButtonStatus.setImageResource(R.drawable.ic_logout);
            holder.imageButtonStatus.setBackgroundColor(Color.parseColor("#DC143C"));
        } else if(status.equals("Borrowed")) {
            holder.imageButtonStatus.setImageResource(R.drawable.ic_borrowed);
            holder.imageButtonStatus.setBackgroundColor(Color.parseColor("#ADD8E6"));
        } else {
            holder.imageButtonStatus.setImageResource(R.drawable.ic_hanger);
            holder.imageButtonStatus.setBackgroundColor(Color.parseColor("#90EE90"));
        }

        holder.checkBoxFavorite.setChecked(fave);
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
