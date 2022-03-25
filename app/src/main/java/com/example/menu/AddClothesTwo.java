package com.example.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AddClothesTwo extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private View v;
    private DatabaseHelper mDatabaseHelper;
    String type;
    String material;
    String pattern;
    String occasion;
    String name;
    String fits;
    boolean isColor2 = true;
    TextView tv_color;
    TextView tv_color2;
    TextView tv_size;
    TextView tv_brand;
    EditText  ed_desc;
    CheckBox checkBoxSpring;
    CheckBox checkBoxFall;
    CheckBox checkBoxSummer;
    CheckBox checkBoxWinter;

    public AddClothesTwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_add_clothes_two, container, false);
        mDatabaseHelper = new DatabaseHelper(getContext());
        Bundle bundle = this.getArguments();
        type = bundle.getString("type");
        material = bundle.getString("material");
        pattern = bundle.getString("pattern");
        occasion = bundle.getString("occasion");
        name = bundle.getString("name");
        fits = bundle.getString("fits");

        tv_color = (TextView)v.findViewById(R.id.color_type);
        tv_color2 = (TextView)v.findViewById(R.id.color2_type);

        tv_size= (TextView)v.findViewById(R.id.size_type);
        tv_brand =(TextView)v.findViewById(R.id.brand_type);
        ed_desc = (EditText) v.findViewById(R.id.edit_text_description);

        checkBoxSpring= v.findViewById(R.id.checkBoxSpring_add);
        checkBoxSummer = v.findViewById(R.id.checkBoxSummer_add);
        checkBoxFall = v.findViewById(R.id.checkBoxFall_add);
        checkBoxWinter = v.findViewById(R.id.checkBoxWinter_add);
        configureImageButton();
        configureFabButtons();



        return v;
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void configureImageButton() {
    // TODO Auto-generated method stub
    ImageButton btn_color = (ImageButton) v.findViewById(R.id.image_color);
    ImageButton btn_color2 = (ImageButton) v.findViewById(R.id.image_color2);
    ImageButton btn_size = (ImageButton) v.findViewById(R.id.image_size);

    ImageButton btn_brand = (ImageButton) v.findViewById(R.id.image_brand);

    btn_color.setOnClickListener(v -> {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(AddClothesTwo.this);
        popup.inflate(R.menu.color_menu);
        popup.show();
        hideKeyboardFrom(getActivity(),v);

    });
    btn_color2.setOnClickListener(v -> {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(AddClothesTwo.this);
        popup.inflate(R.menu.color2_menu);
        popup.show();
        hideKeyboardFrom(getActivity(),v);

    });
    btn_size.setOnClickListener(v -> {

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(AddClothesTwo.this);
         if(type.equals("Pants")){

            popup.inflate(R.menu.pant_size_menu);

        }else if(type.equals("Shoes")){

            popup.inflate(R.menu.shoe_menu);
        }
        else{
            popup.inflate(R.menu.size_menu);
        }

        popup.show();
        hideKeyboardFrom(getActivity(),v);

    });
    btn_brand.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(getActivity(), v);
            popup.setOnMenuItemClickListener(AddClothesTwo.this);
            popup.inflate(R.menu.brand_menu);
            popup.show();
        hideKeyboardFrom(getActivity(),v);

        });

    if(pattern.equals("Solid")){
            btn_color2.setVisibility(View.GONE);
            tv_color2.setVisibility(View.GONE);
            isColor2 = false;

        }
}

    private void configureFabButtons(){

        FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.previous_screen);
        FloatingActionButton fab_add = (FloatingActionButton) v.findViewById(R.id.add_clothes);
        fab2.setOnClickListener(new OnClickListener()
        {

            public void onClick (View v){
                FragmentTransaction fr2 =getFragmentManager().beginTransaction();
                fr2.replace(R.id.fragment_container,new AddClothesFragment());
                fr2.commit();

            }

        });
        fab_add.setOnClickListener(new OnClickListener()
        {

            public void onClick (View v){
                String size = String.valueOf(tv_size.getText());
                String brand = String.valueOf(tv_brand.getText());
                String desc = String.valueOf(ed_desc.getText());
                String color = String.valueOf(tv_color.getText());
                String color2 =  String.valueOf(tv_color2.getText());
                //(String item, String brand, String pattern, String fit, String type, String size, String material, String desc)
                boolean insertData = mDatabaseHelper.addClothing(name,brand,pattern,color, color2,fits,type,size,material,desc);

                if (insertData)
                    toastMessage("Data Successfully Inserted!");
                else
                    toastMessage("Something went wrong");

                // Then Occasion
                mDatabaseHelper.addTag(occasion, mDatabaseHelper.getLatestItem());
                // Then weather
                if (checkBoxSpring.isChecked() && checkBoxFall.isChecked() &&
                        checkBoxSummer.isChecked() && checkBoxWinter.isChecked()) { // ALL SEASONS
                    mDatabaseHelper.addTag("All", mDatabaseHelper.getLatestItem());
                } else {
                    if (checkBoxSpring.isChecked())
                        mDatabaseHelper.addTag("Spring",mDatabaseHelper.getLatestItem());
                    if (checkBoxSummer.isChecked())
                        mDatabaseHelper.addTag("Summer", mDatabaseHelper.getLatestItem());
                    if (checkBoxFall.isChecked())
                        mDatabaseHelper.addTag("Fall", mDatabaseHelper.getLatestItem());
                    if (checkBoxWinter.isChecked())
                        mDatabaseHelper.addTag("Winter",mDatabaseHelper.getLatestItem());
                }

                FragmentTransaction fr2 =getFragmentManager().beginTransaction();
                fr2.replace(R.id.fragment_container,new TheClothesFragment());
                fr2.commit();
            }

            private void toastMessage(String Message) {
                Toast.makeText(getContext(),Message,Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

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

            case R.id.nav_nike:
                tv_brand.setText("Nike");
                break;

            case R.id.nav_nautica:
                tv_brand.setText("Nautica");
                break;
            case R.id.nav_adidas:
                tv_brand.setText("Adidas");
                break;
            case R.id.nav_polorl:
                tv_brand.setText("Polo RL");
                break;
            case R.id.nav_calvinklein:
                tv_brand.setText("Calvin Klein");
                break;
            case R.id.nav_champion:
                tv_brand.setText("Champion");
                break;
            case R.id.nav_supreme:
                tv_brand.setText("Supreme");
                break;
            case R.id.nav_american_eagle:
                tv_brand.setText("American Eagle");
                break;
            case R.id.nav_hollister:
                tv_brand.setText("Hollister");
                break;
            case R.id.nav_tommy_hilfiger:
                tv_brand.setText("Tommy Hilfiger");
                break;
            case R.id.nav_banana_republic:
                tv_brand.setText("Banana Republic");
                break;
           case R.id.nav_sp:
               tv_size.setText("28X30");
               break;
           case R.id.nav_mp:
               tv_size.setText("30X32");
               break;
           case R.id.nav_lp:
               tv_size.setText("34X36");
               break;
           case R.id.nav_xlp:
               tv_size.setText("36X38");
               break;
           case R.id.nav_5:
               tv_size.setText("Size: 5");
               break;
           case R.id.nav_6:
               tv_size.setText("Size: 6");
               break;
           case R.id.nav_7:
               tv_size.setText("Size: 7");
               break;
           case R.id.nav_8:
               tv_size.setText("Size: 8");
               break;
           case R.id.nav_9:
               tv_size.setText("Size: 9");
               break;
           case R.id.nav_10:
               tv_size.setText("Size: 10");
               break;
           case R.id.nav_11:
               tv_size.setText("Size: 11");
               break;
           case R.id.nav_12:
               tv_size.setText("Size: 12");
               break;
           case R.id.nav_13:
               tv_size.setText("Size: 13");
               break;
            }



       return false;
    }


}