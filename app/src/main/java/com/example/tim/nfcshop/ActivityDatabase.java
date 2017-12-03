package com.example.tim.nfcshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Gurt on 27.11.17.
 */

public class ActivityDatabase extends AppCompatActivity {

    private static final String TAG = "ActivityDatabase";

    DBHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private Button btnAddItem, btnViewDataItem;
    private EditText editText,editText2;
    private EditText editTextItem,editTextItem2;
    private Spinner typItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        //user
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        //item
        editTextItem = (EditText) findViewById(R.id.editTextItem);
        editTextItem2 = (EditText) findViewById(R.id.editTextItem2);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnViewDataItem = (Button) findViewById(R.id.btnViewItem);
        typItem = (Spinner) findViewById(R.id.typItem);

        mDatabaseHelper = new DBHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = new User();
                if (editText.length() != 0) {
                    newUser.setMeno(editText.getText().toString());
                    editText.setText("");
                } else {
                    toastMessage("You must put something in the name field!");
                }
                if (editText2.length() != 0) {
                    //s tym ze k tomuto by mal mat pristup iba admin nerobim kontrolu na to ci to je naozaj int
                    newUser.setKredit(Double.valueOf(editText2.getText().toString()));
                    editText2.setText("");
                } else {
                    toastMessage("You must put amount in the credit field!");
                }
                if(newUser.getMeno()!=null){
                    newUser.setIsAsmin(7);
                    newUser.setCardId("58c283a1600");
                    AddData(newUser);
                }

            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDatabase.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        //Product
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product newProduct= new Product();
                if (editTextItem.length() != 0) {
                    newProduct.setNazov(editTextItem.getText().toString());
                    editTextItem.setText("");
                } else {
                    toastMessage("You must put something in the Nazov field!");
                }
                if (editTextItem2.length() != 0) {
                    //s tym ze k tomuto by mal mat pristup iba admin nerobim kontrolu na to ci to je naozaj int
                    newProduct.setCena(Double.valueOf(editTextItem2.getText().toString()));
                    editTextItem2.setText("");
                } else {
                    toastMessage("You must put amount in the credit field!");
                }
                newProduct.setPicture(Integer.valueOf(typItem.getSelectedItem().toString()));
                Log.d(TAG, "*** Spinner vyber je: " + Integer.valueOf(typItem.getSelectedItem().toString()));
                if(newProduct.getNazov()!=null){
                    AddProduct(newProduct);
                }

            }
        });

        btnViewDataItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDatabase.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    public void AddData(User user) {
        boolean insertData = mDatabaseHelper.createUserDB(user);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    public void AddProduct(Product product) {
        boolean insertData = mDatabaseHelper.createProductDB(product);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }




    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}