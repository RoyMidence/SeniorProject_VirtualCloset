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
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private Context context;
    Fragment fragment;

    private ArrayList<String> eventID;
    private ArrayList<String> eventTitle;
    private  ArrayList<String> eventLoc;
    private ArrayList<String> eventSDate;
    private  ArrayList<String> eventEDate;
    private itemClickInterface mItemClickInterface;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView edtitle, edlocation,edstartDate,edendDate;
        EventAdapter.itemClickInterface itemClickInterface;
        CardView mainLayout;

        public MyViewHolder(View view, EventAdapter.itemClickInterface itemClickInterface) {
            super(view);
           edtitle = (TextView) view.findViewById(R.id.eventTitle);
            edlocation = (TextView) view.findViewById(R.id.eventLocation);
            edstartDate = (TextView) view.findViewById(R.id.eventStartDate);
            edendDate = (TextView) view.findViewById(R.id.eventEndDate);


            mainLayout = itemView.findViewById(R.id.event_layout);

            this.itemClickInterface = itemClickInterface;

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            itemClickInterface.onItemClick(getBindingAdapterPosition());
        }
    }
    public EventAdapter(Fragment fragment, Context context, ArrayList<String> eventid, ArrayList<String> eventTitle ,ArrayList<String> eventLoc,ArrayList<String> eventSDate, ArrayList<String> eventEDate,EventAdapter.itemClickInterface itemClickInterface) {
        this.fragment = fragment;
        this.context = context;
        this.eventID = eventid;
        this.eventTitle= eventTitle;
        this.eventLoc= eventLoc;
        this.eventSDate= eventSDate;
        this.eventEDate= eventEDate;
        this.mItemClickInterface = itemClickInterface;

    }
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_recyclerview_item, parent, false);
        return new EventAdapter.MyViewHolder(itemView, mItemClickInterface);
    }
    @Override
    public void onBindViewHolder(EventAdapter.MyViewHolder holder, int position) {
        String title = eventTitle.get(position);
        String location = eventLoc.get(position);
        String startDate = eventSDate.get(position);
        String endDate = eventEDate.get(position);

        holder.edtitle.setText(title);
        holder.edlocation.setText(location);
        holder.edstartDate.setText(startDate);
        holder.edendDate.setText(endDate);

    }
    @Override
    public int getItemCount() { return eventTitle.size();}

    public interface itemClickInterface {
        void onItemClick(int position);
    }
}
