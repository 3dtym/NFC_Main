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

public class UserAdmFrg extends Fragment {

    private static final String TAG = "ListDataActivity";

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_frag, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);

        populateListView();

        return view;
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        final DBHelper db = new DBHelper(getContext());
        //get the data and append to a list
        final ArrayList<User> users = db.getAllUsers();
        ArrayList<String> listData = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add("ID: " + users.get(i).getCardId() + "    Meno: " + users.get(i).getMeno() + "     Kredit: " + users.get(i).getKredit());
        }
        //create the list adapter and set the adapter
        final ListAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i);
                updateUser(db, user.getCardId());
            }
        });
    }

    private void updateUser(final DBHelper db, final String id) {
        LayoutInflater linf = LayoutInflater.from(getContext());
        final View inflator = linf.inflate(R.layout.user_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("User edit");

        builder.setView(inflator);
        final EditText input = inflator.findViewById(R.id.name);
        final EditText input2 = inflator.findViewById(R.id.kredit);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input2.length() != 0) {
                    double credit = Double.valueOf(input2.getText().toString());
                    db.updateCreditUser(id,credit);
                }else if (input.length() != 0){
                    String name = input.getText().toString();
                    db.updateNameUser(id,name);
                }else if (input.length() != 0 && input2.length() != 0){
                    String name = input.getText().toString();
                    double credit = Double.valueOf(input2.getText().toString());
                    db.updateUser(id, name, credit);
                }else
                    Toast.makeText(getContext(),"Obe polia musia byt vyplnene", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteUser(id);
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }
}
