package com.example.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AddClothesTwo extends Fragment {
    private View v;

    public AddClothesTwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // configureImageButton();
       // configureFabButton();
        v =inflater.inflate(R.layout.fragment_add_clothes_two, container, false);
        return v;
    }

/* private void configureImageButton() {
    // TODO Auto-generated method stub
    ImageButton btn_color = (ImageButton) v.findViewById(R.id.image_color);
    ImageButton btn_color2 = (ImageButton) v.findViewById(R.id.image_color2);
    ImageButton btn_size = (ImageButton) v.findViewById(R.id.image_size);
    ImageButton btn_season = (ImageButton) v.findViewById(R.id.image_season);

    btn_color.setOnClickListener(v -> {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(AddClothesTwo.this);
        popup.inflate(R.menu.color_menu);
        popup.show();

    });
    btn_color2.setOnClickListener(v -> {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(AddClothesTwo.this);
        popup.inflate(R.menu.color2_menu);
        popup.show();

    });
    btn_size.setOnClickListener(v -> {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(AddClothesTwo.this);
        popup.inflate(R.menu.size_menu);
        popup.show();

    });
    btn_season.setOnClickListener(v -> {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(AddClothesTwo.this);
        popup.inflate(R.menu.season_menu);
        popup.show();
    });

}

    private void configureFabButton(){

        FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.previous_screen);

        fab2.setOnClickListener(new View.OnClickListener()
        {

            public void onClick (View v){
                // Bundle bundle = new Bundle();
                //  bundle.putString("id",clothingID.get(position));
                // bundle.putString("name",clothingName.get(position));
                // frag.setArguments(bundle);
                UpdateFragment frag = new UpdateFragment();
                FragmentTransaction fr2 =getFragmentManager().beginTransaction();
                fr2.replace(R.id.fragment_container,new AddClothesFragment());
                fr2.commit();

            }

        });
    }


    public boolean onMenuItemClick(MenuItem item) {
       TextView tv_color = (TextView)v.findViewById(R.id.color_type);
       TextView tv_color2 = (TextView)v.findViewById(R.id.color2_type);
       TextView tv_season = (TextView)v.findViewById(R.id.season_type);
       TextView tv_size= (TextView)v.findViewById(R.id.size_type);
       switch(item.getItemId()){
           case R.id.nav_red:

               tv_color.setText("Red");

               break;
           case R.id.nav_black:
               tv_color.setText("Black");

               break;
           case R.id.nav_green:
               tv_color.setText("Green");

               break;
           case R.id.nav_blue:
               tv_color.setText("Blue");

               break;
           case R.id.nav_yellow:
               tv_color.setText("Yellow");

               break;
           case R.id.nav_pink:
               tv_color.setText("Pink");

               break;
           case R.id.nav_orange:
               tv_color.setText("Orange");

               break;
           case R.id.nav_red2:

               tv_color2.setText("Red");

               break;
           case R.id.nav_black2:
               tv_color2.setText("Black");

               break;
           case R.id.nav_green2:
               tv_color2.setText("Green");

               break;
           case R.id.nav_blue2:
               tv_color2.setText("Blue");

               break;
           case R.id.nav_yellow2:
               tv_color2.setText("Yellow");

               break;
           case R.id.nav_pink2:
               tv_color2.setText("Pink");

               break;
           case R.id.nav_orange2:
               tv_color2.setText("Orange");

               break;
           case R.id.nav_winter:
               tv_season.setText("Winter");

               break;
           case R.id.nav_spring:
               tv_season.setText("Spring");

               break;
           case R.id.nav_summer:
               tv_season.setText("Summer");

               break;
           case R.id.nav_fall:
               tv_season.setText("Fall");

               break;
           case R.id.nav_winter_fall:
               tv_season.setText("Winter and Fall");

               break;
           case R.id.nav_spring_summer:
               tv_season.setText("Spring and Summer");

               break;
           case R.id.nav_all:
               tv_season.setText("All");
           case R.id.nav_S:
               tv_size.setText("Small");
               break;
           case R.id.nav_M:
               tv_size.setText("Medium");
               break;
           case R.id.nav_L:
               tv_size.setText("Large");
               break;
           case R.id.nav_XL:
               tv_size.setText("Extra Large");
               break;
       }


       return false;
    }
*/

}