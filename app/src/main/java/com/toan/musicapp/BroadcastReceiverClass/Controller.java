package com.toan.musicapp.BroadcastReceiverClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Controller extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("PerformAction").putExtra("ActionName", intent.getAction()));
    }

}
