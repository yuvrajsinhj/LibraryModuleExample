package com.androhub.networkmodule.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        String referrerString = extras.getString("referrer");

        Log.e("referrerUrl", "Referrer is: " + referrerString);
    }
}