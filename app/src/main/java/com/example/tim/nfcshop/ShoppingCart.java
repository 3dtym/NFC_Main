package com.example.tim.nfcshop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class ShoppingCart extends Activity{
    String user;
    double credit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> foods = getFoods();
        user = getUser();
        credit = 100.0;

        setContentView(R.layout.activity_shopping);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        CustomAdapter adapter = new CustomAdapter(foods, new AdapterListener());
        recyclerView.setAdapter(adapter);

        TextView usernameView = findViewById(R.id.usename);
        usernameView.setText(user);
        TextView creditView = findViewById(R.id.credit);
        creditView.setText(Double.toString(credit) + "€");
    }

    private class AdapterListener implements CustomAdapter.Listener {
        @Override
        public void onSelected(String data) {
            Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();

        }
    }


    private List<String> getFoods(){
        List<String> foods = new LinkedList<>();
        foods.add("Minaral water");
        foods.add("Coffe");
        foods.add("Hot-dog");
        foods.add("Hamburger");
        foods.add("Cola");
        foods.add("Choco-snack");
        return foods;
    }

    private String getUser(){
        return "testUser";
    }
}
