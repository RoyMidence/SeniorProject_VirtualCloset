package com.example.menu;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateFragment extends Fragment {

    EditText editTextUpdateName, editTextDescription;
    TextView textViewPants;
    FloatingActionButton floatingActionButtonUpdate, floatingActionButtonDelete;

    AutoCompleteTextView autoCompleteBrand, autoCompleteType, autoCompleteMaterial, autoCompleteGeneral,
            autoCompletePants1, autoCompletePants2;

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

    String name, ID, brand, type, material, size;

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

        // Get values from bundle
        Bundle bundle = this.getArguments();
        ID = bundle.getString("id");
        name = bundle.getString("name");
        brand = bundle.getString("brand");
        type = bundle.getString("type");
        material = db.getClothingMaterial(ID);
        size = db.getClothingSize(ID);

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
        else
        {
            autoCompleteGeneral.setVisibility(View.VISIBLE);
            // autoCompleteShirt set up but for general purpose
            ArrayAdapter<String> GeneralAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,GENERAL);
            autoCompleteGeneral.setAdapter(GeneralAdapter);
            autoCompleteGeneral.setText(size);

        }

        // UPDATE BUTTON (Save Icon)
        floatingActionButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting all values new and old
                name = editTextUpdateName.getText().toString();
                brand = autoCompleteBrand.getText().toString();
                type = autoCompleteType.getText().toString();
                material = autoCompleteMaterial.getText().toString();
                String desc = editTextDescription.getText().toString();

                // gotta check if pants and reformat size again
                if (type.equalsIgnoreCase("Pants"))
                {
                    size = autoCompletePants1.getText().toString() + textViewPants.getText() + autoCompletePants2.getText().toString();
                }
                else { size = autoCompleteGeneral.getText().toString();} // As long as it not pants all good


                db.updateData(ID,name, brand, type, size,material,desc); // hopefully it worked lol

                // reset fragment before we leave
                autoCompleteGeneral.setVisibility(View.GONE);
                autoCompletePants1.setVisibility(View.GONE);
                textViewPants.setVisibility(View.GONE);
                autoCompletePants2.setVisibility(View.GONE);

                // Changes made, job done, now we can leave
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
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
                db.deleteOneRow(ID);
                // reset fragment before we leave
                autoCompleteGeneral.setVisibility(View.GONE);
                autoCompletePants1.setVisibility(View.GONE);
                textViewPants.setVisibility(View.GONE);
                autoCompletePants2.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
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