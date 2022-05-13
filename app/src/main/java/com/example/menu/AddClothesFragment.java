package com.example.menu;
// Hadia March 15

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddClothesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private View v;
    TextView tv;
    TextView tv_material, txtlight, txtheavy;
    TextView tv_fancy_casual;
    TextView tv_pattern;
    EditText ed_name;
   String fits;
   CheckBox Light,Heavy;


    public AddClothesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_addclothes, container, false);

        tv = (TextView)v.findViewById(R.id.clothes_type);
        tv_material = (TextView)v.findViewById(R.id.material_type);
        tv_fancy_casual= (TextView)v.findViewById(R.id.fancy_casual_type);
        tv_pattern = (TextView) v.findViewById(R.id.pattern_type);
        ed_name = (EditText) v.findViewById(R.id.edit_text_name);
        txtlight = v.findViewById(R.id.textViewLight);
        txtheavy = v.findViewById(R.id.textViewHeavy);
        configureButtons();

        return v;
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void configureButtons() {
        // TODO Auto-generated method stub
        ImageButton btn = (ImageButton) v.findViewById(R.id.image_button);
        ImageButton btn_material = (ImageButton) v.findViewById(R.id.image_material);
        ImageButton btn_fancy_casual = (ImageButton) v.findViewById(R.id.image_fancy_casual);
        ImageButton btn_pattern = (ImageButton) v.findViewById(R.id.image_pattern);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.second_screen);
        CheckBox checkBoxMen= v.findViewById(R.id.checkBoxMen);
        CheckBox checkBoxWomen = v.findViewById(R.id.checkBoxWomen);
        CheckBox checkBoxUnisex = v.findViewById(R.id.checkBoxUnisex);
         Light = v.findViewById(R.id.checkBoxLight);
         Heavy = v.findViewById(R.id.checkBoxHeavy);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.type_menu);
                popup.show();
                hideKeyboardFrom(getActivity(),v);
            }
        });

        btn_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.material_menu);
                popup.show();
                hideKeyboardFrom(getActivity(),v);

            }
        });

        btn_fancy_casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.fancy_casual_menu);
                popup.show();
                hideKeyboardFrom(getActivity(),v);

            }
        });

        btn_pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(AddClothesFragment.this);
                popup.inflate(R.menu.pattern_menu);
                popup.show();
                hideKeyboardFrom(getActivity(),v);

            }
        });
        checkBoxMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxMen.isChecked()){
                    fits = "Mens";
                    checkBoxWomen.setChecked(false);
                    checkBoxUnisex.setChecked(false);

                }
            }
        });
        checkBoxWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxWomen.isChecked()){
                    fits = "Women";
                    checkBoxUnisex.setChecked(false);
                    checkBoxMen.setChecked(false);
                }
            }
        });
        checkBoxUnisex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxUnisex.isChecked()){
                    fits = "Unisex";
                    checkBoxWomen.setChecked(false);
                    checkBoxMen.setChecked(false);
                }
            }
        });
        Light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Light.isChecked()){
                    Heavy.setChecked(false);

                    tv.setText("Light Jacket");
                }
            }
        });
        Heavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Heavy.isChecked()){
                    Light.setChecked(false);

                    tv.setText("Heavy Jacket");
                }
            }
        });
            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (String.valueOf(ed_name.getText()).equals("")) {
                        toastMessage("Please enter a name!");
                    } else {


                        Bundle bundle = new Bundle();
                        bundle.putString("type", String.valueOf(tv.getText()));
                        bundle.putString("material", String.valueOf(tv_material.getText()));
                        bundle.putString("occasion", String.valueOf(tv_fancy_casual.getText()));
                        bundle.putString("pattern", String.valueOf(tv_pattern.getText()));
                        bundle.putString("name", String.valueOf(ed_name.getText()));
                        bundle.putString("fits",fits);
                        AddClothesTwo frag = new AddClothesTwo();
                        frag.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                frag).commit();

                    }
                }
            });
        }

    private void toastMessage(String Message) {
        Toast.makeText(getContext(), Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_shirt:

                tv.setText("T-Shirt");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_LSshirt:

                tv.setText("Long Sleeved Shirt");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_BDshirt:

                tv.setText("Button Down Shirt");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_pants:

                tv.setText("Pants");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_socks:

                tv.setText("Socks");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_jacket:

                tv.setText("Jacket");

                txtlight.setVisibility(v.VISIBLE);
                txtheavy.setVisibility(v.VISIBLE);
                Light.setVisibility(v.VISIBLE);
                Heavy.setVisibility(v.VISIBLE);
                break;
            case R.id.nav_hat:

                tv.setText("Hat");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_shoes:

                tv.setText("Shoes");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_sandals:

                tv.setText("Sandals");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_gloves:

                tv.setText("Gloves");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
                break;
            case R.id.nav_scarf:

                tv.setText("Scarf");
                txtlight.setVisibility(v.GONE);
                txtheavy.setVisibility(v.GONE);
                Light.setVisibility(v.GONE);
                Heavy.setVisibility(v.GONE);
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
                tv_fancy_casual.setText("Formal");
                break;
            case R.id.nav_casual:
                tv_fancy_casual.setText("Casual");
                break;
            case R.id.nav_athletic:
                tv_fancy_casual.setText("Athletic");
                break;
            case R.id.nav_solid:
                tv_pattern.setText("Solid");
                break;
            case R.id.nav_striped:
                tv_pattern.setText("Striped");
                break;
            case R.id.nav_graphic:
                tv_pattern.setText("Graphic");
                break;
            case R.id.nav_floral:
                tv_pattern.setText("Floral");
                break;
            case R.id.nav_dotted:
                tv_pattern.setText("Dotted");
                break;
            case R.id.nav_plaid:
                tv_pattern.setText("Plaid");
                break;
            case R.id.nav_two_color:
                tv_pattern.setText("Two Colors");
                break;

        }

        return false;
    }

}

