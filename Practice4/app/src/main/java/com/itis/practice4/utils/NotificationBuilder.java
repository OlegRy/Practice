package com.itis.practice4.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.itis.practice4.PracticeActivity;
import com.itis.practice4.R;

public class NotificationBuilder {

    public static void notify(Context context, int icon, String title, String text, int id, boolean autoCancel) {
        getNotificationManager(context).notify(id, createBuilder(context, icon, title, text, id,
                autoCancel).build());
    }

    public static void notify(Context context, int icon, String title, String text, int id, boolean autoCancel, PendingIntent pendingIntent) {
        getNotificationManager(context).notify(id, createBuilder(context, icon, title, text, id, autoCancel,
                pendingIntent).build());
    }

    public static void notifyWithActions(Context context, int icon, String title, String text, int id) {
        getNotificationManager(context).notify(id, createActionBuilder(context, icon, title, text, id)
                .build());
    }

    private static NotificationCompat.Builder createBuilder(Context context, int icon, String title,
                                                            String text, int id, boolean autoCancel) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(sound)
                .setAutoCancel(autoCancel);
        return builder;
    }

    private static NotificationCompat.Builder createBuilder(Context context, int icon,
                                                            String title, String text, int id,
                                                            boolean autoCancel, PendingIntent pendingIntent) {
        return createBuilder(context, icon, title, text, id, autoCancel).setContentIntent(pendingIntent);
    }

    private static NotificationCompat.Builder createActionBuilder(Context context, int icon,
                                                                  String title, String text, int id) {
        PendingIntent okIntent = PendingIntent.getActivity(context, 0, new Intent(context, PracticeActivity.class), 0);
        Intent settings = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        PendingIntent settingsIntent = PendingIntent.getActivity(context, 0, settings, 0);
        return createBuilder(context, icon, title, text, id, true)
                .addAction(R.drawable.ic_done_black_24dp, context.getResources().getString(R.string.ok), okIntent)
                .addAction(R.drawable.ic_settings_black_24dp, context.getResources().getString(R.string.action_settings),
                        settingsIntent);
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


}
