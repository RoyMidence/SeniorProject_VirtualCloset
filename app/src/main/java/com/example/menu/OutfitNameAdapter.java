package com.example.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OutfitNameAdapter extends RecyclerView.Adapter<OutfitNameAdapter.MyViewHolder> {
    private Context c;

    private ArrayList<String> Outfitname;
    private itemClickInterface mItemClickInterface;
    private  ArrayList<String> outfitId;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtviewOutfitName;

        itemClickInterface itemClickInterface;
        ConstraintLayout mainLayout;

        public MyViewHolder(View view, itemClickInterface itemClickInterface) {
            super(view);
            txtviewOutfitName = (TextView) view.findViewById(R.id.outfit_name_item);
            mainLayout = itemView.findViewById(R.id.outfit_name_layout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) { itemClickInterface.onItemClick(Outfitname,getBindingAdapterPosition());}
    } // end MyViewHolder
    public OutfitNameAdapter(Context context, ArrayList<String> outfitname,ArrayList<String> outfitId, itemClickInterface itemClickInterface) {
        this.c = context;
        this.Outfitname= outfitname;
        this.mItemClickInterface = itemClickInterface;
        this.outfitId = outfitId;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outfit_name_recycleview_item, parent, false);
        return new MyViewHolder(itemView, mItemClickInterface);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = Outfitname.get(position);

        holder.txtviewOutfitName.setText(name);


    }
    @Override
    public int getItemCount() { return Outfitname.size();}

    public void setData(ArrayList<String> outfits,ArrayList<String>outfitId) {
        Outfitname = outfits;
        outfitId = outfitId;
        notifyDataSetChanged();
    }


    public interface itemClickInterface {
        void onItemClick(ArrayList<String> name, int position);
    }
}
