package com.example.menu;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {

    EditText editTextUpdateName;
    Button btnUpdateItem, btnDeleteItem;

    String name, ID;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(String param1, String param2) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        editTextUpdateName = view.findViewById(R.id.editTextUpdateName);
        btnUpdateItem = view.findViewById(R.id.btnUpdateItem);
        btnDeleteItem = view.findViewById(R.id.btnDeleteItem);

        Bundle bundle = this.getArguments();
        ID = bundle.getString("id");
        name = bundle.getString("name");

        editTextUpdateName.setText(name);

        btnUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.updateData(ID, editTextUpdateName.getText().toString());
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TheClothesFragment()).commit();
            }
        });

        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
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