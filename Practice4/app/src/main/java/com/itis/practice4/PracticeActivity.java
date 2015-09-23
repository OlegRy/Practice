package com.itis.practice4;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.itis.practice4.receivers.WifiReceiver;
import com.itis.practice4.services.NotificationService;
import com.itis.practice4.utils.NotificationBuilder;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    public static final String TAG_HOURS = "hours";
    public static final String TAG_MINUTES = "minutes";
    public static final String TAG_MESSAGE = "message";

    private Button mBtnNotifyNow;
    private Button mBtnNotifyByTime;
    private EditText mEtWifiName;
    private Button mBtnCheckWifiConnection;
    private Button mBtnStopCatching;
    private WifiReceiver mWifiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        mBtnNotifyNow = (Button) findViewById(R.id.btn_notify_now);
        mBtnNotifyByTime = (Button) findViewById(R.id.btn_notify_by_time);
        mEtWifiName = (EditText) findViewById(R.id.et_wifi_name);
        mBtnCheckWifiConnection = (Button) findViewById(R.id.btn_catch_connect);
        mBtnStopCatching = (Button) findViewById(R.id.btn_stop_catching);
        mWifiReceiver = new WifiReceiver();
        mBtnNotifyNow.setOnClickListener(this);
        mBtnNotifyByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(PracticeActivity.this,
                        PracticeActivity.this, 0, 0, true);
                dialog.show();
            }
        });
        mBtnCheckWifiConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtWifiName.getText().toString() != null && !mEtWifiName.getText().toString().equals("")) {
                    LocalBroadcastManager.getInstance(PracticeActivity.this).registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
                    sendMessage(mEtWifiName.getText().toString());
                } else {
                    Toast.makeText(PracticeActivity.this, R.string.wifi_input, Toast.LENGTH_LONG).show();
                }
            }
        });
        mBtnStopCatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWifiReceiver != null) {
                    LocalBroadcastManager.getInstance(PracticeActivity.this).unregisterReceiver(mWifiReceiver);
                }
            }
        });

    }

    private void sendMessage(String message) {
        Intent intent = new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intent.putExtra(TAG_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    public void onClick(View v) {
        NotificationBuilder.notify(this, R.drawable.ic_info_black_24dp, getResources().getString(R.string.app_name),
                getResources().getString(R.string.click), getResources().getInteger(R.integer.click), false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "onDestroy");
        startService(new Intent(this, NotificationService.class));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent(this, NotificationService.class);
        intent.putExtra(TAG_HOURS, hourOfDay);
        intent.putExtra(TAG_MINUTES, minute);
        startService(intent);
    }
}
