package com.example.tim.nfcshop;

import android.app.Activity;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class ShoppingCart extends Activity{
    private User user;
    private List<Product> products;
    private Double price;
    private TextView creditView;

    private static final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        products = new LinkedList<>();
        products.add(new Product("Water",0.99,1));
        products.add(new Product("Snickers",1.25,2));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));
        products.add(new Product("Hot-dog",1.50,0));


        user = new User("Test",5.0,0,"012165464");

        setContentView(R.layout.activity_shopping);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        CustomAdapter adapter = new CustomAdapter(products, new AdapterListener());
        recyclerView.setAdapter(adapter);

        TextView usernameView = findViewById(R.id.usename);
        usernameView.setText(user.getMeno());
        creditView = findViewById(R.id.credit);
        creditView.setText(Double.toString(user.getKredit()) + "€");
        ImageView logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(close);
        logoutButton.setImageResource(R.drawable.logout);

    }

    private class AdapterListener implements CustomAdapter.Listener {
        @Override
        public void onSelected(Product data) {
            price = data.getCena();

            DecimalFormat twoDForm = new DecimalFormat("#.##");
            double balance = Double.valueOf(twoDForm.format(user.getKredit()-data.getCena()));

            if( balance < 0.0) {
                Toast.makeText(ShoppingCart.this,"Not enough credit!",Toast.LENGTH_LONG).show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCart.this);
                builder.setMessage("Do you want to purchase " + data.getNazov() + " for " + data.getCena() + "€\n(balance after purchase: " + balance + "€).").setPositiveButton("Yes", payDialog)
                        .setNegativeButton("No", payDialog).show();
            }
        }

    }

    private View.OnClickListener close = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCart.this);
            builder.setMessage("Do you want to log out?").setPositiveButton("Yes", logoutDialog)
                    .setNegativeButton("No", logoutDialog).show();
        }
    };

    private void logout(){
        user = null;
        finish();
    }

    DialogInterface.OnClickListener logoutDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    logout();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    DialogInterface.OnClickListener payDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    DecimalFormat twoDForm = new DecimalFormat("#.##");
                    user.setKredit(Double.valueOf(twoDForm.format(user.getKredit()-price)));
                    creditView.setText(Double.toString(user.getKredit()) + "€");
                    //todo:update database
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public static final long LOGOUT_TIMEOUT = 30000; // 30s

    private Handler logoutHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable logoutCallback = new Runnable() {
        @Override
        public void run() {
            logout();
        }
    };

    public void resetLogoutTimer(){
        logoutHandler.removeCallbacks(logoutCallback);
        logoutHandler.postDelayed(logoutCallback, LOGOUT_TIMEOUT);
    }

    public void stopLogoutTimer(){
        logoutHandler.removeCallbacks(logoutCallback);
    }

    @Override
    public void onUserInteraction(){
        resetLogoutTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetLogoutTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLogoutTimer();
    }

}
