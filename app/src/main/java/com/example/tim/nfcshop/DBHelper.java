package com.example.tim.nfcshop;

/**
 * Created by Gurt on 27.11.17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Db.db";
    public static final String CUSTOMERS_TABLE_NAME = "contacts";
    public static final String PRODUCTS_TABLE_NAME = "products";

    public static final String CUSTOMERS_COLUMN_ID = "customerId";
    public static final String CUSTOMERS_COLUMN_NAME = "customerName";
    public static final String CUSTOMERS_COLUMN_CREDIT = "credit";
    public static final String CUSTOMERS_COLUMN_ADMIN = "admin";

    public static final String PRODUCTS_COLUMN_ID = "productId";
    public static final String PRODUCTS_COLUMN_NAME = "productName";
    public static final String PRODUCTS_COLUMN_PRICE = "price";
    public static final String PRODUCTS_COLUMN_PICTURE = "picture";
    private static final String TAG = "DatabaseHelper";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + CUSTOMERS_TABLE_NAME + "("+ CUSTOMERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CUSTOMERS_COLUMN_NAME +" TEXT, "+ CUSTOMERS_COLUMN_CREDIT + " INTEGER, " + CUSTOMERS_COLUMN_ADMIN +" INTEGER" + ")";
        String createTable2 = "CREATE TABLE IF NOT EXISTS " + PRODUCTS_TABLE_NAME + "(" + PRODUCTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCTS_COLUMN_NAME +" TEXT, " + PRODUCTS_COLUMN_PRICE + " INTEGER, " + PRODUCTS_COLUMN_PICTURE + " INTEGER" + ")";
        db.execSQL(createTable);
        db.execSQL(createTable2);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + CUSTOMERS_TABLE_NAME);
        db.execSQL("DROP IF TABLE EXISTS " + PRODUCTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean createUserDB(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMERS_COLUMN_NAME, user.getMeno());
        contentValues.put(CUSTOMERS_COLUMN_CREDIT, user.getKredit());
        contentValues.put(CUSTOMERS_COLUMN_ADMIN, 0);

        Log.d(TAG, "addData: Adding " + user.getMeno() + "with credit " + user.getKredit() + " to " + CUSTOMERS_TABLE_NAME);

        long result = db.insert(CUSTOMERS_TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CUSTOMERS_TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT customerId FROM " + CUSTOMERS_TABLE_NAME +
                " WHERE " + CUSTOMERS_COLUMN_NAME + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + CUSTOMERS_TABLE_NAME + " SET " + CUSTOMERS_COLUMN_NAME +
                " = '" + newName + "' WHERE " + "ID" + " = '" + id + "'" +
                " AND " + CUSTOMERS_COLUMN_NAME + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + CUSTOMERS_TABLE_NAME + " WHERE "
                + "ID" + " = '" + id + "'" +
                " AND " + CUSTOMERS_COLUMN_NAME + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
}