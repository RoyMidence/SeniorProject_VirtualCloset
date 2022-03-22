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
    private static final String CLOTHING_TYPE = "clothing_type";
    private static final String CLOTHING_SIZE = "clothing_size";
    private static final String CLOTHING_MATERIAL = "clothing_material";
    private static final String CLOTHING_DESCRIPTION = "clothing_description";
    private static final String CLOTHING_STATUS = "clothing_status";

    // Color Table
    private static final String COLOR_TABLE = "color_table";
    private static final String COLOR_ID = "color_id";
    private static final String COLOR = "color";

    // color & clothing table, uses composite key from COLOR_ID & CLOTHING_ID
    private static final String CLOTHING_COLOR_TABLE = "clothing_color_table";

    // Tags Table
    private static final String TAGS_TABLE = "tag_table";
    private static final String TAGS_ID = "tags_id";
    private static final String TAGS = "tags";

    // tags & clothing table, uses TAGS_ID and CLOTHING_DI as composite key
    private static final String CLOTHING_TAGS_TABLE = "clothing_tags_table";

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
        db.execSQL("DROP TABLE IF EXISTS " + CLOTHING_COLOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLOTHING_TAGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OUTFIT_CLOTHING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLOTHING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OUTFIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    // ACTUALLY MAKING THE DB HERE
    public void onCreate(SQLiteDatabase db) {
        // CREATE CLOTHING TABLE
        String createTable = "CREATE TABLE " + CLOTHING_TABLE +
                " (" + CLOTHING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLOTHING_NAME + " TEXT, " +
                CLOTHING_BRAND + " TEXT, " +
                CLOTHING_TYPE + " TEXT, " +
                CLOTHING_MATERIAL + " TEXT, " +
                CLOTHING_SIZE + " TEXT, " +
                CLOTHING_DESCRIPTION + " TEXT, " +
                CLOTHING_STATUS + " TEXT);";
        db.execSQL(createTable);

        //CREATE COLOR TABLE
        createTable = "CREATE TABLE " + COLOR_TABLE +
                " (" + COLOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLOR + " TEXT);";
        db.execSQL(createTable);

        // MINI TABLE BETWEEN CLOTHING AND COLOR
        createTable = "CREATE TABLE " + CLOTHING_COLOR_TABLE +
                " (" + CLOTHING_ID + " INTEGER, " +
                COLOR_ID + " INTEGER, " +
                "FOREIGN KEY (" + CLOTHING_ID + ") REFERENCES " + CLOTHING_TABLE + "(" + CLOTHING_ID + "), " +
                "FOREIGN KEY (" + COLOR_ID + ") REFERENCES " + COLOR_TABLE + "(" + COLOR_ID + "), " +
                "PRIMARY KEY (" + CLOTHING_ID + ", " + COLOR_ID + "));";
        db.execSQL(createTable);

        // CREATE TAGS TABLE
        createTable = "CREATE TABLE " + TAGS_TABLE +
                " (" + TAGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TAGS + " TEXT);";
        db.execSQL(createTable);

        /*
        // ADDING VALUES TO TAGS TABLE

        */

        //MINI TABLE BETWEEN CLOTHING AND TAGS
        createTable = "CREATE TABLE " + CLOTHING_TAGS_TABLE +
                " (" + CLOTHING_ID + " INTEGER, " +
                TAGS_ID + " INTEGER, " +
                "FOREIGN KEY (" + CLOTHING_ID + ") REFERENCES " + CLOTHING_TABLE + "(" + CLOTHING_ID + "), " +
                "FOREIGN KEY (" + TAGS_ID + ") REFERENCES " + TAGS_TABLE + "(" + TAGS_ID + "), " +
                "PRIMARY KEY (" + CLOTHING_ID + ", " + TAGS_ID + "));";
        db.execSQL(createTable);

        // CREATE OUTFIT TABLE
        createTable = "CREATE TABLE " + OUTFIT_TABLE +
                " (" + OUTFIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OUTFIT_NAME + " TEXT);";
        db.execSQL(createTable);

        // CREATE OUTFIT & CLOTHING TABLE
        createTable = "CREATE TABLE " + OUTFIT_CLOTHING_TABLE +
                " (" + OUTFIT_ID + " INTEGER, " +
                CLOTHING_ID + " INTEGER, " +
                "FOREIGN KEY (" + CLOTHING_ID + ") REFERENCES " + CLOTHING_TABLE + "(" + CLOTHING_ID + "), " +
                "FOREIGN KEY (" + OUTFIT_ID + ") REFERENCES " + OUTFIT_TABLE + "(" + OUTFIT_ID + "), " +
                "PRIMARY KEY (" + OUTFIT_ID + ", " + CLOTHING_ID + "));";
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
    public boolean addClothing(String item, String brand, String type, String size, String material, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOTHING_NAME, item);
        contentValues.put(CLOTHING_BRAND,brand);
        contentValues.put(CLOTHING_TYPE, type);
        contentValues.put(CLOTHING_SIZE, size);
        contentValues.put(CLOTHING_MATERIAL, material);
        contentValues.put(CLOTHING_DESCRIPTION,desc);
        contentValues.put(CLOTHING_STATUS,"AVAILABLE");

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
        contentValues.put(COLOR_ID,getColorID(color));

        long result = db.insert(CLOTHING_COLOR_TABLE,null,contentValues);

        if (result == -1) {
            return false; // DIDN'T WORK
        } else {
            return true; // WORKED
        }
    }

    // ADD COLORS TO TABLE
    public boolean addToColorTable(String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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
        contentValues.put(TAGS_ID,getTagID(tag));

        long result = db.insert(CLOTHING_TAGS_TABLE,null,contentValues);

        if (result == -1) {
            return false; // DIDN'T WORK
        } else {
            return true; // WORKED
        }
    }

    // ADD TAGS TO TABLE
    public boolean addToTagsTable(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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

    // GET CLOTHING DESCRIPTION
    public String getClothingDescription(String clothingID) {
        String result = "FAILED!!";
        String query = "SELECT " + CLOTHING_DESCRIPTION + " FROM " + CLOTHING_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0);
            }
            return result;
        }
        return result;
    }

    // GET CLOTHING SIZE
    public String getClothingSize(String clothingID) {
        String result = "FAILED!!";
        String query = "SELECT " + CLOTHING_SIZE + " FROM " + CLOTHING_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0);
            }
            return result;
        }
        return result;
    }

    // GET CLOTHING MATERIAL
    public String getClothingMaterial(String clothingID) {
        String result = "FAILED!!";
        String query = "SELECT " + CLOTHING_MATERIAL + " FROM " + CLOTHING_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0);
            }
            return result;
        }
        return result;
    }

    // GET COLOR ID
    public int getColorID(String color) {
        String query = "SELECT * FROM " + COLOR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        while (cursor.moveToNext()) {
            if (cursor.getString(1).equalsIgnoreCase(color))
                return cursor.getInt(0);
        }
        return -1;
    }

    // GET TAGS ID
    public int getTagID(String tag) {
        String query = "SELECT * FROM " + TAGS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(1).equalsIgnoreCase(tag))
                    return cursor.getInt(0);
            }
        }

        return -1;
    }

    // CHECK IF TAGS TABLE EMPTY
    public boolean clothingTableEmpty() {
        String query = "SELECT COUNT(*) FROM " + CLOTHING_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                return true;
            }
        }
        return false;
    }

    // CHECK IF TAGS TABLE EMPTY
    public boolean tagTableEmpty() {
        String query = "SELECT COUNT(*) FROM " + TAGS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                return true;
            }
        }
        return false;
    }

    // CHECK IF COLOR TABLE EMPTY
    public boolean colorTableEmpty() {
        String query = "SELECT COUNT(*) FROM " + COLOR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                return true;
            }
        }
        return false;
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

    // GET CLOTHING COLOR
    public String getClothingColor(String clothingID) {
        String result = "DIDN'T WORK!";
        String Query = "SELECT " + COLOR +
                " FROM " + COLOR_TABLE +
                " INNER JOIN " + CLOTHING_COLOR_TABLE +
                " ON " + COLOR_TABLE + "." + COLOR_ID + " = " + CLOTHING_COLOR_TABLE + "." + COLOR_ID +
                " WHERE " + CLOTHING_COLOR_TABLE + "." + CLOTHING_ID + " = " + clothingID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(Query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0) + ", ";
            }
            return result;
        }

        return result;
    }

    // GET CLOTHING TAGS
    public String getClothingTags(String clothingID) {
        String result = "DIDN'T WORK!";
        String Query = "SELECT " + TAGS +
                " FROM " + TAGS_TABLE +
                " INNER JOIN " + CLOTHING_TAGS_TABLE +
                " ON " + TAGS_TABLE + "." + TAGS_ID + " = " + CLOTHING_TAGS_TABLE + "." + TAGS_ID +
                " WHERE " + CLOTHING_TAGS_TABLE + "." + CLOTHING_ID + " = " + clothingID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(Query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0) + ", ";
            }
            return result;
        }

        return result;
    }

    // GET CLOTHING WEATHER CONDITIONS
    public String getWeatherConditions(String clothingID) {
        String result = "DIDNT WORK!";
        String query = "SELECT " + TAGS_ID +
                " FROM " + CLOTHING_TAGS_TABLE +
                " WHERE (" + CLOTHING_ID + " = " + clothingID + ") " +
                " AND (" + TAGS_ID + " BETWEEN 6 AND 10) " +
                " ORDER BY " + TAGS_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0) + ",";
            }
            return result;
        }
        return result;
    }

    void updateData(String row_id, String name,String brand, String type, String size, String material, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLOTHING_NAME, name);
        cv.put(CLOTHING_BRAND, brand);
        cv.put(CLOTHING_TYPE, type);
        cv.put(CLOTHING_SIZE, size);
        cv.put(CLOTHING_MATERIAL, material);
        cv.put(CLOTHING_DESCRIPTION, desc);

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