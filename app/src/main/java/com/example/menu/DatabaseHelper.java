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

    // Clothing Table, will have User_id as FK
    private static final String CLOTHING_TABLE = "clothing_table";
    private static final String CLOTHING_ID = "clothing_id";
    private static final String CLOTHING_NAME = "Clothing_name";
    private static final String CLOTHING_BRAND = "clothing_brand";
    private static final String CLOTHING_TYPE = "clothing_type";
    private static final String CLOTHING_PATTERN = "clothing_pattern";
    private static final String CLOTHING_FIT = "clothing_fit";
    private static final String CLOTHING_SIZE = "clothing_size";
    private static final String CLOTHING_COLOR_1 = "clothing_color_one";
    private static final String CLOTHING_COLOR_2 = "clothing_color_two";
    private static final String CLOTHING_MATERIAL = "clothing_material";
    private static final String CLOTHING_DESCRIPTION = "clothing_description";
    private static final String CLOTHING_STATUS = "clothing_status";

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
    private static final String USER_TABLE = "user_table";
    private static final String USER_ID = "user_id";
    private static final String  USER_NAME = "username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_KEY = "user_key"; // randomly generated key, might get used to share closet later

    // Logged-in table, only has user_id as value, will only ever have 1 entry at a time
    private static String LOGGED_USER_TABLE = "logged_user_table";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CLOTHING_TAGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OUTFIT_CLOTHING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOGGED_USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLOTHING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OUTFIT_TABLE);
        onCreate(db);
    }

    // ACTUALLY MAKING THE DB HERE
    public void onCreate(SQLiteDatabase db) {
        // CREATE USER TABLE
        String createTable = "CREATE TABLE " + USER_TABLE +
                " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT, " +
                USER_PASSWORD + " TEXT, " +
                USER_KEY + " TEXT);";
        db.execSQL(createTable);

        // CREATE LOGGED_USER_TABLE
        createTable = "CREATE TABLE " + LOGGED_USER_TABLE +
                " (" + USER_ID + " INTEGER PRIMARY KEY, " +
                "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "));";
        db.execSQL(createTable);

        // CREATE CLOTHING TABLE
        createTable = "CREATE TABLE " + CLOTHING_TABLE +
                " (" + CLOTHING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLOTHING_NAME + " TEXT, " +
                CLOTHING_BRAND + " TEXT, " +
                CLOTHING_TYPE + " TEXT, " +
                CLOTHING_PATTERN + " TEXT, " +
                CLOTHING_FIT + " TEXT, " +
                CLOTHING_SIZE + " TEXT, " +
                CLOTHING_COLOR_1 + " TEXT, " +
                CLOTHING_COLOR_2 + " TEXT, " +
                CLOTHING_MATERIAL + " TEXT, " +
                CLOTHING_DESCRIPTION + " TEXT, " +
                CLOTHING_STATUS + " TEXT, " +
                USER_ID + " INTEGER, " +
                "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "));";

        db.execSQL(createTable);

        // CREATE TAGS TABLE
        createTable = "CREATE TABLE " + TAGS_TABLE +
                " (" + TAGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TAGS + " TEXT);";
        db.execSQL(createTable);

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
                OUTFIT_NAME + " TEXT, " +
                USER_ID + " INTEGER, " +
                "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "));";;
        db.execSQL(createTable);

        // CREATE OUTFIT & CLOTHING TABLE
        createTable = "CREATE TABLE " + OUTFIT_CLOTHING_TABLE +
                " (" + OUTFIT_ID + " INTEGER, " +
                CLOTHING_ID + " INTEGER, " +
                "FOREIGN KEY (" + CLOTHING_ID + ") REFERENCES " + CLOTHING_TABLE + "(" + CLOTHING_ID + "), " +
                "FOREIGN KEY (" + OUTFIT_ID + ") REFERENCES " + OUTFIT_TABLE + "(" + OUTFIT_ID + "), " +
                "PRIMARY KEY (" + OUTFIT_ID + ", " + CLOTHING_ID + "));";
        db.execSQL(createTable);
    }

    // ADD METHODS BEGIN HERE -----------------------------------------------------------------------------------------
    // ADD CLOTHING ITEM
    public boolean addClothing(String item, String brand, String pattern,
                               String c1, String c2, String fit, String type, String size, String material, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOTHING_NAME, item);
        contentValues.put(CLOTHING_BRAND,brand);
        contentValues.put(CLOTHING_TYPE, type);
        contentValues.put(CLOTHING_PATTERN, pattern);
        contentValues.put(CLOTHING_FIT, fit);
        contentValues.put(CLOTHING_SIZE, size);
        contentValues.put(CLOTHING_COLOR_1, c1);
        contentValues.put(CLOTHING_COLOR_2,c2);
        contentValues.put(CLOTHING_MATERIAL, material);
        contentValues.put(CLOTHING_DESCRIPTION,desc);
        contentValues.put(CLOTHING_STATUS,"AVAILABLE");
        contentValues.put(USER_ID, loggedUserID());

        long result = db.insert(CLOTHING_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // METHOD FOR CREATING AN OUTFIT
    public boolean addOutfit(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTFIT_NAME, name);
        contentValues.put(USER_ID, loggedUserID());

        long result = db.insert(OUTFIT_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // METHOD FOR ADDING CLOTHING TO AN OUTFIT
    public boolean addClothingToOutfit(int clothingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTFIT_ID, getLatestOutfit());
        contentValues.put(CLOTHING_ID, clothingID);

        long result = db.insert(OUTFIT_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // ADD A USER
    public boolean addUser(String userName, String password, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_PASSWORD,password);
        contentValues.put(USER_KEY,key);


        long result = db.insert(USER_TABLE,null,contentValues);

        if (result == -1) {
            return false; // DIDN'T WORK
        } else {
            return true; // WORKED
        }
    }

    // LOG-IN USER
    public boolean logginUser(String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, userID);


        long result = db.insert(LOGGED_USER_TABLE,null,contentValues);
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

    public int getLatestOutfit() {
        String query = "SELECT " + OUTFIT_ID + " FROM " + OUTFIT_TABLE +
                " ORDER BY " + OUTFIT_ID + " DESC LIMIT 1";
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

    // CHECK IF USER TABLE EMPTY
    public boolean userTableEmpty() {
        String query = "SELECT COUNT(*) FROM " + USER_TABLE;
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

    // CHECK IF LOGGED USER TABLE EMPTY
    public boolean loggedUserTableEmpty() {
        String query = "SELECT COUNT(*) FROM " + LOGGED_USER_TABLE;
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

    // QUERIES FOR CHECKING INDIVIDUAL SEASONS
    public boolean checkWinter(String clothingID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + CLOTHING_TAGS_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID +
                " AND " + TAGS_ID + " = 4";

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return false;
        }
        return true;
    }

    public boolean checkSpring(String clothingID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + CLOTHING_TAGS_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID +
                " AND " + TAGS_ID + " = 5";

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return false;
        }
        return true;
    }

    public boolean checkSummer(String clothingID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + CLOTHING_TAGS_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID +
                " AND " + TAGS_ID + " = 6";

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return false;
        }
        return true;
    }
    public boolean checkFall(String clothingID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + CLOTHING_TAGS_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID +
                " AND " + TAGS_ID + " = 7";

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return false;
        }
        return true;
    }

    public boolean checkAll(String clothingID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + CLOTHING_TAGS_TABLE +
                " WHERE " + CLOTHING_ID + " = " + clothingID +
                " AND " + TAGS_ID + " = 8";

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return false;
        }
        return true;
    }

    // GET ALL CLOTHES
    Cursor readUsersClothing(String user_ID) {
        String query = "SELECT * FROM " + CLOTHING_TABLE +
                " WHERE " + USER_ID + " = " + user_ID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        return cursor;
    }

    // GET ID OF LOGGED USER
    String loggedUserID() {
        String query = "SELECT " + USER_ID + " FROM " + LOGGED_USER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }

    public String getClothingColor1(String clothingID) {
        String result = "DIDN'T WORK!";
        String Query = "SELECT " + CLOTHING_COLOR_1 +
                " FROM " + CLOTHING_TABLE +
                " WHERE " +  CLOTHING_ID + " = " + clothingID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(Query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0);
            }
            return result;
        }

        return result;
    }

    public String getClothingColor2(String clothingID) {
        String result = "DIDN'T WORK!";
        String Query = "SELECT " + CLOTHING_COLOR_2 +
                " FROM " + CLOTHING_TABLE +
                " WHERE " +  CLOTHING_ID + " = " + clothingID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(Query, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0);
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
                " AND (" + TAGS_ID + " BETWEEN 4 AND 8) " +
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

    public String getOccasion(String clothingID) {
        String result = "DIDNT WORK!";
        String query = "SELECT " + TAGS_ID +
                " FROM " + CLOTHING_TAGS_TABLE +
                " WHERE (" + CLOTHING_ID + " = " + clothingID + ") " +
                " AND (" + TAGS_ID + " BETWEEN 1 AND 3) " +
                " ORDER BY " + TAGS_ID + " DESC";
        String subQuery = "SELECT " + TAGS +
                " FROM " + TAGS_TABLE +
                " WHERE " + TAGS_ID + " IN (" + query + ")";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(subQuery, null);
            result = "";
            while (cursor.moveToNext()) {
                result += cursor.getString(0);
            }
            return result;
        }
        return result;
    }

    public String getClothingPattern(String clothingID) {
        String result = "FAILED!!";
        String query = "SELECT " + CLOTHING_PATTERN + " FROM " + CLOTHING_TABLE +
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

    public String getClothingFit(String clothingID) {
        String result = "FAILED!!";
        String query = "SELECT " + CLOTHING_FIT + " FROM " + CLOTHING_TABLE +
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

    void updateData(String row_id, String name,String brand, String pattern, String c1, String c2, String fit, String type, String size, String material, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLOTHING_NAME, name);
        cv.put(CLOTHING_BRAND, brand);
        cv.put(CLOTHING_PATTERN,pattern);
        cv.put(CLOTHING_COLOR_1, c1);
        cv.put(CLOTHING_COLOR_2,c2);
        cv.put(CLOTHING_FIT,fit);
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

    // DELETES --------------------------------------------------------------------------------------------------------------------------------------

    // Delete one clothing item
    void deleteOneItem(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(CLOTHING_TABLE, "clothing_id=?", new String[] {row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete all of a specified users closet
    void deleteUserCloset(String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CLOTHING_TABLE +
                " WHERE " + USER_ID + " = " + user_id);
    }

    // Delete all the tags associated to a specific clothing item, used mostly for updating
    void deleteClothingTags(String clothingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(CLOTHING_TAGS_TABLE, "clothing_id=?", new String[] {clothingID});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
        }
    }
}