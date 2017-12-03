package com.example.tim.nfcshop;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;


public class ShoppingCart extends Activity{
    private User user;
    private List<Product> products;
    private Double price;
    private TextView creditView;
    private DBHelper dbHelper;

    private static final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        String nfc_id = getIntent().getStringExtra("NFC_ID");
        Log.i(TAG, "Got nfc ID" + nfc_id);
        dbHelper = new DBHelper(ShoppingCart.this);
        Log.i(TAG, "DBHelper init");
        user = dbHelper.getUserByNfc(nfc_id);
        Log.i(TAG, "User from db");
        if(user == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCart.this);
            builder.setMessage("Unknown user!").setPositiveButton("Ok", logoutDialog).show();
        }
        Log.i(TAG, "user not empty:"+user.getMeno());
        products = dbHelper.getAllProducts();
        Log.i(TAG, "products loaded");

        setContentView(R.layout.activity_shopping);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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

            DecimalFormat twoDForm = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
            double balance = Double.valueOf(twoDForm.format(user.getKredit()-data.getCena())); // Timovi to tu spadne java.lang.NumberFormatException: For input string: "4,01"

            if( balance < 0.0) {
                //Toast.makeText(ShoppingCart.this,"Not enough credit!",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCart.this);
                builder.setMessage("Not enough credit!").setNegativeButton("Ok", payDialog).show();
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
                    DecimalFormat twoDForm = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
                    user.setKredit(Double.valueOf(twoDForm.format(user.getKredit()-price)));
                    creditView.setText(Double.toString(user.getKredit()) + "€");
                    dbHelper.updateCreditUser(user.getCardId(),user.getKredit());
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
