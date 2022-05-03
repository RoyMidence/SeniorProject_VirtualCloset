package com.example.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    Fragment fragment;

    private List<String> mUserList;
    private List<String> mUserID;
    private UserAdapter.itemClickInterface mItemClickInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewUserFullName;
        UserAdapter.itemClickInterface itemClickInterface;
        ConstraintLayout userLayout;

        public MyViewHolder(View view, UserAdapter.itemClickInterface itemClickInterface) {
            super(view);
            textViewUserFullName = (TextView) view.findViewById(R.id.textViewUserFullName);
            userLayout = itemView.findViewById(R.id.userLayout);
            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { itemClickInterface.onItemClick(getBindingAdapterPosition());}
    } // end MyViewHolder

    public UserAdapter( List<String> userList, List<String> userID, UserAdapter.itemClickInterface itemClickInterface) {
        mUserList = userList;
        mUserID = userID;
        this.mItemClickInterface = itemClickInterface;
    }

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recyclerview_item, parent, false);
        return new UserAdapter.MyViewHolder(itemView, mItemClickInterface);
    }

    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, int position) {
        String user = mUserList.get(position);

        holder.textViewUserFullName.setText(user);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setData(List<String> users, List<String> id) {
        mUserList = users;
        mUserID = id;
        notifyDataSetChanged();
    }

    // Attempting to solve above issue using interface
    public interface itemClickInterface {
        void onItemClick(int position);
    }
}

