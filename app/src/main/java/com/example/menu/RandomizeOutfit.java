package com.example.menu;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class RandomizeOutfit extends Fragment  implements NameAdapter.itemClickInterface{

    // For Weather
    private String url = "https://api.openweathermap.org/data/2.5/weather?lat=40.733471&lon=-73.445083&units=imperial&appid=ae4124b573a94aec76337478a86b3885";

     private View v;
    private RecyclerView recyclerViewRandomOutfit;
    private List<ClothingItem> clothingItems = new ArrayList<>();

    private DatabaseHelper mDatabaseHelper;
    private NameAdapter list;
    EditText outfitNameRandom;
    ActivityResultLauncher<Intent> otherActivityLauncher;

    TextView textViewTemperature, textViewWeather;

    // Globals for filter parameters
    private ArrayList<String> types = new ArrayList<>();
    private String c1, c2, occasion;
    private boolean createOutfit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_randomize_outfit, container, false);

        textViewTemperature = v.findViewById(R.id.textViewTemperature);
        textViewWeather = v.findViewById(R.id.textViewWeather);
        findWeather();



        // Set Basic Default Filter Parameters
        c1 = "Any";
        c2 = "Any";
        occasion = "Casual";
        createOutfit = true;


        outfitNameRandom = (EditText) v.findViewById(R.id.randomOutfit);
        mDatabaseHelper = new DatabaseHelper(getContext());
        recyclerViewRandomOutfit = v.findViewById(R.id.recyclerViewRandom);
        configureButtons();
        setUpRecycler(v);

        otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 0) {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                types. clear();
                                types = resultIntent.getStringArrayListExtra("types");
                                c1 = resultIntent.getExtras().getString("c1");
                                c2 = resultIntent.getExtras().getString("c2");
                                occasion = resultIntent.getExtras().getString("Occasion");
                                createOutfit = resultIntent.getExtras().getBoolean("createOutfit");

                            }
                        }

                    }
                });


        return v;
    }

    private void setUpRecycler(View view) {
        recyclerViewRandomOutfit.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new NameAdapter(clothingItems, this);
        recyclerViewRandomOutfit.setAdapter(list);
    }

    private boolean chooseClothingItem(String type) {
        ClothingItem CI;
        List<ClothingItem> temp = new ArrayList<>(); // will hold all of a specific type


        Cursor cursor = mDatabaseHelper.readClothingType(type); // table of specified type
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIR : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),cursor.getString(13),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                temp.add(CI);
            }
            cursor.close();
        } // Now have a list with the specified types


        // Filtering Occasions
        for (int i = 0; i < temp.size(); i++) {
            if (!temp.get(i).getOccasion().equals(occasion)) {
                temp.remove(i);
                i--;
            }
        }
        if(temp.size() == 0) {
            return false;
        }


        // Shorten list down to colors
        // Only doing for shirts for now
        temp = colorFilter(temp);
        if(temp.size() == 0) {
            return false;
        }

        Random random = new Random(); // use this to generate a random number
        clothingItems.add(temp.get(random.nextInt(temp.size()))); // adding a random clothing Item here
        return true;
    }

    private void chooseOutfit() {
        clothingItems.clear();
        ClothingItem CI;
        List<Outfit> of = new ArrayList<>();

        // Create Outfit Object
        // Create list of them
        // select one from list


        // Creating list of outfits
        Cursor cursor = mDatabaseHelper.readUserOutfits();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                // OUTFIT TABLE ID : NAME
                of.add(new Outfit(cursor.getString(0), cursor.getString(1)));
            }
            cursor.close();
        } // Now have a list with the outfits

        // SELECT OUTFIT
        Random random = new Random(); // use this to generate a random number
        int outfit = random.nextInt(of.size());
        String outfitID = of.get(outfit).getOutfitID();
        cursor = mDatabaseHelper.readOutfitClothing(outfitID); // reading the clothing of randomly selected outfit
        while (cursor.moveToNext()) {
            // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIT : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
            String id = cursor.getString(0);
            CI = new ClothingItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                    cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),
                    mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
            clothingItems.add(CI);
        }
        cursor.close();

        outfitNameRandom.setText(of.get(outfit).getOutfitName());
        // LIST CLOTHING
    }

    private List colorFilter(List<ClothingItem> clothingList) {
        List<ClothingItem> filterList = new ArrayList<>();  // Will hold clothing with correct Colors

        for (int i = 0; i < clothingList.size(); i++) {
            if (!c1.equals("Any") && !c2.equals("Any")) { // Both colors, looking for specified combo
                if (clothingList.get(i).getColor1().equals(c1) || clothingList.get(i).getColor2().equals(c2)) {
                    filterList.add(clothingList.get(i));
                }
            } else if (!c1.equals("Any")) { // Looking for primary color
                if (clothingList.get(i).getColor1().equals(c1)) {
                    filterList.add(clothingList.get(i));
                }
            } else if (!c2.equals("Any")) { // Looking for secondary color
                if (clothingList.get(i).getColor2().equals(c2)) {
                    filterList.add(clothingList.get(i));
                }
            }
        } // after the for loop, should now have list of all clothing with matching color
        // or no clothing at all

        // if there is applicable items, send them back
        // if not, use original list
        if (filterList.size() != 0) {
            clothingList = filterList;
            return clothingList;
        }
        return clothingList;
    }

    private void configureButtons() {
        Button randomButton = (Button) v.findViewById(R.id.randomizeButton);
        FloatingActionButton  filter = (FloatingActionButton)v.findViewById(R.id.filterFAB);
        ImageButton cancel = (ImageButton) v.findViewById(R.id.cancel);
        ImageButton save = (ImageButton) v.findViewById(R.id.save);
        Button wearbutton = (Button) v.findViewById(R.id.wearButton);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean worked = true;

                if (mDatabaseHelper.clothingTableEmpty()) {
                    Toast.makeText(getContext(), "No Clothing Available", Toast.LENGTH_SHORT).show();
                    return;
                }
                clothingItems.clear(); // new outfit everytime


                if (createOutfit) {
                    for (int i = 0; i < types.size(); i++) { // Will cycle through the types of clothing
                        if (!chooseClothingItem(types.get(i))) {
                            Toast.makeText(getContext(), "No available" + types.get(i), Toast.LENGTH_SHORT).show();
                            worked = false;
                            break;
                        }
                    }
                } else {
                    if (mDatabaseHelper.outfitTableEmpty()) {
                        Toast.makeText(getContext(), "No Outfits Available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    chooseOutfit();
                }

                if (worked) {
                    list.notifyDataSetChanged();
                    cancel.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    wearbutton.setVisibility(View.VISIBLE);
                }


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());


              String  outfitName = outfitNameRandom.getText().toString();


                if (String.valueOf(outfitNameRandom.getText()).equals("")) {
                    toastMessage("Please enter a name!");
                } else {
                    if(mDatabaseHelper.checkOutfitName(outfitName)){
                        toastMessage("Name already exist");
                    }
                    else {
                        mDatabaseHelper.addOutfit(outfitName,currentDate);
                        for (int i = 0; i < clothingItems.size(); i++) {
                            mDatabaseHelper.addClothingToOutfit(String.valueOf(clothingItems.get(i).getClothingID()));

                        }
                    }
                }

            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FilterChosenOutfit.class);
                intent.putStringArrayListExtra("types",types);
                intent.putExtra("c1",c1);
                intent.putExtra("c2",c2);
                intent.putExtra("Occasion",occasion);
                otherActivityLauncher.launch(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clothingItems.clear();
                list.notifyDataSetChanged();
                cancel.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                wearbutton.setVisibility(View.GONE);
                outfitNameRandom.setText("");

            }
        });
        wearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i <clothingItems.size(); i++){
                    mDatabaseHelper.updateClothingStatus(String.valueOf(clothingItems.get(i).getClothingID()),"Unavailable");

                }
                FragmentTransaction fr2 =getParentFragmentManager().beginTransaction();
                fr2.replace(R.id.fragment_container,new TheClothesFragment());
                fr2.commit();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (chooseClothingItem(clothingItems.get(position).getType())) {
            clothingItems.remove(position);
            list.notifyDataSetChanged();
        } else
            Toast.makeText(getContext(), "Error getting new " + clothingItems.get(position).getType(), Toast.LENGTH_SHORT).show();

    }

    private void toastMessage(String Message) {
        Toast.makeText(getContext(), Message, Toast.LENGTH_SHORT).show();
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

                    textViewTemperature.setText( String.valueOf(t.intValue()) + " F");
                    textViewWeather.setText(d);

                    String desc = d.toLowerCase();


                    if (t >= 88.0) {
                        // Hot Bracket
                        types.add("T-Shirt");
                        types.add("Pants");
                        types.add("Shoes");
                        if (desc.contains("rain") || desc.contains("snow")) {
                            types.add("Light Jacket");
                        }


                    } else if (t < 88 && t >= 45) {
                        // Warm bracket
                        types.add("T-Shirt");
                        types.add("Pants");
                        types.add("Shoes");
                        if (desc.contains("rain") || desc.contains("snow")) {
                            types.add("Light Jacket");
                        }

                    } else if (t < 45 && t >= 32) {
                        // Cold bracket
                        types.add("Long Sleeved Shirt");
                        types.add("Pants");
                        types.add("Shoes");
                        types.add("Heavy Jacket");

                    } else {
                        // Freezing
                        types.add("Long Sleeved Shirt");
                        types.add("Pants");
                        types.add("Shoes");
                        types.add("Heavy Jacket");

                    }

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