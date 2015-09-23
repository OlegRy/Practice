package com.itis.practice4.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActionButtonReceiver extends BroadcastReceiver {

    public static final String ACTION = "action_button";
    public static final String ACTION_BUTTON = "com.itis.practice4.ActionButtonReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(ACTION, -1);
        if (id != -1) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(id);
        }
    }
}
