package com.example.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TypeAdapter  extends RecyclerView.Adapter<TypeAdapter.MyViewHolder>{

    private List<String> mTypeList;
    private TypeAdapter.itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewUserFullName;
        TypeAdapter.itemClickInterface itemClickInterface;
        ConstraintLayout userLayout;

        public MyViewHolder(View view, TypeAdapter.itemClickInterface itemClickInterface) {
            super(view);
            textViewUserFullName = (TextView) view.findViewById(R.id.textViewUserFullName);
            userLayout = itemView.findViewById(R.id.userLayout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { itemClickInterface.onItemClick(getBindingAdapterPosition());}
    } // end MyViewHolder

    public TypeAdapter(List<String> typeList, TypeAdapter.itemClickInterface itemClickInterface) {
        mTypeList = typeList;
        this.mItemClickInterface = itemClickInterface;
    }

    @Override
    public TypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recyclerview_item, parent, false);
        return new TypeAdapter.MyViewHolder(itemView, mItemClickInterface);
    }

    @Override
    public void onBindViewHolder(TypeAdapter.MyViewHolder holder, int position) {
        String type = mTypeList.get(position);

        holder.textViewUserFullName.setText(type);
    }

    @Override
    public int getItemCount() {
        return mTypeList.size();
    }

    public void setData(List<String> types) {
        mTypeList = types;
        notifyDataSetChanged();
    }

    // Attempting to solve above issue using interface
    public interface itemClickInterface {
        void onItemClick(int position);
    }

}
