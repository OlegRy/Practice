package com.itis.practice4.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itis.practice4.PracticeActivity;
import com.itis.practice4.R;
import com.itis.practice4.utils.NotificationBuilder;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, PracticeActivity.class), 0);
        NotificationBuilder.notify(context, R.drawable.ic_info_black_24dp,
                context.getResources().getString(R.string.app_name),
                context.getResources().getString(R.string.boot),
                context.getResources().getInteger(R.integer.boot),
                true, pendingIntent);
    }
}
