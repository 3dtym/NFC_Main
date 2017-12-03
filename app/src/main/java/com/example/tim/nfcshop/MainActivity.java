package com.example.tim.nfcshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textViewInfo;
    private NfcReader nfcReader;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewInfo = (TextView)findViewById(R.id.info);


        nfcReader = new NfcReader(this);

        nfcReader.getNfc();

        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);
                        intent.putExtra("NFC_ID", "58c283a1600");
                        startActivity(intent);
                    }
                }
        );


        button = (Button) findViewById(R.id.MyButton);

        // Capture button clicks
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        ActivityDatabase.class);
                startActivity(myIntent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcReader.enableNfc();//
    }

    @Override
    protected void onNewIntent(Intent intent) {
        final String id = nfcReader.getTag(intent);
        textViewInfo.setText(id);
        final DBHelper db = new DBHelper(this);
        if(db.isAdmin(id) == -1){
            registerUser(db,id);
        }
        else if(db.isAdmin(id) == 1){
            Intent intent2 = new Intent(getApplicationContext(), AdminActivity.class);
            intent2.putExtra("NFC_ID", id);
            startActivity(intent2);
        }else {
            Intent intent2 = new Intent(getApplicationContext(), ShoppingCart.class);
            intent2.putExtra("NFC_ID", id);
            startActivity(intent2);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcReader.disableNFC();//
    }

    private void registerUser(final DBHelper db, final String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User registration");
        final EditText input = new EditText(this);
        input.setHint("Zadaj svoje meno");
        builder.setView(input);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                db.createUserDB(new User(name,10.0,0,id));
            }
        });
        builder.setNegativeButton("canel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }
}
