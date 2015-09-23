package com.itis.practice4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.itis.practice4.PracticeActivity;
import com.itis.practice4.R;
import com.itis.practice4.utils.NotificationBuilder;

public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra(PracticeActivity.TAG_MESSAGE);
        if (name != null && getWifiName(context).contains(name)) {
            NotificationBuilder.notify(context, R.drawable.ic_info_black_24dp,
                    R.string.app_name,
                    String.format(context.getResources().getString(R.string.connected), name),
                    context.getResources().getInteger(R.integer.connected), false);
        }

    }

    private String getWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }
}
