package com.example.menu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "VirtualCloset.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CLOTHING_TABLE = "clothing_table";
    private static final String CLOTHING_ID = "_ID";
    private static final String CLOTHING_NAME = "Clothing_name";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CLOTHING_TABLE);
        onCreate(db);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + CLOTHING_TABLE +
                " (" + CLOTHING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLOTHING_NAME + " TEXT);";
        db.execSQL(createTable);
    }

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLOTHING_NAME, item);

        long result = db.insert(CLOTHING_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

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

        long result = db.update(CLOTHING_TABLE, cv, "_ID=?", new String[] {row_id});
        if (result == -1)
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Updated!!", Toast.LENGTH_SHORT).show();
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(CLOTHING_TABLE, "_ID=?", new String[] {row_id});
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