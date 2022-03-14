package com.example.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

public class AddClothesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private View v;
    public AddClothesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_addclothes,container, false);
        configureImageButton();
        return v;
    }

    private void configureImageButton() {
        // TODO Auto-generated method stub
        ImageButton btn = (ImageButton) v.findViewById(R.id.image_button);
        ImageButton btn_color = (ImageButton) v.findViewById(R.id.image_color);
        ImageButton btn_season = (ImageButton) v.findViewById(R.id.image_season);
        ImageButton btn_material = (ImageButton) v.findViewById(R.id.image_material);
        ImageButton btn_fancy_casual = (ImageButton) v.findViewById(R.id.image_fancy_casual);
        ImageButton btn_size = (ImageButton) v.findViewById(R.id.image_size);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.type_menu);
                popup.show();

            }
        });

        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.color_menu);
                popup.show();

            }
        });
        btn_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.season_menu);
                popup.show();

            }
        });
        btn_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.material_menu);
                popup.show();

            }
        });
        btn_fancy_casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.fancy_casual_menu);
                popup.show();

            }
        });
        btn_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.size_menu);
                popup.show();

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        TextView tv = (TextView)v.findViewById(R.id.clothes_type);
        TextView tv_color = (TextView)v.findViewById(R.id.color_type);
        TextView tv_season = (TextView)v.findViewById(R.id.season_type);
        TextView tv_material = (TextView)v.findViewById(R.id.material_type);
        TextView tv_fancy_casual= (TextView)v.findViewById(R.id.fancy_casual_type);
        TextView tv_size= (TextView)v.findViewById(R.id.size_type);


        switch(item.getItemId()){
            case R.id.nav_shirt:

                tv.setText("Shirt");

                break;
            case R.id.nav_pants:

                tv.setText("Pants");

                break;
            case R.id.nav_socks:

                tv.setText("Socks");
                break;
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

                break;
            case R.id.nav_wool:
                tv_material.setText("Wool");

                break;
            case R.id.nav_cotton:
                tv_material.setText("Cotton");

                break;
            case R.id.nav_polyester:
                tv_material.setText("Polyester");

                break;
            case R.id.nav_cotton_polyester:
                tv_material.setText("Cotton/Polyester blend");

                break;
            case R.id.nav_nylon:
                tv_material.setText("Nylon");

                break;
            case R.id.nav_latex:
                tv_material.setText("Latex");

                break;
            case R.id.nav_denim:
                tv_material.setText("Denim");

                break;
            case R.id.nav_other_material:
                tv_material.setText("Other");
                break;
            case R.id.nav_fancy:
                tv_fancy_casual.setText("Fancy");
                break;
            case R.id.nav_casual:
                tv_fancy_casual.setText("Casual");
                break;
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

}

