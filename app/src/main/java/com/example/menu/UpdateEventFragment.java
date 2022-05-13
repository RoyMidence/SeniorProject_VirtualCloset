package com.example.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class UpdateEventFragment extends Fragment implements OutfitNameAdapter.itemClickInterface {
String title,id,location,start,end;
private DatabaseHelper mDatabaseHelper;
private View v;
    private RecyclerView eventOutfit, eventClothes ;

    private List<ClothingItem> clothingItems = new ArrayList<>();
    private ArrayList<String> clothingName = new ArrayList<>();
    private ArrayList<String> clothingID = new ArrayList<>();
    private ArrayList<String> outfitName = new ArrayList<>();
    private ArrayList<String> outfitID = new ArrayList<>();

    ActivityResultLauncher<Intent> otherActivityLauncher;
    TextView startEdit, endEdit, emptyClothes, emptyOutfits;
    EditText editTitle, editlocation;
    public UpdateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         v=inflater.inflate(R.layout.fragment_update_event, container, false);
        Bundle bundle = this.getArguments();
         editTitle = v.findViewById(R.id.editTextEditEventTitle);
         editlocation = v.findViewById(R.id.editTextEditEventLocation);
         startEdit = v.findViewById(R.id.textViewEditEventStart);
         endEdit = v.findViewById(R.id.textViewEditEventEnd);
         emptyClothes = v.findViewById(R.id.textViewEventClothesEmpty);
         emptyOutfits = v.findViewById(R.id.textViewEventOutfitsEmpty);

        eventClothes  =v.findViewById(R.id.editEventsClothesRecycle);
        eventOutfit = v.findViewById(R.id.editEventOutfitRecycle);
        mDatabaseHelper = new DatabaseHelper(getContext());

        id = bundle.getString("id");
        title= bundle.getString("title");
        location = bundle.getString("location");
        start = bundle.getString("start");
        end = bundle.getString("end");
        System.out.println(id);
        System.out.println(title);
        System.out.println(location);
        System.out.println(start);
        System.out.println(end);
        editTitle.setText(title);

        editlocation.setText(location);
        startEdit.setText(start);
        endEdit.setText(end);


        configureButtons(v);
        setUpRecycler();
        storeValuesInArrays(id);

        otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 2) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {

                                String type = resultIntent.getStringExtra("type");
                                if(type.equals("NONE")){
                                    mDatabaseHelper.addOutfitToEvent(String.valueOf(resultIntent.getStringExtra("id")));

                                }else{
                                    mDatabaseHelper.addClothingToEvent(String.valueOf(resultIntent.getStringExtra("id")));
                                }
                                setUpRecycler();

                            }
                        }
                        if (result.getResultCode() == 0) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {
                                String date = resultIntent.getStringExtra("date");
                                startEdit.setText(date);

                            }
                        }
                        if (result.getResultCode() == 1) {
                            Intent resultIntent = result.getData(); //  Just getting whatever information is sent back
                            if (resultIntent != null) {
                                String date = resultIntent.getStringExtra("date");
                                endEdit.setText(date);

                            }
                        }
                    }
                });
        return v;
    }


    private void configureButtons(View v){
        FloatingActionButton updateButton = v.findViewById(R.id.floatingActionButtonUpdateEdit);
        FloatingActionButton deleteButtton = v.findViewById(R.id.floatingActionButtonDeleteEdit);
        FloatingActionButton addClothes = v.findViewById(R.id.fabAddEditEventClothes);
        FloatingActionButton addOutfits = v.findViewById(R.id.fabAddEditEventsOutfits);

        startEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(getContext(),StartCalendar.class);
                otherActivityLauncher.launch(go);
            }
        });
        endEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(getContext(), EndCalendar.class);
                go.putExtra("date", String.valueOf(startEdit.getText()));
                otherActivityLauncher.launch(go);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = String.valueOf(startEdit.getText());
                String date = String.valueOf(endEdit.getText());
                String startMonth = startDate.substring(0,2);
                String endMonth = date.substring(0,2);
                String startDay = startDate.substring(3,5);
                String endDay = date.substring(3,5);
                String startYear =startDate.substring(6);
                String endYear = date.substring(6);


                // compares if start year comes before or after end year
                if(startYear.compareTo(endYear)<=0){
                    if(startMonth.compareTo(endMonth)<0) {

                        mDatabaseHelper.updateEvent(id,String.valueOf(editTitle.getText()),String.valueOf(editlocation.getText()),
                                String.valueOf(startEdit.getText()),String.valueOf(endEdit.getText()));
                    }
                    else if(startMonth.compareTo(endMonth)==0){
                        if(startDay.compareTo(endDay)>0) {
                            toastMessage("Invalid End Date");
                        }
                        else {

                            mDatabaseHelper.updateEvent(id,String.valueOf(editTitle.getText()),String.valueOf(editlocation.getText()),
                                    String.valueOf(startEdit.getText()),String.valueOf(endEdit.getText()));
                        }
                    }
                    else{
                        toastMessage("Invalid End Date");
                    }

                }else{
                    toastMessage("Invalid End Date");
                }


            }
        });
        deleteButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(getActivity(),SelectClothesEvents.class);
                otherActivityLauncher.launch(go);
            }
        });
        addOutfits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(getActivity(),SelectOutfitEvents.class);
                otherActivityLauncher.launch(go);
            }
        });
    }
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete This "+ title +" event?");
        builder.setMessage("Are you sure you want to delete ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(clothingID.isEmpty()){

                }else{
                    mDatabaseHelper.deleteAllClothingFromEvent(id);
                }
                if(outfitID.isEmpty()){

                }else{
                    mDatabaseHelper.deleteAllOutfitFromEvent(id);
                }
                mDatabaseHelper.deleteOneEvent(id);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EventFragment()).commit();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    private void storeValuesInArrays(String ID) {
        ClothingItem CI;
        clothingItems.clear();
        clothingName.clear();
        clothingID.clear();
        outfitName.clear();
        outfitID.clear();


        Cursor cursor = mDatabaseHelper.readEventClothing(ID);
        if (cursor.getCount() == 0) {
            emptyClothes.setVisibility(View.VISIBLE);

        } else {
            emptyClothes.setVisibility(View.GONE);


            while (cursor.moveToNext()) {

                String id = cursor.getString(0);
                CI = new ClothingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13),
                        mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id), mDatabaseHelper.getClothingFave(id));
                clothingItems.add(CI);
                clothingName.add(cursor.getString(1));
                clothingID.add(cursor.getString(0));
            }
            cursor.close();
        }
        Cursor cursor2 = mDatabaseHelper.readEventOutfit(ID);
        if (cursor2.getCount() == 0) {

            emptyOutfits.setVisibility(View.VISIBLE);
        } else {

            emptyOutfits.setVisibility(View.GONE);

            while (cursor2.moveToNext()) {
                outfitID.add(cursor2.getString(0));
                outfitName.add(cursor2.getString(1));
            }
            cursor2.close();
        }
    }
    private void setUpRecycler() {
            storeValuesInArrays(id);
        eventClothes.setLayoutManager(new LinearLayoutManager(getContext()));
        OutfitNameAdapter list = new OutfitNameAdapter(getContext(),clothingName,clothingID,this);
        eventClothes.setAdapter(list);

        eventOutfit.setLayoutManager(new LinearLayoutManager(getContext()));
        OutfitNameAdapter list2 = new OutfitNameAdapter(getContext(),outfitName,outfitID,this);
        eventOutfit.setAdapter(list2);

    }

    void confirmDeleteDialogOutfit(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this item from the event?");
        builder.setMessage("Are you sure you want to delete "+ outfitName.get(pos)+"?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabaseHelper.deleteOutfitItemFromEvent(id,outfitID.get(pos));
                setUpRecycler();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    void confirmDeleteDialogClothing(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this item from the event?");
        builder.setMessage("Are you sure you want to delete "+ clothingName.get(pos)+"?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabaseHelper.deleteClothingItemFromEvent(id,clothingID.get(pos));
                setUpRecycler();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onItemClick(ArrayList<String> name ,int position) {
// name is the names of the items in the recycler view.\
        if(name.size() == clothingName.size()) {
            for (int i = 0; i < name.size(); i++) {
                if (name.get(i).equals(clothingName.get(position))) {
                    confirmDeleteDialogClothing(position);
                }
            }
        }
        if(name.size() == outfitName.size()){
            for(int k = 0; k < name.size();k++){
                if(name.get(k).equals(outfitName.get(position))){
                        confirmDeleteDialogOutfit(position);
                    }

                }
            }
    }

    private void toastMessage(String Message) {
        Toast.makeText(getContext(),Message,Toast.LENGTH_LONG).show();
    }
}
