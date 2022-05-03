package com.example.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class WeatherFragment extends Fragment implements UserAdapter.itemClickInterface{

    private View v;
    private String url = "https://api.openweathermap.org/data/2.5/weather?lat=40.733471&lon=-73.445083&units=imperial&appid=ae4124b573a94aec76337478a86b3885";
    TextView textViewTemp, textViewCondition;
    ImageView imageViewWarning;
    Button buttonGenerate;

    private ArrayList<String> types = new ArrayList<>();
    private ArrayList<String> garbage = new ArrayList<>();
    private RecyclerView recyclerViewTest;
    private UserAdapter list;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_weather, container, false);

        textViewCondition = v.findViewById(R.id.textViewCondition);
        textViewTemp = v.findViewById(R.id.textViewTemp);
        buttonGenerate = v.findViewById(R.id.buttonGenerate);
        recyclerViewTest = v.findViewById(R.id.recyclerViewTesting);
        imageViewWarning = v.findViewById(R.id.imageViewWarning);

        recyclerViewTest.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new UserAdapter(types, garbage, this);
        recyclerViewTest.setAdapter(list);


        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // List of Conditions
                types.clear();
                garbage.clear();
                imageViewWarning.setVisibility(View.GONE);
                ArrayList<String> conditions = new ArrayList<>();
                conditions.add("Clear");
                conditions.add("Rain");
                conditions.add("Slush");
                conditions.add("Showers");
                conditions.add("Sleet");
                conditions.add("Snow");
                conditions.add("Hail");
                conditions.add("Sunny");
                conditions.add("Cloudy");

                // Randomly generate temp and condition
                Random random = new Random();
                int t = random.nextInt(105);
                String c = conditions.get(random.nextInt(conditions.size()));
                textViewTemp.setText(String.valueOf(t));
                textViewCondition.setText(c);

                //Now we generate types based on weather
                boolean b = c.contains("Rain") || c.contains("Snow") || c.contains("Showers")
                        || c.contains("Slush") || c.contains("Hail") || c.contains("Sleet");

                if (t >= 88.0) {
                    // Hot Bracket
                    types.add("T-Shirt");
                    types.add("Shorts");
                    types.add("Shoes");
                    if (b) {
                        imageViewWarning.setVisibility(View.VISIBLE);
                        types.add("Jacket");
                    }


                } else if (t < 88 && t >= 45) {
                    // Warm bracket
                    types.add("T-Shirt");
                    types.add("Pants");
                    types.add("Shoes");
                    if (b) {
                        imageViewWarning.setVisibility(View.VISIBLE);
                        types.add("Jacket");
                    }

                } else if (t < 45 && t >= 32) {
                    // Cold bracket
                    types.add("Long Sleeved Shirt");
                    types.add("Pants");
                    types.add("Shoes");
                    types.add("Jacket");
                    if (b) {
                        imageViewWarning.setVisibility(View.VISIBLE);
                    }

                } else {
                    // Freezing
                    types.add("Long Sleeved Shirt");
                    types.add("Pants");
                    types.add("Shoes");
                    types.add("Jacket");
                    if (b) {
                        imageViewWarning.setVisibility(View.VISIBLE);
                    }
                }

                list.notifyDataSetChanged();
            }
        });

        return v;
    }

    @Override
    public void onItemClick(int position) { }
}

//    public void findWeather() {
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//           @Override
//           public void onResponse(JSONObject response) {
//                try{
//                    JSONObject main = response.getJSONObject("main");
//                    JSONArray array = response.getJSONArray("weather");
//                    JSONObject object = array.getJSONObject(0);
//
//                    Double t = main.getDouble("temp");
//                    String d = object.getString("description");
//                    String c = response.getString("name");
//
//                    temp.setText(t.toString());
//                    city.setText(c);
//                    description.setText(d);
//
//                   Calendar calendar = Calendar.getInstance();
//                   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
//                   String formattedDate = simpleDateFormat.format(calendar.getTime());
//                   date.setText(formattedDate);
//
//
//
//                } catch(JSONException e){
//                    e.printStackTrace();
//                }
//           }
//       }, new Response.ErrorListener() {
//           @Override
//           public void onErrorResponse(VolleyError error) {
//
//           }
//       });
//        queue.add(jor);
//    }