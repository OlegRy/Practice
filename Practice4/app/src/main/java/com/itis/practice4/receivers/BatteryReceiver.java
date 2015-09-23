package com.itis.practice4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.itis.practice4.R;
import com.itis.practice4.utils.NotificationBuilder;

public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (level != -1 && scale != -1) {
            float batteryPct = level / (float) scale;
            if (batteryPct < 30) {
                NotificationBuilder.notifyWithActions(context, R.drawable.ic_info_black_24dp,
                        R.string.app_name, R.string.battery,
                        context.getResources().getInteger(R.integer.battery));
            }
        }
    }
}
