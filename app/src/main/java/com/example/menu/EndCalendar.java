package com.example.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EndCalendar extends AppCompatActivity {
String date,startDate;
boolean isDay, isMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_calendar);
        CalendarView startCalendar = (CalendarView) findViewById(R.id.end_Calendar);
        TextView txtEndDate = findViewById(R.id.textViewEndDate);
        Button endDate = (Button) findViewById(R.id.saveDateEnd);
        startDate = getIntent().getExtras().getString("date");

        startCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {

                String actualmonth;
                String actualday;

                // format the month
                if((month+1)<10){
                    actualmonth="0"+(month+1);
                }
                else {
                    actualmonth = String.valueOf((month + 1));
                }
                // format the day
                if (dayOfMonth < 10){
                    actualday= "0"+dayOfMonth;
                }
                else{
                    actualday = String.valueOf(dayOfMonth);
                }
               date= actualmonth+ "-"+ actualday+"-"+year;



                txtEndDate.setText(date);
                endDate.setEnabled(true);


            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String startMonth = startDate.substring(0,2);
                String endMonth = date.substring(0,2);
                String startDay = startDate.substring(3,5);
                String endDay = date.substring(3,5);
                String startYear =startDate.substring(6);
                String endYear = date.substring(6);


                // compares if start month comes before or after end month
                if(startYear.compareTo(endYear)<=0){
                    if(startMonth.compareTo(endMonth)<0) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("date", date);
                        setResult(1, resultIntent);
                        finish();
                    }
                        else if(startMonth.compareTo(endMonth)==0){
                            if(startDay.compareTo(endDay)>0) {
                                toastMessage("Invalid End Date");
                             }
                            else {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("date", date);
                                setResult(1, resultIntent);
                                finish();
                            }
                        }
                        else{
                        toastMessage("Invalid End Date");
                    }

                    }else{
                        toastMessage("Invalid End Date");
                    }
                }
        });
}
    private void toastMessage(String Message) {
        Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_SHORT).show();
    }
}