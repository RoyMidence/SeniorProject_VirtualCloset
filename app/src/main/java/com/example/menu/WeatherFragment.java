package com.example.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WeatherFragment extends Fragment {

    private View v;
    private String url = "https://api.openweathermap.org/data/2.5/weather?lat=40.733471&lon=-73.445083&units=imperial&appid=ae4124b573a94aec76337478a86b3885";
    TextView temp, city, date, description;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_weather, container, false);

        temp = (TextView) v.findViewById(R.id.temp);
        city = (TextView) v.findViewById(R.id.city);
        date = (TextView) v.findViewById(R.id.date);
        description = (TextView) v.findViewById(R.id.description);
        findWeather();

        return v;
    }

    public void findWeather() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
                try{
                    JSONObject main = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);

                    Double t = main.getDouble("temp");
                    String d = object.getString("description");
                    String c = response.getString("name");

                    temp.setText(t.toString());
                    city.setText(c);
                    description.setText(d);

                   Calendar calendar = Calendar.getInstance();
                   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                   String formattedDate = simpleDateFormat.format(calendar.getTime());
                   date.setText(formattedDate);



                } catch(JSONException e){
                    e.printStackTrace();
                }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
        queue.add(jor);
    }

}