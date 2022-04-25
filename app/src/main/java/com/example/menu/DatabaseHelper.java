package com.example.menu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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
    private static final String DATE_CREATED="date_created";

    // Shared Closet Table
    private static final String SHARED_CLOSET_TABLE = "shared_closet_table";
    private static final String CLOSET_OWNER = "closet_owner";
    private static final String ALLOWED_USER = "allowed_user";

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
    private static final String OUTFIT_DATE = "outfit_date";

    // Outfit/clothing table
    // Uses CLOTHING_ID & OUTFIT_ID as Composite key
    private static final String OUTFIT_CLOTHING_TABLE = "outfit_clothing_table";

    // User Table
    private static final String USER_TABLE = "user_table";
    private static final String USER_ID = "user_id";
    private static final String USER_FULLNAME = "user_fullname";
    private static final String USER_NAME = "username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_KEY = "user_key";// randomly generated key, might get used to share closet later
    private static final String User_Hot = "user_hot";
    private static final String User_Cold="user_cold";
    private static final String User_Freezing="user_freezing";
    private static final String User_Warm="user_warm";

    // Logged-in table, only has user_id as value, will only ever have 1 entry at a time
    private static final String LOGGED_USER_TABLE = "logged_user_table";

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
                USER_FULLNAME + " TEXT, " +
                USER_NAME + " TEXT, " +
                USER_PASSWORD + " TEXT, " +
                USER_KEY + " TEXT, " +
                User_Hot + " TEXT, " +
                User_Freezing + " TEXT, " +
                User_Warm + " TEXT, " +
                User_Cold + " TEXT);";
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
                DATE_CREATED + " TEXT, " +
                USER_ID + " INTEGER, " +
                "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "));";

        db.execSQL(createTable);

        // CREATE SHARED CLOSET
        createTable = "CREATE TABLE " + SHARED_CLOSET_TABLE +
                " (" + CLOSET_OWNER + "  INTEGER, " +
                ALLOWED_USER + " INTEGER, " +
                "FOREIGN KEY ("+ CLOSET_OWNER +") REFERENCES " + USER_TABLE + "(" + USER_ID + "), " +
                "FOREIGN KEY ("+ ALLOWED_USER +") REFERENCES " + USER_TABLE + "(" + USER_ID + "), " +
                "PRIMARY KEY ("+ CLOSET_OWNER + ", " + ALLOWED_USER +"));";
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
                OUTFIT_DATE + " TEXT, " +
                "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "));";
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
                               String c1, String c2, String fit, String type, String size, String material, String desc, String date) {

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
        contentValues.put(CLOTHING_STATUS,"Available");
        contentValues.put(DATE_CREATED,date);
        contentValues.put(USER_ID, loggedUserID());

        long result = db.insert(CLOTHING_TABLE, null, contentValues);

        return result != -1;
    }

    // METHOD FOR CREATING AN OUTFIT
    public boolean addOutfit(String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTFIT_NAME, name);
        contentValues.put(OUTFIT_DATE, date);
        contentValues.put(USER_ID, loggedUserID());

        long result = db.insert(OUTFIT_TABLE, null, contentValues);

        return result != -1;
    }

    // METHOD FOR ADDING CLOTHING TO AN OUTFIT
    public boolean addClothingToOutfit(String clothingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTFIT_ID, getLatestOutfit());
        contentValues.put(CLOTHING_ID, clothingID);

        long result = db.insert(OUTFIT_CLOTHING_TABLE, null, contentValues);

        return result != -1;
    }

    // ADD A USER
    public boolean addUser(String fullname, String userName, String password,String hot,String freezing,String warm,String cold) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_FULLNAME, fullname);
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_PASSWORD,password);
        contentValues.put(USER_KEY,generateKey());
        contentValues.put(User_Hot,hot);
        contentValues.put(User_Freezing,freezing);
        contentValues.put(User_Warm,warm);
        contentValues.put(User_Cold,cold);




        long result = db.insert(USER_TABLE,null,contentValues);

        return result != -1;
    }

    public boolean shareCloset(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOSET_OWNER, userKeyCheck(key));
        contentValues.put(ALLOWED_USER, loggedUserID());

        long result = db.insert(SHARED_CLOSET_TABLE,null,contentValues);
        return result != -1;
    }



    // LOG-IN USER
    public boolean logginUser(String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, userID);


        long result = db.insert(LOGGED_USER_TABLE,null,contentValues);
        return result != -1;
    }

    // ADD A TAG
    public boolean addTag(String tag, int clothingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOTHING_ID, clothingID);
        contentValues.put(TAGS_ID,getTagID(tag));

        long result = db.insert(CLOTHING_TAGS_TABLE,null,contentValues);

        return result != -1;
    }

    // ADD TAGS TO TABLE
    public boolean addToTagsTable(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAGS,tag);

        long result = db.insert(TAGS_TABLE,null,contentValues);

        return result != -1;
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

    // RETURN ID OF LAST OUTFIT MADE
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

    // RETURN  LIST OF SAVED OUTFITS
    public Cursor readUserOutfits() {
        String query = "SELECT * FROM " +
                OUTFIT_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query,null);
        return cursor;
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
    // Check if Outfit table is empty
    public boolean outfitTableEmpty() {
        String query = "SELECT COUNT(*) FROM " + OUTFIT_TABLE;
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

    // NEXT FEW CHECKS LOOKS TO SEE IF AN ITEM IS FOR A SPECIFIC SEASON
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

    public boolean checkOutfitName(String outfitName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + OUTFIT_TABLE +
                " WHERE " + OUTFIT_NAME + " = '" + outfitName + "' ";

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return false;
        }
        return true;
    }

    // CHECK IF THERE IS A USER WITH THE SPECIFIED KEY
    // RETURNS -1 IF IT DIDN'T WORK (KEY DOESN'T EXIST)
    public int userKeyCheck(String key) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + USER_ID +
                " FROM " + USER_TABLE +
                " WHERE " + USER_KEY + " = '" + key + "' ";

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getInt(0);
            }
        }
        return -1;
    }

    // GET ALL CLOTHES
    Cursor readUsersClothing() {
        String query = "SELECT * FROM " + CLOTHING_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        return cursor;
    }

    Cursor readClothingType(String type) {
        String query = "SELECT * FROM " + CLOTHING_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID() +
                " AND " + CLOTHING_TYPE + " = '" + type + "' " +
                " AND " + CLOTHING_STATUS + " = 'Available' " ;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        return cursor;
    }

    Cursor readOutfitClothing(String outfitID) {
        String Subquery = " SELECT " + CLOTHING_ID +
                " FROM " + OUTFIT_CLOTHING_TABLE +
                " WHERE " + OUTFIT_ID + " = " + outfitID;
        String query = "SELECT * FROM " + CLOTHING_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID() +
                " AND " + CLOTHING_ID + " IN " + "(" + Subquery + ")";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        return cursor;
    }

    // GETS LIST OF EVERYONE WHO HAS ACCESS TO A USERS CLOSET
    public Cursor readSharedUsers() {
        String Subquery = " SELECT " + CLOSET_OWNER +
                " FROM " + SHARED_CLOSET_TABLE +
                " WHERE " + ALLOWED_USER + " = " + loggedUserID();
        String Query = "SELECT " + USER_FULLNAME + ", " + USER_ID +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " IN " + " (" + Subquery + ")";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(Query,null);
        }

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

    // GET THE FIRST COLOR OF AN ITEM
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

    // GET THE SECOND COLOR OF AN ITEM
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

    public String getClothingType(String clothingID) {
        String result = "DIDN'T WORK!";
        String Query = "SELECT " + CLOTHING_TYPE +
                " FROM " + CLOTHING_TABLE +
                " WHERE " +  CLOTHING_ID + " = " + clothingID ;
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

    // GET A CLOTHING ITEMS OCCASION
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

    // GET A CLOTHING ITEMS PATTERN
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

    // CHECK HOW A CLOTHING ITEM FITS
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

    // GETS FOR USERS---------------------------------------------------------------------
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

    // GET SHARED USER NAMES
    public Cursor getSharedUsers() {
        String subquery = " SELECT " + CLOSET_OWNER +
                " FROM " + SHARED_CLOSET_TABLE +
                " WHERE " + ALLOWED_USER + " = " + loggedUserID();
        String Query = "SELECT " + USER_FULLNAME + ", " + USER_ID +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " IN (" + subquery + ")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(Query,null);
        return cursor;
    }

    public String getUserName() {
        String query = "SELECT " + USER_FULLNAME +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "FAILED";
    }
    public String getUserUserName() {
        String query = "SELECT " + USER_NAME +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "FAILED";
    }
    public String getUserPassword() {
        String query = "SELECT " + USER_PASSWORD +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "FAILED";
    }
    public String getUserHotTemp() {
        String query = "SELECT " + User_Hot +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "FAILED";
    }
    public String getUserWarmTemp() {
        String query = "SELECT " + User_Warm +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "FAILED";
    }
    public String getUserColdTemp() {
        String query = "SELECT " + User_Cold +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "FAILED";
    }
    public String getUserFreezingTemp() {
        String query = "SELECT " + User_Freezing +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        return "FAILED";
    }
    public Cursor readSharedCloset(String userID) {
        String query = "SELECT * FROM " + CLOTHING_TABLE +
                " WHERE " + USER_ID + " = " + userID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        return cursor;
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

    public boolean isSharing() {
        String Query = "SELECT COUNT(*) " +
                " FROM " + SHARED_CLOSET_TABLE +
                " WHERE " + ALLOWED_USER + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(Query,null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                return true;
            }
        }
        return false;
    }

    public String getUserKey() {
        String result = "DIDN'T WORK";
        String Query = "SELECT " + USER_KEY +
                " FROM " + USER_TABLE +
                " WHERE " + USER_ID + " = " + loggedUserID();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(Query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }

        return result;
    }

    // checks if user is logged in
    // returns user ID if passed and failed other wise
    public String checkLogin(String userName, String password) {
        String Query = "SELECT " + USER_ID +
                " FROM " + USER_TABLE +
                " WHERE " + USER_NAME + " = '" + userName + "' " +
                " AND " + USER_PASSWORD + " = '" + password + "' ";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(Query, null);
            if (db != null) {
                cursor.moveToFirst();
                if( cursor != null && cursor.moveToFirst() ) {
//                    cursor = db.rawQuery(Query, null);
                    return cursor.getString(0);
                }
            }

         return null;

    }


    // GETS FOR TAGS-------------------------------------------------------------------
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

    // CHECKS FOR OUTFIT
    // CHECK IF OUTFIT HAS SHIRT
    public boolean checkOutfitTypes(String outfitID, String clothingType) {
        String subquery = " SELECT " + CLOTHING_ID +
                " FROM " + OUTFIT_CLOTHING_TABLE +
                " WHERE " + OUTFIT_ID + " = " + outfitID;

        String query = "SELECT COUNT(*) "
                + " FROM " + CLOTHING_TABLE +
                " WHERE " + CLOTHING_TYPE + " = '" + clothingType + "' " +
                " AND " + CLOTHING_ID + " IN (" + subquery + ")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0)
                return false;
        }

        return true;
    }

    // UPDATES-----------------------------------------------------------------------------------------
    // UPDATE AN ITEM
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

    void updateClothingStatus(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLOTHING_STATUS, status);

        long result = db.update(CLOTHING_TABLE, cv, "clothing_id=?", new String[] {id});
        if (result == -1)
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show();
    }
    void updateUserPassword(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_PASSWORD,password);

        long result = db.update(USER_TABLE, contentValues, "user_id=?",new String[] {loggedUserID()});
        if (result == -1)
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show();
    }
    void updateAllUserTemp(String hot, String warm, String cold,String freezing){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User_Hot,hot);
        contentValues.put(User_Warm,warm);
        contentValues.put(User_Cold,cold);
        contentValues.put(User_Freezing,freezing);

        long result = db.update(USER_TABLE, contentValues, "user_id=?",new String[] {loggedUserID()});
        if (result == -1)
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show();
    }
    void updateOutfit(String row_id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTFIT_NAME,name);

        long result = db.update(OUTFIT_TABLE, contentValues, "outfit_id=?", new String[] {row_id});
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

    // Delete one clothing item
    void logOut() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + LOGGED_USER_TABLE);
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

    void stopSharing(String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + SHARED_CLOSET_TABLE +
                " WHERE " + CLOSET_OWNER + " = " + loggedUserID() +
                " AND " + ALLOWED_USER + " = " + userID);
    }

    void deleteClothingItemFromOutfit(String outfitID, String clothingID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + OUTFIT_CLOTHING_TABLE +
                " WHERE " + OUTFIT_ID + " = " + outfitID +
                " AND " + CLOTHING_ID + " = " + clothingID);
    }

    void deleteAllClothingFromOutfit(String outfitID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + OUTFIT_CLOTHING_TABLE +
                " WHERE " + OUTFIT_ID + " = " + outfitID);
    }

    void deleteOutfit(String outfitID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + OUTFIT_CLOTHING_TABLE +
                " WHERE " + OUTFIT_ID + " = " + outfitID);
        db.execSQL("DELETE FROM " + OUTFIT_TABLE +
                " WHERE " + OUTFIT_ID + " = " + outfitID);
    }


    // OTHER----------------------------------------------------------------------------------------------------------
    String generateKey() {
        // list of possible values
        String values = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random(); // use this to generate a random number
        String key = "";

        // Key is only 8 digits long
        for (int i = 0; i < 8; i++) {
            key += values.charAt(random.nextInt(values.length())); // get random letter from that list ^
        }

        return key;
    }
}