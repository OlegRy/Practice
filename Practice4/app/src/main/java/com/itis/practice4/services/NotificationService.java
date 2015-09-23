package com.itis.practice4.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.itis.practice4.PracticeActivity;
import com.itis.practice4.R;
import com.itis.practice4.receivers.TimeReceiver;

import java.util.Calendar;

public class NotificationService extends Service {

    private AlarmManager mAlarmManager;

    private long mTimePassed = 60000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notifyIntent = new Intent(getApplicationContext(), TimeReceiver.class);
        if (intent.getExtras() != null) {
            notifyTimePassed(intent, notifyIntent);
        } else {
            notifyOneMinutePassed(notifyIntent);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void notifyOneMinutePassed(Intent notifyIntent) {
        notifyIntent.setAction(getResources().getString(R.string.one_minute_action));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, notifyIntent, 0);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + mTimePassed, pendingIntent);
    }

    private void notifyTimePassed(Intent intent, Intent notifyIntent) {
        int hours = intent.getIntExtra(PracticeActivity.TAG_HOURS, 0);
        int minute = intent.getIntExtra(PracticeActivity.TAG_MINUTES, 0);
        notifyIntent.putExtra(PracticeActivity.TAG_HOURS, hours);
        notifyIntent.putExtra(PracticeActivity.TAG_MINUTES, minute);
        notifyIntent.setAction(getResources().getString(R.string.time_action));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, notifyIntent, 0);
        mAlarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}
