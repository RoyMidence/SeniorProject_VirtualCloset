package com.example.menu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UpdateOutfit extends AppCompatActivity implements NameAdapter.itemClickInterface {
    String outfitID, outfitName;
    int outfitPosition;

    private EditText editTextOutfitName;
    private Button buttonAddToOutfit;
    private FloatingActionButton fabUpdateButton, fabDeleteButton;
    private ArrayList<String> list = new ArrayList<>();


    private RecyclerView recyclerViewUpdateClothing;
    private NameAdapter NameAdapter;
    private List<ClothingItem> outfitClothing = new ArrayList<>();

    ActivityResultLauncher<Intent> otherActivityLauncher;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_outfit);

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        editTextOutfitName = findViewById(R.id.editTextOutfitName);
        buttonAddToOutfit = findViewById(R.id.buttonAddToOutfit);

        outfitName=getIntent().getExtras().getString("outfitname");
        outfitPosition = getIntent().getExtras().getInt("itemposition");
        outfitID = getIntent().getExtras().getString("outfitID");
        list = getIntent().getExtras().getStringArrayList("namelist");
        configureButtons();
        setUpRecycler();

        otherActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 0) {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                mDatabaseHelper.addClothingToOutfit(resultIntent.getExtras().getString("id"));
                                setUpRecycler();
                            }
                        }
                    }
                });



        buttonAddToOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateOutfit.this, SelectClothes.class);
                intent.putExtra("type","all");
                otherActivityLauncher.launch(intent);
            }
        });



    }

    private void setUpRecycler() {
        recyclerViewUpdateClothing = findViewById(R.id.recyclerViewUpdateClothing);
        storeValuesInArrays();

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerViewUpdateClothing.setLayoutManager(layoutManager);
        NameAdapter = new NameAdapter(outfitClothing, this);
        recyclerViewUpdateClothing.setAdapter(NameAdapter);
    }

    private void configureButtons(){
    fabUpdateButton= findViewById(R.id.floatingActionButtonUpdateOutfit);
    fabDeleteButton= findViewById(R.id.floatingActionButtonDeleteOutfit);
    editTextOutfitName.setText(outfitName);
        fabUpdateButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            outfitName = editTextOutfitName.getText().toString();
        if(mDatabaseHelper.checkOutfitTypes(outfitID,"Shirt")&&
            mDatabaseHelper.checkOutfitTypes(outfitID,"Pants")&&
            mDatabaseHelper.checkOutfitTypes(outfitID,"Shoes")){
            Intent resultIntent = new Intent();
            setResult(0,resultIntent);

            mDatabaseHelper.updateOutfit(outfitID,outfitName);
            finish();
        }else{
            toastMessage("you need shirt, pants and shoes");
        }

        }
    });
    fabDeleteButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDatabaseHelper.deleteOutfit(outfitID);
            Intent resultIntent = new Intent();
            setResult(0,resultIntent);
            finish();


        }
    });


}
    private void toastMessage(String Message) {
        Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_SHORT).show();
    }
    private void storeValuesInArrays() {
        ClothingItem CI;
        outfitClothing.clear();


        Cursor cursor = mDatabaseHelper.readOutfitClothing(outfitID);
        while (cursor.moveToNext()) {
            // CLOTHING TABLE:  clothingID  : NAME  : BRAND : TYPE  : PATTERN : FIT : SIZE : COLOR1 : COLOR2 : MATERIAL : DESC : STATUS : userID
            String id = cursor.getString(0);
            CI = new ClothingItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),
                    cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),
                    mDatabaseHelper.getOccasion(id), mDatabaseHelper.checkSpring(id), mDatabaseHelper.checkSummer(id), mDatabaseHelper.checkFall(id), mDatabaseHelper.checkWinter(id), mDatabaseHelper.checkAll(id));
            outfitClothing.add(CI);
        }
    }

    @Override
    public void onItemClick(int position) {
        mDatabaseHelper.deleteClothingItemFromOutfit(outfitID,String.valueOf(outfitClothing.get(position).getClothingID()));
        storeValuesInArrays();
        NameAdapter.setData(outfitClothing);


    }

    void confirmDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Remove " + outfitClothing.get(pos).getName() + "From Outfit?");
        builder.setMessage("Are you sure you want to remove " + outfitClothing.get(pos).getName() + "from this outfit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Data to delete
                mDatabaseHelper.deleteClothingItemFromOutfit(outfitID,String.valueOf(outfitClothing.get(pos).getClothingID()));
                storeValuesInArrays();
                NameAdapter.setData(outfitClothing);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    void confirmDeleteDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Delete " + outfitName + " ?");
        builder.setMessage("Are you sure you want to delete " + outfitName + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Data to delete
            mDatabaseHelper.deleteOutfit(outfitID);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("pos", pos);
                setResult(1,resultIntent);
                finish();
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
