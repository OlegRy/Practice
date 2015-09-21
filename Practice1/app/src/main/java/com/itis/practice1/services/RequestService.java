package com.itis.practice1.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.itis.practice1.PracticeActivity;
import com.itis.practice1.R;
import com.itis.practice1.model.Place;
import com.itis.practice1.utils.ParseUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestService extends IntentService {

    private  String mRequestUrl = "https://geocode-maps.yandex.ru/1.x/?geocode=%s,%s&sco=latlong" +
            "&rspn=0&kind=locality&format=json";

    public static final String SERVICE_INTENT_BROADCAST = "RequestService";

    public RequestService() {
        super("RequestService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.getExtras() != null) {
            double latitude = intent.getDoubleExtra(PracticeActivity.LATITUDE_TAG, 0);
            double longitude = intent.getDoubleExtra(PracticeActivity.LONGITUTE_TAG, 0);
            if (!isNetworkConnected()) {
                sendMessage(getResources().getString(R.string.no_connection), latitude, longitude);
            }
            load(latitude, longitude);
        }
    }

    private void load(double latitude, double longitude) {
        try {
            List<Place> places = ParseUtils.parse(executeRequest(latitude, longitude));
            sendMessage(places, -1, -1);
        } catch (IOException | JSONException e) {
            sendMessage(getResources().getString(R.string.error_processing), latitude, longitude);
        }
    }

    public String executeRequest(double latitude, double longitude) throws IOException {
        String url = String.format(mRequestUrl, Double.toString(latitude), Double.toString(longitude));
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void sendMessage(Object message, double latitude, double longitude) {
        Intent intent = new Intent(SERVICE_INTENT_BROADCAST);

        if (message instanceof String) {
            intent.putExtra(PracticeActivity.BROADCAST_BOOLEAN, true);
            intent.putExtra(PracticeActivity.BROADCAST_MESSAGE, message.toString());
        } else {
            intent.putExtra(PracticeActivity.BROADCAST_BOOLEAN, false);
            intent.putExtra(PracticeActivity.LATITUDE_TAG, latitude);
            intent.putExtra(PracticeActivity.LONGITUTE_TAG, longitude);
            intent.putExtra(PracticeActivity.BROADCAST_MESSAGE, (ArrayList<Place>) message);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
