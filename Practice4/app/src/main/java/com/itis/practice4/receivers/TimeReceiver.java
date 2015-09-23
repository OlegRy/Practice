package com.itis.practice4.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itis.practice4.PracticeActivity;
import com.itis.practice4.R;
import com.itis.practice4.utils.NotificationBuilder;

public class TimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, PracticeActivity.class), 0);
        String notificationText;
        boolean canCancel;
        if (intent.getAction().equals(context.getResources().getString(R.string.time_action))) {
            int hour = intent.getIntExtra(PracticeActivity.TAG_HOURS, 0);
            int minute = intent.getIntExtra(PracticeActivity.TAG_MINUTES, 0);
            notificationText = String.format(context.getResources().getString(R.string.time_passed), hour, minute);
            canCancel = false;
        } else {
            notificationText = context.getResources().getString(R.string.one_minute_passed);
            canCancel = true;
        }
        NotificationBuilder.notify(context, R.drawable.ic_info_black_24dp,
                context.getResources().getString(R.string.app_name), notificationText,
                context.getResources().getInteger(R.integer.time_passed),
                canCancel, pendingIntent);
    }
}
