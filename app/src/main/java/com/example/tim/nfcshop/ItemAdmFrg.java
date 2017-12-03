package com.example.tim.nfcshop;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;

public class ItemAdmFrg  extends Fragment{

    private static final String TAG = "ListDataActivity";

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_frag, container, false);

        mListView = (ListView) view.findViewById(R.id.listView2);

        populateListView();

        return view;
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        final DBHelper db = new DBHelper(getContext());
        //get the data and append to a list
        final ArrayList<Product> products = db.getAllProducts();
        ArrayList<String> listData = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add("ID: " + products.get(i).getProduktId() + "Produkt: " + products.get(i).getNazov() + "    Cena: " + products.get(i).getCena() + "     Typ: " + products.get(i).getPicture());
        }
        //create the list adapter and set the adapter
        final ListAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product =  products.get(i);
                Toast.makeText(getContext(),"vybral si si", Toast.LENGTH_LONG).show();
                //updateUser(db, user.getCardId());
            }
        });
    }


}
