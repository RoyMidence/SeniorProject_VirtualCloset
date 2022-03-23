package com.example.menu;
// Hadia March 15

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddClothesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private View v;
    TextView tv;
    TextView tv_material;
    TextView tv_fancy_casual;
    TextView tv_pattern;
    EditText ed_name;

    public AddClothesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_addclothes, container, false);
        configureButtons();
        tv = (TextView)v.findViewById(R.id.clothes_type);
        tv_material = (TextView)v.findViewById(R.id.material_type);
        tv_fancy_casual= (TextView)v.findViewById(R.id.fancy_casual_type);
        tv_pattern = (TextView) v.findViewById(R.id.pattern_type);
        ed_name = (EditText) v.findViewById(R.id.edit_text_name);
        return v;
    }

    private void configureButtons() {
        // TODO Auto-generated method stub
        ImageButton btn = (ImageButton) v.findViewById(R.id.image_button);
        ImageButton btn_material = (ImageButton) v.findViewById(R.id.image_material);
        ImageButton btn_fancy_casual = (ImageButton) v.findViewById(R.id.image_fancy_casual);
        ImageButton btn_pattern = (ImageButton) v.findViewById(R.id.image_pattern);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.second_screen);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.type_menu);
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

        btn_pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.pattern_menu);
                popup.show();

            }
        });

        fab.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){ Bundle bundle = new Bundle();
               bundle.putString("type",String.valueOf(tv.getText()));
               bundle.putString("material",String.valueOf(tv_material.getText()));
               bundle.putString("occasion",String.valueOf(tv_fancy_casual.getText()));
               bundle.putString("pattern",String.valueOf(tv_pattern.getText()));
               bundle.putString("name",String.valueOf(ed_name.getText()));
                 AddClothesTwo frag = new AddClothesTwo();
                frag.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        frag).commit();

            }

        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

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
            case R.id.nav_hat:

                tv.setText("Hat");

                break;
            case R.id.nav_shoes:

                tv.setText("Shoes");
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
            case R.id.nav_one_color:
                tv_pattern.setText("Solid");
                break;
            case R.id.nav_two_color:
                tv_pattern.setText("Two Colors");
                break;

        }

        return false;
    }

}

