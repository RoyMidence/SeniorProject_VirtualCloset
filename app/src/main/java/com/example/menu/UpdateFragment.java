package com.example.menu;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateFragment extends Fragment {

    EditText editTextUpdateName, editTextDescription;
    TextView textViewPants;
    FloatingActionButton floatingActionButtonUpdate, floatingActionButtonDelete;

    AutoCompleteTextView autoCompleteBrand, autoCompleteType, autoCompleteMaterial, autoCompleteGeneral,
            autoCompletePants1, autoCompletePants2, autoCompletePattern, autoCompleteColor1, autoCompleteColor2,
            autoCompleteOccasion, autoCompleteTextViewFit;

    CheckBox checkBoxSpring, checkBoxSummer, checkBoxFall, checkBoxWinter;

    // Values for autoComplete fields
    // Brand of clothing
    private static final String[] BRANDS = new String[] {
            "Champion","Supreme","Adidas", "Nike",
            "Calvin Klein"
    };

    // Type of clothing
    private static final String[] TYPES = new String[] {
            "Shirt","Pants", "Shoes", "Hat"
    };

    // Material of clothing
    private static final String[] MATERIALS = new String[] {
            "Wool", "Cotton", "Polyester", "Cotton Blend",
            "Nylon", "Latex", "Denim", "Unknown"
    };

    // Sizes of shirts
    private static final String[] SHIRTS = new String[] {
            "XS", "S", "M", "L", "XL", "XXL"
    };

    // Sizes of shirts
    private static final String[] GENERAL = new String[] {
            "ONE SIZE", "FITS ALL", "Unknown"
    };

    // Sizes of Shoes
    private static final String[] SHOES = new String[] {
            "5", "6", "7", "8", "9", "10", "11", "12",
            "13"
    };

    // Sizes of Pants
    private static final String[] PANTS = new String[] {
            "22", "24", "26", "28", "30", "32", "34",
            "36", "38", "40"
    };

    // Patterns on clothing
    private static final String[] PATTERNS = new String[] {
            "Solid", "Striped", "Dotted", "Floral",
            "Graphic", "Plaid", "Two Colors"
    };

    // Color of Clothing
    private static final String[] COLOR = new String[] {
            "Red", "Black", "Green", "Blue", "Yellow",
            "Pink", "Orange"
    };

    // Occasion for Clothing
    private static final String[] OCCASION = new String[] {
            "Formal", "Casual", "Athletic"
    };

    // Fit for Clothing
    private static final String[] FIT = new String[] {
            "Mens", "Womens", "Unisex"
    };

    String name, ID, brand, type, material, size, c1, c2, pattern, occasion, weather,fit;

    public UpdateFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        DatabaseHelper db = new DatabaseHelper(getContext());

        // Connect layout things
        textViewPants = view.findViewById(R.id.textViewPants);
        editTextUpdateName = view.findViewById(R.id.editTextUpdateName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        floatingActionButtonUpdate = view.findViewById(R.id.floatingActionButtonUpdate);
        floatingActionButtonDelete = view.findViewById(R.id.floatingActionButtonDelete);
        autoCompleteBrand = view.findViewById(R.id.autoCompleteBrand);
        autoCompleteType = view.findViewById(R.id.autoCompleteType);
        autoCompleteMaterial = view.findViewById(R.id.autoCompleteMaterial);
        autoCompleteGeneral = view.findViewById(R.id.autoCompleteGeneral);
        autoCompletePants1 = view.findViewById(R.id.autoCompletePants1);
        autoCompletePants2 = view.findViewById(R.id.autoCompletePants2);
        autoCompletePattern = view.findViewById(R.id.autoCompletePattern);
        autoCompleteColor1 = view.findViewById(R.id.autoCompleteColor1);
        autoCompleteColor2 = view.findViewById(R.id.autoCompleteColor2);
        autoCompleteOccasion = view.findViewById(R.id.autoCompleteOccasion);
        autoCompleteTextViewFit = view.findViewById(R.id.autoCompleteTextViewFit);

        // Setting up radio buttons
        checkBoxSpring = view.findViewById(R.id.checkBoxSpring);
        checkBoxSummer = view.findViewById(R.id.checkBoxSummer);
        checkBoxFall = view.findViewById(R.id.checkBoxFall);
        checkBoxWinter = view.findViewById(R.id.checkBoxWinter);

        // Get values from bundle
        Bundle bundle = this.getArguments();
        ID = bundle.getString("id");
        name = bundle.getString("name");
        brand = bundle.getString("brand");
        type = bundle.getString("type");
        fit = bundle.getString("fit");
        material = db.getClothingMaterial(ID);
        size = db.getClothingSize(ID);
        c1 = db.getClothingColor1(ID);
        c2 = db.getClothingColor2(ID);

        // Break down tag values
        pattern = db.getClothingPattern(ID);

        occasion = db.getOccasion(ID);

        // Lets have fun with weather....
        weather = db.getWeatherConditions(ID); // has all weather conditions attributed to item

        // Set value of Buttons
        if (weather.substring(0, weather.indexOf(",")).equals("8")) { // ID for All seasons
            checkBoxSpring.setChecked(true);
            checkBoxSummer.setChecked(true);
            checkBoxFall.setChecked(true);
            checkBoxWinter.setChecked(true);
        } else { // At least 3 are checked, still have to check all
            if (weather.substring(0, weather.indexOf(",")).equals("7")) {
                checkBoxFall.setChecked(true);
                weather = weather.substring(weather.indexOf(","));
                if ((weather.indexOf(",") == 0) && (weather.length() > 1)) {weather = weather.substring(weather.indexOf(",")+1);} // KEPT RUNNING INTO STRING OUT OF BOUNDS ERRORS, USING THIS TO STOP IT
            }
            if (weather.substring(0, weather.indexOf(",")).equals("6")) {
                checkBoxSummer.setChecked(true);
                weather = weather.substring(weather.indexOf(","));
                if ((weather.indexOf(",") == 0) && (weather.length() > 1)) {weather = weather.substring(weather.indexOf(",")+1);}
            }
            if (weather.substring(0, weather.indexOf(",")).equals("5")) {
                checkBoxSpring.setChecked(true);
                weather = weather.substring(weather.indexOf(","));
                if ((weather.indexOf(",") == 0) && (weather.length() > 1)) {weather = weather.substring(weather.indexOf(",")+1);}
            }
            if (weather.substring(0, weather.indexOf(",")).equals("4")) {
                checkBoxWinter.setChecked(true);
                weather = weather.substring(weather.indexOf(","));
                if ((weather.indexOf(",") == 0) && (weather.length() > 1)) {weather = weather.substring(weather.indexOf(",")+1);}
            }
        }

        // Set up buttons and autoCompletes
        editTextUpdateName.setText(name); // Name of clothing at the top

        // Clothing Description
        editTextDescription.setText(db.getClothingDescription(ID));

        // autoCompleteBrand set up
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,BRANDS);
        autoCompleteBrand.setAdapter(brandAdapter);
        autoCompleteBrand.setText(brand);

        // autoCompleteType set up
        ArrayAdapter<String> TypeAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,TYPES);
        autoCompleteType.setAdapter(TypeAdapter);
        autoCompleteType.setText(type);

        // autoCompleteMaterial set up
        ArrayAdapter<String> MaterialAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,MATERIALS);
        autoCompleteMaterial.setAdapter(MaterialAdapter);
        autoCompleteMaterial.setText(material);

        // Deciding size info to display based on type
        if (type.equalsIgnoreCase("Shirt"))
        {
            autoCompleteGeneral.setVisibility(View.VISIBLE);
            // autoCompleteShirt set up
            ArrayAdapter<String> ShirtAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,SHIRTS);
            autoCompleteGeneral.setAdapter(ShirtAdapter);
            autoCompleteGeneral.setText(size);
        }
        else if (type.equalsIgnoreCase("Shoes"))
        {
            autoCompleteGeneral.setVisibility(View.VISIBLE);
            // autoCompleteShoe set up
            ArrayAdapter<String> ShoeAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,SHOES);
            autoCompleteGeneral.setAdapter(ShoeAdapter);
            autoCompleteGeneral.setText(size);
        }
        else if (type.equalsIgnoreCase("Pants"))
        {
            autoCompletePants1.setVisibility(View.VISIBLE);
            autoCompletePants2.setVisibility(View.VISIBLE);
            textViewPants.setVisibility(View.VISIBLE);

            // Have to break down size here
            String s1, s2;
            s1 = size.substring(0,size.indexOf("X"));
            s2 = size.substring(size.indexOf("X")+1, size.indexOf("X")+3);
            // Broken string

            // Set up size auto completes
            // autoCompleteShoe set up
            ArrayAdapter<String> PantsAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,PANTS);
            autoCompletePants1.setAdapter(PantsAdapter);
            autoCompletePants1.setText(s1);
            autoCompletePants2.setAdapter(PantsAdapter);
            autoCompletePants2.setText(s2);

            // Pants size accordingly set up
        }
        else // General size, Odd type
        {
            autoCompleteGeneral.setVisibility(View.VISIBLE);
            // autoCompleteShirt set up but for general purpose
            ArrayAdapter<String> GeneralAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,GENERAL);
            autoCompleteGeneral.setAdapter(GeneralAdapter);
            autoCompleteGeneral.setText(size);

        }

        // Set up Pattern autoComplete
        ArrayAdapter<String> patternAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,PATTERNS);
        autoCompletePattern.setAdapter(patternAdapter);
        autoCompletePattern.setText(pattern);

        // Creating Color Adapter
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,COLOR);
        autoCompleteColor1.setAdapter(colorAdapter);
        autoCompleteColor2.setAdapter(colorAdapter);

        // set up colors autoCompletes. Find out if multi-colored
        if (!pattern.equals("Solid")) { // Means I gotta work with 2 colors
            autoCompleteColor2.setVisibility(View.VISIBLE);
            autoCompleteColor1.setText(c1);
            autoCompleteColor2.setText(c2);
        } else { // One Color
            autoCompleteColor1.setText(c1);
        }

        autoCompletePattern.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) { // Using to change color settings on demand
                if (!editable.toString().equalsIgnoreCase("Solid")) {
                    // Code for multi-colored
                    autoCompleteColor2.setVisibility(View.VISIBLE);
                } else {
                    // Code for single color
                    autoCompleteColor2.setVisibility(View.GONE);
                }
            }
        });

        // Set up Occasion autoComplete
        ArrayAdapter<String> occasionAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,OCCASION);
        autoCompleteOccasion.setAdapter(occasionAdapter);
        autoCompleteOccasion.setText(occasion);

        // autoCompleteMaterial set up
        ArrayAdapter<String> FitAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,FIT);
        autoCompleteTextViewFit.setAdapter(FitAdapter);
        autoCompleteTextViewFit.setText(fit);

        // UPDATE BUTTON (Save Icon)
        floatingActionButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting all values new and old
                name = editTextUpdateName.getText().toString();
                brand = autoCompleteBrand.getText().toString();
                type = autoCompleteType.getText().toString();
                fit = autoCompleteTextViewFit.getText().toString();
                material = autoCompleteMaterial.getText().toString();
                String desc = editTextDescription.getText().toString();
                pattern = autoCompletePattern.getText().toString();
                c1 = autoCompleteColor1.getText().toString();

                if (!pattern.equalsIgnoreCase("Solid")) {
                    c2 = autoCompleteColor2.getText().toString();
                } else {
                    c2 = "";
                }

                // gotta check if pants and reformat size again
                if (type.equalsIgnoreCase("Pants"))
                {
                    size = autoCompletePants1.getText().toString() + textViewPants.getText() + autoCompletePants2.getText().toString();
                }
                else { size = autoCompleteGeneral.getText().toString();} // As long as it not pants all good


                db.updateData(ID,name, brand,pattern,c1,c2, fit, type, size,material,desc); // hopefully it worked lol, updated clothing table

                // Lets update tags and colors
                // Start by delete all current values
                db.deleteClothingTags(ID);

                // Now we get and reinsert all  new values
                occasion = autoCompleteOccasion.getText().toString();
                db.addTag(occasion, Integer.parseInt(ID));

                // Yeehaw I love seasons.... Have to check radio buttons
                if (checkBoxSpring.isChecked() && checkBoxFall.isChecked() &&
                        checkBoxSummer.isChecked() && checkBoxWinter.isChecked()) { // ALL SEASONS
                    db.addTag("All", Integer.parseInt(ID));
                } else {
                    if (checkBoxSpring.isChecked())
                        db.addTag("Spring",Integer.parseInt(ID));
                    if (checkBoxSummer.isChecked())
                        db.addTag("Summer",Integer.parseInt(ID));
                    if (checkBoxFall.isChecked())
                        db.addTag("Fall",Integer.parseInt(ID));
                    if (checkBoxWinter.isChecked())
                        db.addTag("Winter",Integer.parseInt(ID));
                }


                // Changes made, job done, now we can leave
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TheClothesFragment()).commit();
            }
        });

        // DELETE BUTTON (Trash Can)
        floatingActionButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

        return view;
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.deleteClothingTags(ID);
                db.deleteOneItem(ID);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TheClothesFragment()).commit();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}