package com.example.menu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.CellSignalStrengthGsm;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "VirtualCloset.db";
    private static final int DATABASE_VERSION = 1;

    // Clothing Table
    private static final String CLOTHING_TABLE = "clothing_table";
    private static final String CLOTHING_ID = "clothing_id";
    private static final String CLOTHING_NAME = "Clothing_name";
    private static final String CLOTHING_BRAND = "clothing_brand";
    private static final String CLOTHING_SIZE = "clothing_size";
    private static final String CLOTHING_MATERIAL = "clothing_material";
    private static final String CLOTHING_DESCRIPTION = "clothing_description";
    private static final String CLOTHING_STATUS = "clothing_status";

    // Color Table, uses CLOTHING_ID as FK/PK
    private static final String COLOR_TABLE = "color_table";
    private static final String COLOR = "color";

    // Tags Table, uses CLOTHING_DI as PK?FK
    private static final String TAGS_TABLE = "tag_table";
    private static final String TAGS = "tags";

    // Outfit table
    private static final String OUTFIT_TABLE = "outfit_table";
    private static final String OUTFIT_ID = "outfit_id";
    private static final String OUTFIT_NAME = "outfit_name";

    // Outfit/clothing table
    // Uses CLOTHING_ID & OUTFIT_ID as Composite key
    private static final String OUTFIT_CLOTHING_TABLE = "outfit_clothing_table";

    // User Table
    private static String USER_TABLE = "user_table";
    private static String USER_ID = "user_id";
    private static String USER_NAME = "username";
    private static String USER_EMAIL = "user_email";
    private static String USER_PASSWORD = "user_password";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CLOTHING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public void onCreate(SQLiteDatabase db) {
        // CREATE CLOTHING TABLE
        String createTable = "CREATE TABLE " + CLOTHING_TABLE +
                " (" + CLOTHING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLOTHING_NAME + " TEXT, " +
                CLOTHING_BRAND + " TEXT, " +
                CLOTHING_MATERIAL + " TEXT, " +
                CLOTHING_SIZE + " TEXT, " +
                CLOTHING_DESCRIPTION + " TEXT, " +
                CLOTHING_STATUS + " TEXT);";
        db.execSQL(createTable);

        //CREATE COLOR TABLE
        createTable = "CREATE TABLE " + COLOR_TABLE +
                " (" + CLOTHING_ID + " INTEGER PRIMARY KEY, " +
                COLOR + " TEXT);";
        db.execSQL(createTable);

        // CREATE TAGS TABLE
        createTable = "CREATE TABLE " + TAGS_TABLE +
                " (" + CLOTHING_ID + " INTEGER PRIMARY KEY, " +
                TAGS + " TEXT);";
        db.execSQL(createTable);

        // CREATE OUTFIT TABLE
        createTable = "CREATE TABLE " + OUTFIT_TABLE +
                " (" + OUTFIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OUTFIT_NAME + " TEXT);";
        db.execSQL(createTable);

        // CREATE OUTFIT & CLOTHING TABLE
        createTable = "CREATE TABLE " + OUTFIT_CLOTHING_TABLE +
                " (" + OUTFIT_ID + " INTEGER PRIMARY KEY, " +
                CLOTHING_ID + " INTEGER, " +
                " FOREIGN KEY (" + CLOTHING_ID + ") REFERENCES " + CLOTHING_TABLE + "(" + CLOTHING_ID + "));";
        db.execSQL(createTable);

        // CREATE USER TABLE
        createTable = "CREATE TABLE " + USER_TABLE +
                " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_PASSWORD + " TEXT);";
        db.execSQL(createTable);
    }

    // ADD METHODS BEGIN HERE -----------------------------------------------------------------------------------------
    // ADD CLOTHING ITEM
    public boolean addClothing(String item, String brand, String size, String material, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOTHING_NAME, item);
        contentValues.put(CLOTHING_BRAND,brand);
        contentValues.put(CLOTHING_SIZE, size);
        contentValues.put(CLOTHING_MATERIAL, material);
        contentValues.put(CLOTHING_DESCRIPTION,desc);

        long result = db.insert(CLOTHING_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // ADD A COLOR
    public boolean addColor(String color, int clothingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOTHING_ID, clothingID);
        contentValues.put(COLOR,color);

        long result = db.insert(COLOR_TABLE,null,contentValues);

        if (result == -1) {
            return false; // DIDN'T WORK
        } else {
            return true; // WORKED
        }
    }

    // ADD A TAG
    public boolean addTag(String tag, int clothingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOTHING_ID, clothingID);
        contentValues.put(TAGS,tag);

        long result = db.insert(TAGS_TABLE,null,contentValues);

        if (result == -1) {
            return false; // DIDN'T WORK
        } else {
            return true; // WORKED
        }
    }

    // QUERIES HERE-------------------------------------------------------------------------
    // Get the last Clothing Item in the list
    // returns -1 if didn't work for some reason
    public int getLatestItem() {
        String query = "SELECT " + CLOTHING_ID + " FROM " + CLOTHING_TABLE +
                " ORDER BY " + CLOTHING_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }

    // GET ALL CLOTHES
    Cursor readAllData() {
        String query = "SELECT * FROM " + CLOTHING_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        return cursor;
    }

    void updateData(String row_id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLOTHING_NAME, name);

        long result = db.update(CLOTHING_TABLE, cv, "clothing_id=?", new String[] {row_id});
        if (result == -1)
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show();
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(CLOTHING_TABLE, "clothing_id=?", new String[] {row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CLOTHING_TABLE);
    }
}