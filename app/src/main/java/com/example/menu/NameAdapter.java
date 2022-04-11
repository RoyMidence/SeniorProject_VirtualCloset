package com.example.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.MyViewHolder> {
    private List<ClothingItem> mClothingList;
    private NameAdapter.itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtviewClothingName, textViewColor, textViewType;

        NameAdapter.itemClickInterface itemClickInterface;
        ConstraintLayout mainLayout;

        public MyViewHolder(View view, NameAdapter.itemClickInterface itemClickInterface) {
            super(view);
            txtviewClothingName = (TextView) view.findViewById(R.id.name_item);
            textViewColor = (TextView) view.findViewById(R.id.color1_item);
            textViewType = (TextView) view.findViewById(R.id.type_item);

            mainLayout = itemView.findViewById(R.id.name_layout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickInterface.onItemClick(getBindingAdapterPosition());
        }
    }

    public NameAdapter(List<ClothingItem> clothingItems, NameAdapter.itemClickInterface itemClickInterface) {

        mClothingList = clothingItems;
        this.mItemClickInterface = itemClickInterface;
    }

    @Override
    public NameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_recycleview_item, parent, false);
        return new NameAdapter.MyViewHolder(itemView, mItemClickInterface);
    }

    @Override
    public void onBindViewHolder(NameAdapter.MyViewHolder holder, int position) {
        String clothing = mClothingList.get(position).getName();
        String type = mClothingList.get(position).getType();
        String color = mClothingList.get(position).getColor1();

        holder.txtviewClothingName.setText(clothing);
        holder.textViewType.setText(type);
        holder.textViewColor.setText(color);


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
}