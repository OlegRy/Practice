package com.itis.practice4.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import com.itis.practice4.R;
import com.itis.practice4.receivers.ActionButtonReceiver;

public class NotificationBuilder {

    public static void notify(Context context, @DrawableRes int icon, String title, String text,
                              int id, boolean autoCancel) {
        getNotificationManager(context).notify(id, createBuilder(context, icon, title, text, id,
                autoCancel).build());
    }

    public static void notify(Context context, @DrawableRes int icon, String title, String text,
                              int id, boolean autoCancel, PendingIntent pendingIntent) {
        getNotificationManager(context).notify(id, createBuilder(context, icon, title, text, id, autoCancel,
                pendingIntent).build());
    }

    public static void notify(Context context, @DrawableRes int icon, @StringRes int title, @StringRes int text,
                              int id, boolean autoCancel) {
        notify(context, icon, context.getResources().getString(title), context.getResources().getString(text),
                context.getResources().getInteger(id), autoCancel);
    }

    public static void notify(Context context, @DrawableRes int icon, @StringRes int title, @StringRes int text,
                              int id, boolean autoCancel, PendingIntent pendingIntent) {
        notify(context, icon, context.getResources().getString(title), context.getResources().getString(text),
                id, autoCancel, pendingIntent);
    }

    public static void notify(Context context, @DrawableRes int icon, @StringRes int title, String text,
                              int id, boolean autoCancel) {
        notify(context, icon, context.getResources().getString(title), text, id, autoCancel);
    }

    public static void notify(Context context, @DrawableRes int icon, @StringRes int title, String text,
                              int id, boolean autoCancel, PendingIntent pendingIntent) {
        notify(context, icon, context.getResources().getString(title), text, id, autoCancel, pendingIntent);
    }

    public static void notifyWithActions(Context context, @DrawableRes int icon, String title, String text, int id) {
        getNotificationManager(context).notify(id, createActionBuilder(context, icon, title, text,
                id).build());
    }

    public static void notifyWithActions(Context context, @DrawableRes int icon, @StringRes int title,
                                         @StringRes int text, @IntegerRes int id) {
        notifyWithActions(context, icon, context.getResources().getString(title), context.getResources().getString(text),
                id);
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
        Intent okIntent = new Intent(ActionButtonReceiver.ACTION_BUTTON);
        okIntent.putExtra(ActionButtonReceiver.ACTION, id);
        PendingIntent okPendingIntent = PendingIntent.getBroadcast(context, 0, okIntent, 0);
        Intent settings = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        PendingIntent settingsIntent = PendingIntent.getActivity(context, 0, settings, 0);

        return createBuilder(context, icon, title, text, id, true)
                .addAction(R.drawable.ic_done_black_24dp, context.getResources().getString(R.string.ok), okPendingIntent)
                .addAction(R.drawable.ic_settings_black_24dp, context.getResources().getString(R.string.action_settings),
                        settingsIntent);
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


}
