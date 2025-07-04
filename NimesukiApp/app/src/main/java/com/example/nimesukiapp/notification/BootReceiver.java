package com.example.nimesukiapp.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.nimesukiapp.view.activities.ListaAnimesView;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            ListaAnimesView.scheduleWeeklyAnimeNotification(context);
        }
    }
}
