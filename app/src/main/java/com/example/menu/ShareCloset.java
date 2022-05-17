package com.example.menu;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShareCloset extends Fragment implements UserAdapter.itemClickInterface{
    DatabaseHelper mDatabaseHelper;

    TextView textViewUserKey;
    EditText editTextEnterKey;
    Button buttonEnterKey;


    RecyclerView recyclerView;
    private List<String> userName = new ArrayList<>();
    private List<String> userID = new ArrayList<>();
    private UserAdapter list;

    public ShareCloset() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_closet, container, false);

        textViewUserKey = view.findViewById(R.id.textViewUserKey);
        editTextEnterKey = view.findViewById(R.id.editTextEnterKey);
        buttonEnterKey = view.findViewById(R.id.buttonEnterKey);

        mDatabaseHelper = new DatabaseHelper(getContext());

        // This is the users own key, send this to friend
        textViewUserKey.setText(mDatabaseHelper.getUserKey());

        //setUpRecycler(view);

        // They hit enter on the button
        // check the entered key and make sure it isn't null
        buttonEnterKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEnterKey.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(),"Please Enter a Key",Toast.LENGTH_SHORT).show();
                } else if (mDatabaseHelper.shareCloset(editTextEnterKey.getText().toString())) {
                    Toast.makeText(getContext(),"Key Successfully Entered",Toast.LENGTH_SHORT).show();
                    //storeValuesInArrays();
                    //list.setData(userName,userID);
                } else {
                    Toast.makeText(getContext(),"Invalid Key",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void setUpRecycler(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAllowedUsers);
        storeValuesInArrays();

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        list = new UserAdapter(userName, userID, this);
        recyclerView.setAdapter(list);
    }

    private void storeValuesInArrays() {
        userName.clear();
        userID.clear();
        Cursor cursor = mDatabaseHelper.readSharedUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                userName.add(cursor.getString(0));
                userID.add(cursor.getString(1));
            }
        }
        cursor.close();
    }

    @Override
    public void onItemClick(int position) {
        confirmDialog(position);
    }

    void confirmDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Stop sharing with " + userName.get(pos));
        builder.setMessage("Are you sure you want to stop sharing your closet with " + userName.get(pos) + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseHelper.stopSharing(userID.get(pos));
                storeValuesInArrays();
                list.setData(userName,userID);
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