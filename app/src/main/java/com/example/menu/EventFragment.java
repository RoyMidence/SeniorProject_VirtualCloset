package com.example.menu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class EventFragment extends Fragment implements EventAdapter.itemClickInterface{
    View v;
    TextView total, recents, futureEmpty, upComingEmpty;
    String startDate;
    ActivityResultLauncher<Intent> otherActivityLauncher;

    private ArrayList<String> eventTitle = new ArrayList<>();
    private ArrayList<String> eventLoc = new ArrayList<>();
    private ArrayList<String> eventID = new ArrayList<>();
    private ArrayList<String> eventStart = new ArrayList<>();
    private ArrayList<String> eventEnd = new ArrayList<>();

    private ArrayList<String> eventTitle2 = new ArrayList<>();
    private ArrayList<String> eventLoc2 = new ArrayList<>();
    private ArrayList<String> eventID2 = new ArrayList<>();
    private ArrayList<String> eventStart2 = new ArrayList<>();
    private ArrayList<String> eventEnd2 = new ArrayList<>();
    String dayAdvance,dateAdvance;
    String monthAdvance;
    String yearAdvance;
    int inc;

    private EventAdapter eventAdapter;
    private DatabaseHelper mDatabaseHelper;
    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_event, container, false);

        mDatabaseHelper =  new DatabaseHelper(getContext());

         futureEmpty = v.findViewById(R.id.txtViewFutureEventsEmpty);
         upComingEmpty = v.findViewById(R.id.textViewUpComingEmpty);
        total = v.findViewById(R.id.textviewTotalEvents);
        storeValuesInArray();
        configureButtons();
        setUpRecycler(v);
        setUpRecycler2(v);
        total.setText("Total Number of Events: "+ (eventID.size() + eventID2.size()))
        ;
        return v;
    }

    private void configureButtons() {
        FloatingActionButton add = (FloatingActionButton)v.findViewById(R.id.add_event_screen);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEvent.class);
                startActivity(intent);
            }
        });
    }

    private void storeValuesInArray(){
        String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

        monthAdvance = currentDate.substring(0,2);
        dayAdvance = currentDate.substring(3,5);
        yearAdvance =currentDate.substring(6);
        inc = Integer.parseInt(monthAdvance);

            inc++;

        if(inc<10){
            monthAdvance="0"+(inc);
        }
        else if(inc == 13) {
            monthAdvance="01";
        }
         else{
             monthAdvance = String.valueOf(inc);
        }

        dateAdvance = monthAdvance +"-"+ dayAdvance + "-" + yearAdvance;
        System.out.println(dateAdvance);

        eventEnd.clear();
       eventID.clear();
       eventLoc.clear();
       eventStart.clear();
       eventEnd.clear();

        eventEnd2.clear();
        eventID2.clear();
        eventLoc2.clear();
        eventStart2.clear();
        eventEnd2.clear();

        Cursor cursor = mDatabaseHelper.readUsersEvent();

        if (cursor.getCount() == 0) {

        futureEmpty.setVisibility(v.VISIBLE);
        upComingEmpty.setVisibility(v.VISIBLE);

        } else {
            futureEmpty.setVisibility(v.GONE);
            upComingEmpty.setVisibility(v.GONE);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                startDate = mDatabaseHelper.getStartDate(id);


                String startMonth = startDate.substring(0,2);
                String startDay = startDate.substring(3,5);
                String startYear = startDate.substring(6);

                if(startYear.compareTo(yearAdvance) == 0) {
                    int tempMonth = Integer.parseInt(monthAdvance)-1;
                    if(tempMonth == 0)
                        tempMonth= 12;
                     if ((Integer.parseInt(startMonth)==Integer.parseInt(monthAdvance))) {
                         if (startDay.compareTo(dayAdvance) <= 0) {
                             String title = cursor.getString(1);
                             String loc = cursor.getString(2);
                             String start = cursor.getString(3);
                             String end = cursor.getString(4);
                             eventID.add(id);
                             eventTitle.add(title);
                             eventLoc.add(loc);
                             eventStart.add(start);
                             eventEnd.add(end);
                         }
                         else{
                             String title = cursor.getString(1);
                             String loc = cursor.getString(2);
                             String start = cursor.getString(3);
                             String end = cursor.getString(4);
                             eventID2.add(id);
                             eventTitle2.add(title);
                             eventLoc2.add(loc);
                             eventStart2.add(start);
                             eventEnd2.add(end);
                         }
                     } else if(((Integer.parseInt(startMonth)) == tempMonth)){
                         if (startDay.compareTo(dayAdvance) >= 0) {
                             String title = cursor.getString(1);
                             String loc = cursor.getString(2);
                             String start = cursor.getString(3);
                             String end = cursor.getString(4);
                             eventID.add(id);
                             eventTitle.add(title);
                             eventLoc.add(loc);
                             eventStart.add(start);
                             eventEnd.add(end);
                         }

                    }
                     else{
                         String title = cursor.getString(1);
                         String loc = cursor.getString(2);
                         String start = cursor.getString(3);
                         String end = cursor.getString(4);
                         eventID2.add(id);
                         eventTitle2.add(title);
                         eventLoc2.add(loc);
                         eventStart2.add(start);
                         eventEnd2.add(end);
                     }
                }
                else{

                        String title = cursor.getString(1);
                        String loc = cursor.getString(2);
                        String start = cursor.getString(3);
                        String end = cursor.getString(4);
                        eventID2.add(id);
                        eventTitle2.add(title);
                        eventLoc2.add(loc);
                        eventStart2.add(start);
                        eventEnd2.add(end);
                    }
                }


            }
        }


    private void setUpRecycler(View v){
        RecyclerView recyclerView = v.findViewById(R.id.eventRecycle);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(EventFragment.this, getContext(),eventID,eventTitle,eventLoc,eventStart,eventEnd,this);
        recyclerView.setAdapter(eventAdapter);
    }
    private void setUpRecycler2(View v){
        RecyclerView recyclerView = v.findViewById(R.id.eventRecycleView2);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(EventFragment.this, getContext(),eventID2,eventTitle2,eventLoc2,eventStart2,eventEnd2,this);
        recyclerView.setAdapter(eventAdapter);
    }
    @Override
    public void onItemClick(int position) {

    }
}