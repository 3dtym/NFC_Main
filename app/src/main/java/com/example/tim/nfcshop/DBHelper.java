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

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Databaza.db";
    public static final String CUSTOMERS_TABLE_NAME = "contacts";
    public static final String PRODUCTS_TABLE_NAME = "products";

    public static final String CUSTOMERS_COLUMN_ID = "customerId";
    public static final String CUSTOMERS_COLUMN_NAME = "customerName";
    public static final String CUSTOMERS_COLUMN_CREDIT = "credit";
    public static final String CUSTOMERS_COLUMN_ADMIN = "admin";
    public static final String CUSTOMERS_COLUMN_CARD_ID = "cardId";

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
        String createTable = "CREATE TABLE " + CUSTOMERS_TABLE_NAME + "("+ CUSTOMERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CUSTOMERS_COLUMN_NAME +" TEXT, "+ CUSTOMERS_COLUMN_CREDIT + " DOUBLE, " + CUSTOMERS_COLUMN_ADMIN+
                " INTEGER, " + CUSTOMERS_COLUMN_CARD_ID + " TEXT " + ")";
        String createTable2 = "CREATE TABLE " + PRODUCTS_TABLE_NAME + "(" + PRODUCTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCTS_COLUMN_NAME +" TEXT, " + PRODUCTS_COLUMN_PRICE + " DOUBLE, " + PRODUCTS_COLUMN_PICTURE + " INTEGER" + ")";
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
        contentValues.put(CUSTOMERS_COLUMN_ADMIN, user.getIsAsmin());
        contentValues.put(CUSTOMERS_COLUMN_CARD_ID, user.getCardId());

        Log.d(TAG, "addData: Adding " + user.getMeno() + "with credit " + user.getKredit() + " to " + CUSTOMERS_TABLE_NAME);

        long result = db.insert(CUSTOMERS_TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    /**
     * Returns all the data from database
     * @return
     */
    public ArrayList<User> getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CUSTOMERS_TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        ArrayList<User> users = new ArrayList<>();
        User user=null;
        while(data.moveToNext()){
            user = new User(Integer.valueOf(data.getString(0)), data.getString(1), Double.valueOf(data.getString(2)), Integer.valueOf(data.getString(3)), data.getString(4));
            users.add(user);
            user = null;
        }

        return users;
    }
    public ArrayList<User> getUsersByName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT *" + " FROM " + CUSTOMERS_TABLE_NAME +
                " WHERE " + CUSTOMERS_COLUMN_NAME + " LIKE '%" + name + "%'";
        Cursor data = db.rawQuery(query, null);
        ArrayList<User> users = new ArrayList<>();
        User user=null;
        while(data.moveToNext()){
            user = new User(Integer.valueOf(data.getString(0)), data.getString(1), Integer.valueOf(data.getString(2)), Integer.valueOf(data.getString(3)), data.getString(4));
            users.add(user);
            user = null;
        }

        return users;
    }
    public String geNameByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT customerName FROM " + CUSTOMERS_TABLE_NAME +
                " WHERE " + CUSTOMERS_COLUMN_ID + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        String name = "null";
        while(data.moveToNext()) {
             name = data.getString(0);
        }
        return name;
    }

    public ArrayList<User> getUsersByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT *" + " FROM " + CUSTOMERS_TABLE_NAME +
                " WHERE " + CUSTOMERS_COLUMN_ID + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        ArrayList<User> users = new ArrayList<>();
        User user=null;
        while(data.moveToNext()){
            user = new User(Integer.valueOf(data.getString(0)), data.getString(1), Integer.valueOf(data.getString(2)), Integer.valueOf(data.getString(3)), data.getString(4));
            users.add(user);
            user = null;
        }

        return users;
    }

    public ArrayList<User> getUserByNfcId(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT *" + " FROM " + CUSTOMERS_TABLE_NAME +
                " WHERE " + CUSTOMERS_COLUMN_CARD_ID + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        ArrayList<User> users = new ArrayList<>();
        User user=null;
        while(data.moveToNext()){
            user = new User(Integer.valueOf(data.getString(0)), data.getString(1), Integer.valueOf(data.getString(2)), Integer.valueOf(data.getString(3)), data.getString(4));
            users.add(user);
            user = null;
        }

        return users;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getCustomerNfcID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT customerId FROM " + CUSTOMERS_TABLE_NAME +
                " WHERE " + CUSTOMERS_COLUMN_ID + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param id
     * @param name
     */
    public void updateNameUser(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + CUSTOMERS_TABLE_NAME + " SET " + CUSTOMERS_COLUMN_NAME +
                " = '" + name + "' WHERE " + CUSTOMERS_COLUMN_CARD_ID + " = '" + id + "'";
        db.execSQL(query);
    }

    public void updateCreditUser(String id, double newKredit){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + CUSTOMERS_TABLE_NAME + " SET " + CUSTOMERS_COLUMN_CREDIT +
                " = '" + newKredit + "' WHERE " + CUSTOMERS_COLUMN_CARD_ID + " = '" + id + "'";
        db.execSQL(query);
    }

    void updateUser(String id, String name, double credit){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + CUSTOMERS_TABLE_NAME + " SET " + CUSTOMERS_COLUMN_NAME +
                " = '" + name + "', " + CUSTOMERS_COLUMN_CREDIT + " = '" + credit + "' WHERE cardID = '"+ id +"'";
        db.execSQL(query);
    }

    public double getCreditUser(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + CUSTOMERS_COLUMN_CREDIT + " FROM "+ CUSTOMERS_TABLE_NAME + " WHERE " + CUSTOMERS_COLUMN_ID + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        data.moveToNext();
        return data.getDouble(0);
    }

    int isAdmin(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + CUSTOMERS_COLUMN_ADMIN + " FROM "+ CUSTOMERS_TABLE_NAME + " WHERE " + CUSTOMERS_COLUMN_CARD_ID + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        if(!data.moveToNext())
            return -1;
        else
            return data.getInt(0);
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