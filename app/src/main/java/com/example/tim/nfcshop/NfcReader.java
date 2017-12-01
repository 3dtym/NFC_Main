package com.example.tim.nfcshop;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.widget.Toast;

public class NfcReader {
    private Context context;
    private Activity act;
    private NfcAdapter nfcAdapter;

    public NfcReader(Activity act) {
        this.act = act;
        context = act.getApplicationContext();
    }

    void getNfc(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if(nfcAdapter == null){
            Toast.makeText(context,
                    "NFC NOT supported on this devices!",
                    Toast.LENGTH_LONG).show();
        }else if(!nfcAdapter.isEnabled()){
            Toast.makeText(context,
                    "NFC NOT Enabled!",
                    Toast.LENGTH_LONG).show();
        }
    }

    void enableNfc(){
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, act.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
//        nfcAdapter.enableForegroundDispatch(act,pendingIntent,new IntentFilter[]{filter},null);
    }

    String getTag(Intent intent){
        StringBuilder tagInfo = new StringBuilder();
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            if(tagId == null){
                Toast.makeText(context,"Unable to read ID.",Toast.LENGTH_LONG).show();

            }else{
                for (byte aTagId : tagId) {
                    tagInfo.append(Integer.toHexString(aTagId & 0xFF));
                }
            }
        }
        return tagInfo.toString();
        // return "58c283a1600"
    }

    void disableNFC(){
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        nfcAdapter.disableForegroundDispatch(act);
    }
}
