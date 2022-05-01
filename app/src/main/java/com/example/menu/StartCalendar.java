package com.example.menu;

import androidx.activity.result.ActivityResultLauncher;
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

public class StartCalendar extends AppCompatActivity {
    String date, testDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_calendar);

        CalendarView startCalendar = (CalendarView) findViewById(R.id.start_Calendar);
        TextView startDate = findViewById(R.id.textViewDate);
        Button saveDate = (Button) findViewById(R.id.saveDate);

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

                                startDate.setText(date);
                                saveDate.setEnabled(true);


                            }
                        });

       saveDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

               String currentMonth = currentDate.substring(0,2);
               String startMonth = date.substring(0,2);
               String currentDay = currentDate.substring(3,5);
               String startDay = date.substring(3,5);
               String currentYear =currentDate.substring(6);
               String startYear = date.substring(6);

               if (currentYear.compareTo(startYear) < 0) {
                   Intent resultIntent = new Intent();
                   resultIntent.putExtra("date", date);
                   setResult(0, resultIntent);
                   finish();
               }
                else {
                   if (currentYear.compareTo(startYear) == 0) {
                       if (currentMonth.compareTo(startMonth) < 0) {
                           Intent resultIntent = new Intent();
                           resultIntent.putExtra("date", date);
                           setResult(0, resultIntent);
                           finish();
                       } else if (currentMonth.compareTo(startMonth) == 0) {
                           if (currentDay.compareTo(startDay) > 0) {
                               toastMessage("Invalid Start Date");
                           } else {
                               Intent resultIntent = new Intent();
                               resultIntent.putExtra("date", date);
                               setResult(0, resultIntent);
                               finish();
                           }
                       }
                        else{
                           toastMessage("Invalid Start Date");
                       }
                   } else {
                       toastMessage("Invalid Start Date");
                   }
               }
           }
       });
    }
    private void toastMessage(String Message) {
        Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_SHORT).show();
    }
}